package com.intervalintl.intro;

import android.app.Application;
import timber.log.Timber;


public class IntroApplication extends Application /*implements HasActivityInjector*/ {

    //@Inject
    //DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    private AppComponent appComponent;
    //private Picasso picasso;


    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                //.netModule(new NetModule("https://api.github.com"))
                .setApplication(this)
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //AppInjector.init(this);
    }

    /*@Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }*/

    public AppComponent getAppComponent() {
        return appComponent;
    }
/*
    public Picasso getPicasso() {

        if (picasso == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            String token = appComponent.getUserManager().getToken();

                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", AppUtil.buildAuthorizationHeader(token))
                                    .build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(client))
                    .build();
        }

        return picasso;
    }
*/
}