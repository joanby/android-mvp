package com.juangabriel.moviesfeed.root;


import com.juangabriel.moviesfeed.MainActivity;
import com.juangabriel.moviesfeed.http.MovieExtraInfoApiModule;
import com.juangabriel.moviesfeed.http.MovieTitleApiModule;
import com.juangabriel.moviesfeed.movies.MoviesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        MoviesModule.class,
        MovieTitleApiModule.class,
        MovieExtraInfoApiModule.class
})
public interface ApplicationComponent {

    void inject(MainActivity target);
}
