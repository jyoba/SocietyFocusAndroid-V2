package com.zircon.app.utils;

import com.zircon.app.App;
import com.zircon.app.BuildConfig;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by jikoobaruah on 21/01/16.
 */
public class HTTP {

    private static HTTP mInstance;
    private API mApi;

    private HTTP() {

        File cache = new File(App.appInstance.getCacheDir().getPath() + "/HTTP");
        if (!cache.exists())
            cache.mkdir();


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder clientBuilder = new OkHttpClient
                .Builder()
                .cache(new Cache(cache, 1 * 1024 * 1024)) // 1 MB
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();
                        if (NetworkUtils.isNetworkAvailable()) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                });

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(logging);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();


        mApi = retrofit.create(API.class);


    }

    public static API getAPI() {
        if (mInstance == null)
            mInstance = new HTTP();

        return mInstance.mApi;
    }


}