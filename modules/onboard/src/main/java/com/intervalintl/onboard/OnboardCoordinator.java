package com.intervalintl.onboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.interval.common.Constants;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.onboard.splash.SplashViewModel;
import com.intervalintl.onboard.splash.SplashViewModelBuilder;
import javax.inject.Inject;


public class OnboardCoordinator extends Coordinator<OnboardCoordinatorBuilder.Component> {

    private enum Stage {
        Idle,
        Splash,
        Onboarding,
        Done
    }

    private Stage stage = Stage.Idle;
    private Handler mainHandler;

    // Children Coordinator
    private SplashViewModel splashViewModel;

    @Inject
    Listener listener;


    public OnboardCoordinator(String id) {
        super(id);
        setupChildrenCoordinator();
    }

    @Override
    public void onInputStateChange(OnboardCoordinatorBuilder.Component inputInjector) {

        inputInjector.inject(this);

        // Update Children dependencies.
        if (splashViewModel != null) {
            SplashViewModelBuilder splashBuilder = new SplashViewModelBuilder(inputInjector);
            splashViewModel.onInputStateChange(splashBuilder.build(splashListener));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void start() {

        if (stage == Stage.Idle) {
            stage = Stage.Splash;
            getChildById(Constants.SPLASH_VIEWMODEL_ID).start();
        }
        else if (stage == Stage.Splash) {
            // The SplashViewModel is already onStage, so don't need to start it twice.
            // showSplashScreen();
        }

    }

    /**
     * The time to setup initial child Coordinators that this Coordinator will use.
     * */
    private void setupChildrenCoordinator() {
        splashViewModel = new SplashViewModel(Constants.SPLASH_VIEWMODEL_ID);
        registerChildForActivityEvents(splashViewModel);

    }


    private SplashViewModel.Listener splashListener = new SplashViewModel.Listener() {
        @Override
        public void onSplashFinished() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Pablo", "Splash finish Success");
                    listener.onBoardingSuccess();
                }
            });
        }
    };


    public interface Listener {
        void onBoardingSuccess();
        void onBoardingFail();
    }

}
