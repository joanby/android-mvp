package com.juangabriel.moviesfeed.http;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieExtraInfoApiModule {
    public final String BASE_URL = "http://www.omdbapi.com/";
    public final String API_KEY = "2b514a00";

   @Provides
   public OkHttpClient provideClient(){
       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

       return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
           @Override
           public Response intercept(Chain chain) throws IOException {
               Request request = chain.request();
               HttpUrl url = request.url().newBuilder().addQueryParameter("apikey", API_KEY).build();
               request = request.newBuilder().url(url).build();
               return chain.proceed(request);
           }
       }).build();
   }

   @Provides
   public Retrofit provideRetrofit(String baseUrl, OkHttpClient client){
       return new Retrofit.Builder()
               .baseUrl(baseUrl)
               .client(client)
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(GsonConverterFactory.create())
               .build();
   }

   @Provides
    public MoviesExtraInfoApisService provideApiService(){
       return provideRetrofit(BASE_URL, provideClient()).create(MoviesExtraInfoApisService.class);
   }

}
