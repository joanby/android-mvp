package com.juangabriel.moviesfeed.movies;

import com.juangabriel.moviesfeed.http.apimodel.Result;

import io.reactivex.Observable;

public interface Repository {

    Observable<Result> getResultFromNetwork();
    Observable<Result> getResultFromCache();
    Observable<Result> getResultData();

    Observable<String> getCountryFromNetwork();
    Observable<String> getCountryFromCache();
    Observable<String> getCountryData();





}
