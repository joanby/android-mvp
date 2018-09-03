package com.juangabriel.daggerlogin.login;

public interface LoginRepository {

    void saveUser(User user);

    User getUser();
}
