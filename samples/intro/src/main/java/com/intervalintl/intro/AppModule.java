package com.intervalintl.intro;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.interval.common.login.UserManager;
import com.interval.common.login.UserManagerMock;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


@Module
class AppModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    UserManager provideUserManager() {
        return new UserManagerMock();
    }

/*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 100 * 1024 * 1024; // 100 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        BooleanSerializer serializer = new BooleanSerializer();
        gsonBuilder.registerTypeAdapter(Boolean.class, serializer);
        gsonBuilder.registerTypeAdapter(boolean.class, serializer);
        return gsonBuilder.create();
    }
*/
}