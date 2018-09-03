package com.juangabriel.moviesfeed.http;

import com.juangabriel.moviesfeed.http.apimodel.TopMoviesRated;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiService {

    @GET("top_rated")
    Observable<TopMoviesRated> getTopMoviesRated(@Query("page") Integer page);
}
