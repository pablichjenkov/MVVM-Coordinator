package com.intervalintl.onboard.splash;

import android.util.Log;
import com.interval.common.Constants;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import javax.inject.Inject;


public class SplashViewModel extends Coordinator<SplashViewModelBuilder.Component> {

    public enum Stage {
        Idle,
        Splash,
        Done
    }

    @Inject
    Listener listener;

    @Inject
    CoordinatorScreenManager screenManager;

    private Stage stage;


    public SplashViewModel(String viewModelId) {
        super(viewModelId);
        stage = Stage.Idle;
    }

    @Override
    public void onInputStateChange(SplashViewModelBuilder.Component inputInjector) {
        inputInjector.inject(this);
    }

    @Override
    public void start() {
        if (stage.equals(Stage.Idle)) {
            showSplashScreen();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        screenManager = null;
    }

    public Stage getStage() {
        return stage;
    }

    private void showSplashScreen() {
        SplashFragment splashFragment;
        splashFragment = new SplashFragment();
        splashFragment.setCoordinatorId(getId());

        screenManager.setView(splashFragment, Constants.SPLASH_FRAGMENT_TAG);
    }

    public void startSplashTimeout() {
        stage = Stage.Splash;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep( 1500);

                    Log.d("SplashViewModel","SplashViewModel: Dispatching splash timeout");
                    listener.onSplashFinished();
                    stage = Stage.Done;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }


    public interface Listener {
        void onSplashFinished();
    }

}
