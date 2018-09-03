package com.juangabriel.daggerlogin.root;


import com.juangabriel.daggerlogin.http.TwitchModule;
import com.juangabriel.daggerlogin.LoginActivity;
import com.juangabriel.daggerlogin.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LoginModule.class, TwitchModule.class})
public interface ApplicationComponent {

    void inject(LoginActivity target);
}
