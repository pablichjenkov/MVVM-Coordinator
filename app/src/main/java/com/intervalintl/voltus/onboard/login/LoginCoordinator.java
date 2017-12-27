package com.intervalintl.voltus.onboard.login;

import com.intervalintl.voltus.MainActivity;
import com.intervalintl.voltus.onboard.OnboardCoordinator;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.util.CoordinatorUtil;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.BaseViewModel;
import com.intervalintl.voltus.viewmodel.Coordinator;


public class LoginCoordinator extends Coordinator {


    private LoginViewModel loginViewModel;


    public LoginCoordinator(String tagId) {
        super(tagId);
        loginViewModel = new LoginViewModel();
        loginViewModel.setListener(loginListener);
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    @Override
    public void dispatch() {
        showLoginScreen();
    }

    @Override
    public void shut() {

    }

    @Override
    public <VM extends BaseViewModel> VM provideViewModel(BaseFragment baseFragment) {

        BaseViewModel viewModel = null;
        String viewModelId = baseFragment.viewModelId;
        Class<? extends BaseViewModel> viewModelClass = baseFragment.viewModelClass;

        // TODO(Pablo): use a map is more scalable
        if (viewModelClass.isAssignableFrom(LoginViewModel.class)) {
            viewModel = loginViewModel;
        }

        return (VM)viewModel;
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

    public  void showLoginScreen() {
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        loginFragment.coordinatorId = CoordinatorUtil.COORDINATOR_LOGIN_ID;
        loginFragment.viewModelClass = LoginViewModel.class;
        loginFragment.viewModelId = "loginViewModelDefault";

        Link link = Link.Builder.newLink()
                .toRoute(loginFragment, "LoginFragment")
                .build();

        router.handleLink(link);
    }

}
