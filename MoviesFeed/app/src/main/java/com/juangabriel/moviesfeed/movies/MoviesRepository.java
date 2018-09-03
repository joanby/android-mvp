package com.juangabriel.moviesfeed.movies;

import com.juangabriel.moviesfeed.http.MoviesApiService;
import com.juangabriel.moviesfeed.http.MoviesExtraInfoApisService;
import com.juangabriel.moviesfeed.http.apimodel.OmdbApi;
import com.juangabriel.moviesfeed.http.apimodel.Result;
import com.juangabriel.moviesfeed.http.apimodel.TopMoviesRated;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MoviesRepository implements Repository {

    private MoviesApiService moviesApiService;
    private MoviesExtraInfoApisService extraInfoApisService;

    private List<String> countries;
    private List<Result> results;
    private long lastTimestamp;

    private static final long CACHE_LIFETIME = 20 * 1000; //Cache que durará 20 segundos

    public MoviesRepository(MoviesApiService mService, MoviesExtraInfoApisService eService){
        this.moviesApiService = mService;
        this.extraInfoApisService = eService;

        this.lastTimestamp = System.currentTimeMillis();

        this.countries = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public boolean isUpdated(){
        return (System.currentTimeMillis() - lastTimestamp) < CACHE_LIFETIME;
    }

    @Override
    public Observable<Result> getResultFromNetwork() {

        Observable<TopMoviesRated> topMoviesRatedObservable = moviesApiService.getTopMoviesRated(1)
                /*.concatWith(moviesApiService.getTopMoviesRated(2))
                .concatWith(moviesApiService.getTopMoviesRated(3))*/;

        return topMoviesRatedObservable
                .concatMap(new Function<TopMoviesRated, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(TopMoviesRated topMoviesRated) {
                        return Observable.fromIterable(topMoviesRated.getResults());
                    }
                }).doOnNext(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        results.add(result);
                    }
                });

    }

    @Override
    public Observable<Result> getResultFromCache() {
       if (isUpdated()){
           return Observable.fromIterable(results);
       }else {
           lastTimestamp = System.currentTimeMillis();
           results.clear();
           return Observable.empty();
       }
    }

    @Override
    public Observable<Result> getResultData() {
        return getResultFromCache().switchIfEmpty(getResultFromNetwork());
    }

    @Override
    public Observable<String> getCountryFromNetwork() {
        return getResultFromNetwork().concatMap(new Function<Result, Observable<OmdbApi>>() {
            @Override
            public Observable<OmdbApi> apply(Result result) {
                return extraInfoApisService.getExtraInfoMovie(result.getTitle());
            }
        }).concatMap(new Function<OmdbApi, Observable<String>>() {
            @Override
            public Observable<String> apply(OmdbApi omdbApi){
                if (omdbApi == null || omdbApi.getCountry() == null){
                    return Observable.just("Desconocido");
                }else {
                    return Observable.just(omdbApi.getCountry());
                }
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String country) {
                countries.add(country);
            }
        });
    }

    @Override
    public Observable<String> getCountryFromCache() {
       if (isUpdated()){
           return Observable.fromIterable(countries);
       }else{
           lastTimestamp = System.currentTimeMillis();
           countries.clear();
           return Observable.empty();
       }
    }

    @Override
    public Observable<String> getCountryData() {
        return getCountryFromCache().switchIfEmpty(getCountryFromNetwork());
    }
}
