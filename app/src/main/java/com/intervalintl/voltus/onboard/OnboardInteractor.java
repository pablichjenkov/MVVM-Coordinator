package com.intervalintl.voltus.onboard;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.intervalintl.voltus.MainActivity;
import com.intervalintl.voltus.onboard.login.LoginFragment;
import com.intervalintl.voltus.onboard.login.LoginViewModel;
import com.intervalintl.voltus.onboard.splash.SplashFragment;
import com.intervalintl.voltus.onboard.splash.SplashViewModel;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.root.LinkHandler;
import com.intervalintl.voltus.util.ReflectionUtil;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.Interactor;


public class OnboardInteractor extends Interactor {

    private Object listener;
    private LinkHandler linkHandler;
    private AppCompatActivity activity;
    private int backButtonExitCount = 2;
    private SplashFragment splashFragment;
    private SplashViewModel splashViewModel;
    private LoginFragment loginFragment;
    private LoginViewModel loginViewModel;


    public OnboardInteractor(String tagId, Object listener, LinkHandler linkHandler, AppCompatActivity activity) {
        super(tagId);
        this.listener = listener;
        this.linkHandler = linkHandler;
        this.activity = activity;
    }

    public void setListener(Object listener) {
        this.listener = listener;
    }

    @Override
    public void act() {
        showSplashScreen();
    }

    @Override
    public void shut() {

    }

    @Override
    public boolean handleBackPress() {
        Toast.makeText(activity, "Press Back Again", Toast.LENGTH_SHORT).show();
        backButtonExitCount --;
        return backButtonExitCount > 0;
    }

    public  void showSplashScreen() {
        splashFragment = new SplashFragment();
        splashFragment.viewModelId = "splashViewModelDefault";
        splashFragment.viewModelClass = SplashViewModel.class;
        splashFragment.listener = new BaseFragment.Listener<SplashViewModel>() {
            @Override
            public void onViewModelReady(SplashViewModel viewModel) {
                splashViewModel = viewModel;
                splashViewModel.setListener(splashListener);
            }
        };

        /*new FragmentViewModelBinder<SplashViewModel>(activity.getSupportFragmentManager()
                , new FragmentViewModelBinder.Callback<SplashViewModel>() {
                    @Override
                    public void onViewModelBound(SplashViewModel splashViewModel) {
                        setupSplashViewModel(splashViewModel);
                    }
                }
                , splashFragment
                , SplashViewModel.class
                , "splashViewModelDefault");*/


        Link link = Link.Builder.newLink()
                .toRoute(splashFragment, "SplashFragment")
                .build();

        linkHandler.handleLink(link);
    }

    SplashViewModel.Listener splashListener = new SplashViewModel.Listener() {
        @Override
        public void onSplashFinished() {

            ReflectionUtil.invokeMethod(listener
                    , CALLBACK_METHOD_ON_SPLASH_END
                    , null
                    , null);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoginScreen();
                }
            });

        }
    };

    public  void showLoginScreen() {
        loginFragment = new LoginFragment();
        loginFragment.viewModelClass = LoginViewModel.class;
        loginFragment.viewModelId = "loginViewModelDefault";
        loginFragment.setListener(new BaseFragment.Listener<LoginViewModel>() {
            @Override
            public void onViewModelReady(LoginViewModel viewModel) {
                loginViewModel = viewModel;
                loginViewModel.setListener(loginListener);
            }
        });

        Link link = Link.Builder.newLink()
                .toRoute(loginFragment, "LoginFragment")
                .build();

        linkHandler.handleLink(link);
    }

    LoginViewModel.Listener loginListener = new LoginViewModel.Listener() {
        @Override
        public void onLoginSuccess() {

            Link link = Link.Builder.newLink()
                    .toRoute(MainActivity.class)
                    .build();

            linkHandler.handleLink(link);

        }
    };


    // Private Callback method names
    private static final String CALLBACK_METHOD_ON_SPLASH_END = "onSplashEnd";

}
