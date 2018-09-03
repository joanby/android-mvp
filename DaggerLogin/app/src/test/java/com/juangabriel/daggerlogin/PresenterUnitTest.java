package com.juangabriel.daggerlogin;

import com.juangabriel.daggerlogin.login.LoginActivityMVP;
import com.juangabriel.daggerlogin.login.LoginActivityPresenter;
import com.juangabriel.daggerlogin.login.User;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PresenterUnitTest {


    LoginActivityPresenter presenter;
    User user;

    LoginActivityMVP.Model mockedModel;
    LoginActivityMVP.View mockedView;

    @Before
    public void initialization(){
        mockedModel = mock(LoginActivityMVP.Model.class);
        mockedView = mock(LoginActivityMVP.View.class);

        user = new User("Manolo", "Escobar");

        //when(mockedModel.getUser()).thenReturn(user);

        //when(mockedView.getFirstName()).thenReturn("Antonio");
        //when(mockedView.getLastName()).thenReturn("Banderas");

        presenter = new LoginActivityPresenter(mockedModel);
        presenter.setView(mockedView);
    }

    @Test
    public void noExistsInteractionWithView(){
        presenter.getCurrentUser();

        verify(mockedView, times(1)).showUserNotAvailable();
    }


    @Test
    public void loadUserFromTheRepoWhenValidUserIsPresent(){
        when(mockedModel.getUser()).thenReturn(user);

        presenter.getCurrentUser();

        //Comprobamos la interactuación con el modelo de datos
        verify(mockedModel, times(1)).getUser();


        //Comprobamos la interactuación con la vista
        verify(mockedView, times(1)).setFirstName("Manolo");
        verify(mockedView, times(1)).setLastName("Escobar");
        verify(mockedView, never()).showUserNotAvailable();

    }

    @Test
    public void showErrorMessageWhenUserIsNull(){
        when(mockedModel.getUser()).thenReturn(null);

        presenter.getCurrentUser();

        //Comprobamos la interactuación con el modelo de datos
        verify(mockedModel, times(1)).getUser();


        //Comprobamos la interactuación con la vista
        verify(mockedView, never()).setFirstName("Manolo");
        verify(mockedView, never()).setLastName("Escobar");
        verify(mockedView, times(1)).showUserNotAvailable();

    }

    @Test
    public void createErrorMessageIfAnyFieldIsEmpty(){
        //Primera prueba poniendo el campo firstname vacío
        when(mockedView.getFirstName()).thenReturn("");

        presenter.loginButtonClicked();

        verify(mockedView, times(1)).getFirstName();
        verify(mockedView, never()).getLastName();
        verify(mockedView, times(1)).showInputError();


        //Segunda prueba poniendo un valor en el campo firstname y dejando el lastname vacío
        when(mockedView.getFirstName()).thenReturn("Antonio");
        when(mockedView.getLastName()).thenReturn("");

        presenter.loginButtonClicked();

        verify(mockedView, times(2)).getFirstName();//El método se llama dos veces, una en la prueba anterior, y una en la actual!
        verify(mockedView, times(1)).getLastName(); //Este método es la primera vez que se llama, ya que antes no pasó el test booleano
        verify(mockedView, times(2)).showInputError();//El método se llamó antes y de nuevo ahora, en total dos veces
    }


    @Test
    public void saveValidUser(){

        when(mockedView.getFirstName()).thenReturn("Juan Gabriel");
        when(mockedView.getLastName()).thenReturn("Gomila");

        presenter.loginButtonClicked();

        //Las llamadas deben ser dobles, una en el if y otro cuando se crea el usuario
        verify(mockedView, times(2)).getFirstName();
        verify(mockedView, times(2)).getLastName();

        //Miramos que el modelo persista en el repositorio
        verify(mockedModel, times(1)).createUser("Juan Gabriel", "Gomila");

        //Miramos que se muestre el mensaje de éxito al usuario
        verify(mockedView, times(1)).showUserSaved();


    }
}
