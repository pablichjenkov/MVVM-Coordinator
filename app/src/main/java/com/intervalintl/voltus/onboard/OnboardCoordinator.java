package com.intervalintl.voltus.onboard;

import android.content.Intent;
import android.util.Log;
import com.intervalintl.voltus.onboard.login.LoginCoordinator;
import com.intervalintl.voltus.onboard.splash.SplashFragment;
import com.intervalintl.voltus.onboard.splash.SplashViewModel;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.util.CoordinatorUtil;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.BaseViewModel;
import com.intervalintl.voltus.viewmodel.Coordinator;
import java.util.Map;


public class OnboardCoordinator extends Coordinator {

    private enum State {
        Idle,
        Splash,
        Onboarding,
        Login
    }

    private State state = State.Idle;
    private LoginCoordinator loginCoordinator;
    private SplashViewModel splashViewModel;
    private Map<String, BaseViewModel> viewModeByIdMap;


    public OnboardCoordinator(String tagId) {
        super(tagId);
        loginCoordinator = new LoginCoordinator(CoordinatorUtil.COORDINATOR_LOGIN_ID);
        addChild(CoordinatorUtil.COORDINATOR_LOGIN_ID, loginCoordinator);
        splashViewModel = new SplashViewModel();
        splashViewModel.setListener(splashListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If requestCode matches this, don't call super to not propagate the event down to children
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dispatch() {
        if (state == State.Idle) {
            showSplashScreen();
        }
        else if (state == State.Splash) {
            //showSplashScreen();

        }
        else if (state == State.Login) {
            //showLoginScreen();
        }
        Log.d("Pablo", "Onboarding Coordinator.dispatch() State-> " + state.name());
    }

    @Override
    public void shut() {
        // This interactor Host Activity is being destroyed
    }

    @Override
    public <VM extends BaseViewModel> VM provideViewModel(BaseFragment baseFragment) {
        BaseViewModel viewModel = null;
        String viewModelId = baseFragment.viewModelId;
        Class<? extends BaseViewModel> viewModelClass = baseFragment.viewModelClass;

        // TODO(Pablo): use a map is more scalable
        if (viewModelClass.isAssignableFrom(SplashViewModel.class)) {
            viewModel = splashViewModel;
        }

        return (VM)viewModel;
    }

    @Override
    public boolean handleBackPress() {

        if (state == State.Splash) {

        }
        else if (state == State.Login) {
            state = State.Splash;
            showSplashScreen();
            return true;
        }

        return false;
    }

    public  void showSplashScreen() {
        state = State.Splash;
        SplashFragment splashFragment;
        splashFragment = new SplashFragment();
        splashFragment.coordinatorId = CoordinatorUtil.COORDINATOR_ONBOARD_ID;
        splashFragment.viewModelClass = SplashViewModel.class;
        splashFragment.viewModelId = "splashViewModelDefault";

        Link link = Link.Builder.newLink()
                .toRoute(splashFragment, "SplashFragment")
                .build();

        router.handleLink(link);
    }

    SplashViewModel.Listener splashListener = new SplashViewModel.Listener() {
        @Override
        public void onSplashFinished() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Pablo", "Onboarding Coordinator.splashListener() State-> " + state.name());
                    state = OnboardCoordinator.State.Login;
                    loginCoordinator.dispatch();
                }
            });
        }
    };


}
