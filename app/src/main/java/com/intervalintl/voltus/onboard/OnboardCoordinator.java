package com.intervalintl.voltus.onboard;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.intervalintl.voltus.MainActivity;
import com.intervalintl.voltus.onboard.login.LoginFragment;
import com.intervalintl.voltus.onboard.login.LoginViewModel;
import com.intervalintl.voltus.onboard.splash.SplashFragment;
import com.intervalintl.voltus.onboard.splash.SplashViewModel;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.root.Router;
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
    private Map<String, BaseViewModel> viewModeByIdMap;
    private Router router;
    private Handler mainHandler;
    private SplashViewModel splashViewModel;
    private LoginViewModel loginViewModel;


    public OnboardCoordinator(String tagId) {
        super(tagId);
        splashViewModel = new SplashViewModel();
        splashViewModel.setListener(splashListener);
        loginViewModel = new LoginViewModel();
        loginViewModel.setListener(loginListener);
    }

    public void onCreate(Router router) {
        this.router = router;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void act() {
        if (state == State.Idle) {
            showSplashScreen();
        }
        else if (state == State.Splash) {
            //showSplashScreen();

        }
        else if (state == State.Login) {
            //showLoginScreen();
        }
        Log.d("Pablo", "Onboarding Coordinator.act() State-> " + state.name());
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
        else if (viewModelClass.isAssignableFrom(LoginViewModel.class)) {
            viewModel = loginViewModel;
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
        splashFragment.interactorId = CoordinatorUtil.COORDINATOR_ONBOARD_ID;
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
                    showLoginScreen();
                }
            });
        }
    };

    public  void showLoginScreen() {
        state = State.Login;
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        loginFragment.interactorId = CoordinatorUtil.COORDINATOR_ONBOARD_ID;
        loginFragment.viewModelClass = LoginViewModel.class;
        loginFragment.viewModelId = "loginViewModelDefault";

        Link link = Link.Builder.newLink()
                .toRoute(loginFragment, "LoginFragment")
                .build();

        router.handleLink(link);
    }

    LoginViewModel.Listener loginListener = new LoginViewModel.Listener() {
        @Override
        public void onLoginSuccess() {

            Link link = Link.Builder.newLink()
                    .toRoute(MainActivity.class)
                    .build();

            router.handleLink(link);

        }
    };

}
