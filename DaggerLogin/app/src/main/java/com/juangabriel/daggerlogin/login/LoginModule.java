package com.juangabriel.daggerlogin.login;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    public LoginActivityMVP.Presenter provideLoginActivityPresenter(LoginActivityMVP.Model model){
        return new LoginActivityPresenter(model);
    }

    @Provides
    public LoginActivityMVP.Model provideLoginActivityModel(LoginRepository repository){
        return new LoginActivityModel(repository);
    }

    @Provides
    public LoginRepository provideLoginRepository(){
        return new MemoryRepository();//cambiar aquí si queremos en lugar de un repo en memoria, una BD, un cloud...
    }
}
