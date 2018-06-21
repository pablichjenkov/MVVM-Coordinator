package com.intervalintl.login;

import com.interval.common.Constants;
import com.intervalintl.coordinator.Coordinator;
import javax.inject.Inject;


public class LoginCoordinator extends Coordinator<LoginCoordinatorBuilder.Component> {

    public enum Stage {
        Idle,
        LoginViewModel,
        SignupViewModel
    }

    private Stage stage;
    private Coordinator onStageCoordinator;
    private LoginViewModel loginViewModel;

    @Inject
    Listener listener;

    public LoginCoordinator(String coordinatorId) {
        super(coordinatorId);
        stage = Stage.Idle;
        initChildrenCoordinator();
    }

    @Override
    public void onInputStateChange(LoginCoordinatorBuilder.Component componentInjector) {

        componentInjector.inject(this);

        // Only propagate dependency updates if the child coordinator is not null
        if (loginViewModel != null) {
            LoginViewModelBuilder loginViewModelBuilder = new LoginViewModelBuilder(componentInjector);
            loginViewModel.onInputStateChange(loginViewModelBuilder.build(loginViewModelListener));
        }

    }

    @Override
    public void start() {

        if (stage == Stage.Idle) {
            stage = Stage.LoginViewModel;
            onStageCoordinator = getChildById(Constants.LOGIN_VIEWMODEL_ID);
            onStageCoordinator.start();
        }

    }

    public void initChildrenCoordinator() {
        loginViewModel = new LoginViewModel(Constants.LOGIN_VIEWMODEL_ID);
        registerChildForActivityEvents(loginViewModel);
    }


    private LoginViewModel.Listener loginViewModelListener = new LoginViewModel.Listener() {

        @Override
        public void onLoginSuccess() {
            listener.onLoginSuccess();
        }

        @Override
        public void onLoginFailed() {
            listener.onLoginFailed();
        }

        @Override
        public void onSignupSuccess() {
            listener.onSignupSuccess();
        }

        @Override
        public void onSignupFailed() {
            listener.onSignupFailed();
        }

    };


    public interface Listener {
        void onLoginSuccess();
        void onLoginFailed();
        void onSignupSuccess();
        void onSignupFailed();
    }

}
