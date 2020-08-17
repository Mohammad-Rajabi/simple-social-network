package com.zoho.mohammadrajabi.socialnetwork.di.modules;


import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;
import com.zoho.mohammadrajabi.socialnetwork.util.NetworkUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public Retrofit provideRetrofit(NetworkUtil networkUtil) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (!networkUtil.connectivity()) throw new ConnectException();
                Request oldRequest = chain.request();
                Request.Builder newRequestBuilder = oldRequest.newBuilder();

//            if (TokenContainer.getToken() != null) {
//                newRequestBuilder.addHeader("Authorization", "Bearer " + TokenContainer.getToken());
//            }
//            newRequestBuilder.addHeader("accept", "application/json");
                newRequestBuilder.method(oldRequest.method(), oldRequest.body());
                return chain.proceed(newRequestBuilder.build());
            }
        }).connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://myapiservices.ir/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
