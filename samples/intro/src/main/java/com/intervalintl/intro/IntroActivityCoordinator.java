package com.intervalintl.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.interval.common.Constants;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.login.LoginCoordinator;
import com.intervalintl.login.LoginCoordinatorBuilder;
import com.intervalintl.onboard.OnboardCoordinator;
import com.intervalintl.onboard.OnboardCoordinatorBuilder;
import javax.inject.Inject;


public class IntroActivityCoordinator extends Coordinator<IntroActivityCoordinatorBuilder.Component> {

    private enum Stage {
        Idle,
        Onboarding,
        Login,
        Done
    }

    private Stage stage = Stage.Idle;
    private Handler mainHandler;

    // Children Coordinator
    private OnboardCoordinator onboardCoordinator;
    private LoginCoordinator loginCoordinator;

    @Inject
    AppCompatActivity appCompatActivity;


    public IntroActivityCoordinator(String id) {
        super(id);

        setupChildrenCoordinator();
    }

    @Override
    public void onInputStateChange(IntroActivityCoordinatorBuilder.Component inputInjector) {

        inputInjector.inject(this);

        if (onboardCoordinator != null) {
            OnboardCoordinatorBuilder onboardCoordinatorBuilder = new OnboardCoordinatorBuilder(inputInjector);
            onboardCoordinator.onInputStateChange(onboardCoordinatorBuilder.build(onboardingListener));
        }

        if (loginCoordinator != null) {
            LoginCoordinatorBuilder loginCoordinatorBuilder = new LoginCoordinatorBuilder(inputInjector);
            loginCoordinator.onInputStateChange(loginCoordinatorBuilder.build(loginListener));
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
            stage = Stage.Onboarding;
            //getChildById(Constants.SPLASH_VIEWMODEL_ID).start();
            onboardCoordinator.start();
        }
        else if (stage == Stage.Onboarding) {
            // The SplashViewModel is already onStage, so don't need to start it twice.
            // showSplashScreen();
        }
        else if (stage == Stage.Login) {
            //showLoginScreen();
        }
    }

    /**
     * The time to setup initial child Coordinators that this Coordinator will use.
     * */
    private void setupChildrenCoordinator() {

        onboardCoordinator = new OnboardCoordinator(Constants.ONBOARD_COORDINATOR_ID);
        registerChildForActivityEvents(onboardCoordinator);

        loginCoordinator = new LoginCoordinator(Constants.LOGIN_COORDINATOR_ID);
        registerChildForActivityEvents(loginCoordinator);
    }


    private OnboardCoordinator.Listener onboardingListener = new OnboardCoordinator.Listener() {

        @Override
        public void onBoardingSuccess() {
            loginCoordinator.start();
        }

        @Override
        public void onBoardingFail() {
            Toast.makeText(appCompatActivity, "OnboardFail", Toast.LENGTH_SHORT).show();
        }

    };

    private LoginCoordinator.Listener loginListener = new LoginCoordinator.Listener() {
        @Override
        public void onLoginSuccess() {
            Log.d("Pablo", "Login Success, Proceding to Home Activity with Navigation Coordinator");

            Intent intent = new Intent(appCompatActivity, HomeActivity.class);
            appCompatActivity.startActivity(intent);

        }

        @Override
        public void onLoginFailed() {
            Log.d("Pablo", "Login Failed, What to do ?");
        }

        @Override
        public void onSignupSuccess() {
            Log.d("Pablo", "Signup Success, Proceding to Home Coordinator or what?");
        }

        @Override
        public void onSignupFailed() {
            Log.d("Pablo", "Signup Failed, What to do ?");
        }
    };

}
