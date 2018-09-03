package com.juangabriel.daggerlogin;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juangabriel.daggerlogin.http.TwitchAPI;
import com.juangabriel.daggerlogin.http.twitch.Game;
import com.juangabriel.daggerlogin.http.twitch.Twitch;
import com.juangabriel.daggerlogin.login.LoginActivityMVP;
import com.juangabriel.daggerlogin.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View {

    @Inject
    LoginActivityMVP.Presenter presenter;

    @Inject
    TwitchAPI twitchAPI;

    @BindView(R.id.edit_text_first_name)
    EditText firstName;

    @BindView(R.id.edit_text_last_name)
    EditText lastName;

    @Nullable @BindViews({R.id.edit_text_first_name, R.id.edit_text_last_name, R.id.button_login})
    List<View> inputs;

    @BindView(R.id.button_login)
    Button loginButton;

    @BindString(R.string.main_activity_name)
    String title;

    @BindDrawable(R.drawable.ic_launcher_foreground)
    Drawable imageForeground;

    @BindColor(R.color.colorMainActivity)
    ColorStateList mainColor;

    @BindDimen(R.dimen.spacing)
    int spacing;

    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };
    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };


    @Optional @OnClick(R.id.button_login)
    void loginClicked(Button b){
        b.setText("Enviado!");
        presenter.loginButtonClicked();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ButterKnife.apply(inputs, DISABLE);
        ButterKnife.apply(inputs, ENABLED, true);
        ButterKnife.apply(inputs, View.ALPHA, 1.0f);

        ((App) getApplication()).getComponent().inject(this);


        //Ejemplo de uso de la api de Twitch con retrofit
        /*Call<Twitch> call = twitchAPI.getTopGames("41l7gp8rw3q0jm0ssiwd77i2y5497o");

        call.enqueue(new Callback<Twitch>() {
            @Override
            public void onResponse(Call<Twitch> call, Response<Twitch> response) {
                List<Game> topGames = response.body().getGame();
                for (Game game : topGames){
                    System.out.println(game.getName());
                }
            }

            @Override
            public void onFailure(Call<Twitch> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
        twitchAPI.getTopGamesObservable("41l7gp8rw3q0jm0ssiwd77i2y5497o")
                .flatMap(new Function<Twitch, Observable<Game>>() {
                    @Override
                    public Observable<Game> apply(Twitch twitch) {
                        return Observable.fromIterable(twitch.getGame());
                    }
                })
                .flatMap(new Function<Game, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Game game){
                        return Observable.just(game.getName());
                    }
                }).filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return s.contains("w")||s.contains("W");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String name) {
                        System.out.println("RxJava says: "+name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getCurrentUser();
    }

    @Override
    public String getFirstName() {
        return this.firstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return this.lastName.getText().toString();
    }

    @Override
    public void showUserNotAvailable() {
        Toast.makeText(this, "Error, el usuario no está disponible", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInputError() {
        Toast.makeText(this, "Error, el nombre ni el apellido pueden estar vacíos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserSaved() {
        Toast.makeText(this, "¡Usuario guardado correctamente!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }
}
