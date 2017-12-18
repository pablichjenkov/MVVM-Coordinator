package com.intervalintl.voltus.onboard;

import android.support.v7.app.AppCompatActivity;
import com.intervalintl.voltus.MainActivity;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.root.LinkHandler;
import com.intervalintl.voltus.util.ReflectionUtil;
import com.intervalintl.voltus.viewmodel.FragmentViewModelBinder;
import java.util.Map;
import java.util.TreeMap;


public class OnboardComponent {

    private Object listener;
    private LinkHandler linkHandler;
    private SplashFragment splashFragment;
    private AppCompatActivity activity;

    public OnboardComponent(Object listener, LinkHandler linkHandler, AppCompatActivity activity) {
        this.listener = listener;
        this.linkHandler = linkHandler;
        this.activity = activity;
    }

    public void setListener(Object listener) {
        this.listener = listener;
    }

    public void onStage() {
        showSplashScreen();
    }

    public  void showSplashScreen() {
        //splashFragment = SplashFragment.newInstance(fields);
        splashFragment = new SplashFragment();

        new FragmentViewModelBinder<SplashViewModel>(activity.getSupportFragmentManager()
                , new FragmentViewModelBinder.Callback<SplashViewModel>() {
                    @Override
                    public void onViewModelBound(SplashViewModel splashViewModel) {
                        Map<String, Object> fields = new TreeMap<>();
                        fields.put("splashViewModel", splashViewModel);
                        ReflectionUtil.injectFields(splashFragment, fields);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep( 3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                splashFragment.getView().post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Link link = Link.Builder.newLink()
                                                .toRoute(MainActivity.class)
                                                .build();

                                        linkHandler.handleLink(link);
                                    }
                                });
                                ReflectionUtil.invokeMethod(listener
                                        , CALLBACK_METHOD_ON_SPLASH_END
                                        , null
                                        , null);
                            }
                        }).start();
                    }
                }
                , splashFragment
                , SplashViewModel.class
                , "splashViewModelDefault");


        Link link = Link.Builder.newLink()
                .toRoute(splashFragment, "SplashFragment")
                .build();

        linkHandler.handleLink(link);
    }


    // Private Callback method names
    private static final String CALLBACK_METHOD_ON_SPLASH_END = "onSplashEnd";

}
