package com.juangabriel.moviesfeed.movies;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MoviesPresenter implements MoviesMVP.Presenter {

    private MoviesMVP.View view;
    private MoviesMVP.Model model;

    private Disposable subscription = null;


    public MoviesPresenter(MoviesMVP.Model model){
        this.model = model;
    }


    @Override
    public void loadData() {
        subscription = model.result()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ViewModel>() {
                    @Override
                    public void onNext(ViewModel viewModel) {
                        if(view != null){
                            view.updateData(viewModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(view != null){
                            view.showSnackbar("Error al descargar las películas...");
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null){
                            view.showSnackbar("Información descargada con éxito");
                        }
                    }
                });
    }

    @Override
    public void rxJavaUnsuscribe() {
        if (subscription!= null && !subscription.isDisposed()){
            subscription.dispose();
        }
    }

    @Override
    public void setView(MoviesMVP.View view) {
        this.view = view;
    }
}
