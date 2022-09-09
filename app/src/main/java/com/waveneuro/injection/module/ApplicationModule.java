package com.waveneuro.injection.module;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.waveneuro.data.Config;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.DataManagerImpl;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.analytics.AnalyticsManagerImpl;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.data.preference.PreferenceManagerImpl;
import com.waveneuro.data.remote.TreatmentService;
import com.waveneuro.data.remote.UserService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    protected Application myApplication;

    public ApplicationModule(Application application) {
        this.myApplication = application;
    }

    @Provides
    public Application provideApplication() {
        return this.myApplication;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return this.myApplication;
    }

    @Singleton
    @Provides
    public PreferenceManager providePreferenceManager(@ApplicationContext Context context) {
        return new PreferenceManagerImpl(context);
    }

    @Singleton
    @Provides
    public UserService provideUserService(Gson gson, OkHttpClient client) {
        // Whenever Dagger needs to provide an instance of type UserService,
        // this code (the one inside the @Provides method) is run.

        return new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build()
                .create(UserService.class);
    }

    @Singleton
    @Provides
    public TreatmentService provideTreatmentService(Gson gson, OkHttpClient client) {
        // Whenever Dagger needs to provide an instance of type TreatmentService,
        // this code (the one inside the @Provides method) is run.

        return new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build()
                .create(TreatmentService.class);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context, PreferenceManager preferenceManager) {

        // Caching
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        // Interceptors
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // URL Interceptors
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            Request.@NotNull Builder requestBuilder = chain.request().newBuilder();
            Log.d("OKHTTP :: ", chain.request().url()+"");
            if (!chain.request().url().url().getPath().contains("login")) {
                requestBuilder.addHeader("Authorization", "Bearer " + preferenceManager.getAccessToken());
                Log.e("OKHTTP :: ","ACCESS TOKEN :: " + preferenceManager.getAccessToken());
                Response response = chain.proceed(requestBuilder.build());
                Log.e("OKHTTP :: ","RESPONSE :: " + response.code());
                if (response.code() == 401) {
                    response.close();
                    Request.@NotNull Builder refreshRequestBuilder = chain.request().newBuilder();
                    HttpUrl newUrl = new HttpUrl.Builder()
                            .scheme(chain.request().url().scheme())
                            .host(chain.request().url().host())
                            .addPathSegment("refresh")
                            .build();
                    refreshRequestBuilder.url(newUrl);
                    refreshRequestBuilder.post(new FormBody.Builder().build());
                    refreshRequestBuilder.addHeader("Authorization", "Bearer " + preferenceManager.getRefreshToken());
                    Response refreshResponse = chain.proceed(refreshRequestBuilder.build());
                    Log.e("OKHTTP :: ","REFRESH :: " + refreshResponse.code());
                    if (refreshResponse.code() == 401) {
                        return refreshResponse;
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(refreshResponse.body().string());
                            if(jsonObject.has("access_token")) {
                                preferenceManager.setAccessToken(""+jsonObject.getString("access_token"));
                                Log.e("OKHTTP :: ","REFRESH NEW A TOKEN 1:: " + preferenceManager.getAccessToken());
                                refreshResponse.close();
                                requestBuilder = request.newBuilder();
                                requestBuilder.removeHeader("Authorization");
                                requestBuilder.addHeader("Authorization", "Bearer " + preferenceManager.getAccessToken());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    return response;
                }
            }
            Log.e("OKHTTP :: ","REFRESH NEW A TOKEN 2 :: " + preferenceManager.getAccessToken());
            return chain.proceed(requestBuilder.build());
        });
        // Enable caching
        clientBuilder.cache(cache);

        return clientBuilder.build();
    }

    @Singleton
    @Provides
    public DataManager provideDataManager(UserService userService, TreatmentService treatmentService,
                                          PreferenceManager preferenceManager) {
        return new DataManagerImpl(userService, treatmentService, preferenceManager);
    }

    @Singleton
    @Provides
    public AnalyticsManager provideAnalyticsManager(@ApplicationContext Context context) {
        return new AnalyticsManagerImpl(context);
    }
}

