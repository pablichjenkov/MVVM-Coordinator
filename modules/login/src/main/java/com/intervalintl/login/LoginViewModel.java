package com.intervalintl.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;
import com.interval.common.Constants;
import com.interval.common.login.LoginEvent;
import com.interval.common.login.LoginFormData;
import com.interval.common.login.SignUpFormData;
import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class LoginViewModel extends Coordinator<LoginViewModelBuilder.Component> {

    public enum Stage {
        Idle,
        SavedTokenHamperValidation,
        SavedOauthUserValidation,
        LoginScreen,
        Oauth2Screen,
        InternalLogin,
        InternalSignUp,
        Done
    }

    public enum ViewState {
        Login,
        SignUp
    }

    private Stage stage;
    private ViewState viewState;
    private Subject<LoginEvent> publisher;

    @Inject
    AppCompatActivity activity;

    @Inject
    CoordinatorScreenManager screenManager;

    @Inject
    UserManager userManager;

    @Inject
    Listener listener;


    public LoginViewModel(String viewModelId) {
        super(viewModelId);

        stage = Stage.Idle;
        viewState = ViewState.Login;

        publisher = PublishSubject.create();
    }

    @Override
    public void onInputStateChange(LoginViewModelBuilder.Component injector) {
        injector.inject(this);

        this.userManager.setListener(userManagerListener);
    }

    @Override
    public void start() {
        if (stage == Stage.Idle) {
            stage = Stage.SavedTokenHamperValidation;
            String token = userManager.getToken();
            if (!TextUtils.isEmpty(token)) {
                // TODO(Pablo): Call the validation endpoint in HamperUSA
                stage = Stage.Done;
                listener.onLoginSuccess();
            }
            else {
                stage = Stage.SavedOauthUserValidation;
                userManager.loginPersistedOauthUser();
            }
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_LOGIN_COORDINATOR_FIREBASE_AUTH) {
            userManager.handleAuthActivityResult(resultCode, data);
            return true;
        }

        return super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        screenManager = null;
        userManager = null;
    }

    public void subscribe(Observer<LoginEvent> observer) {
        publisher.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public ViewState getViewState() {
        return viewState;
    }

    public void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }

    public void doLogin(LoginFormData loginForm) {
        stage = Stage.InternalLogin;
        userManager.loginInternal(loginForm);
        publisher.onNext(new LoginEvent.InternalLoginStart());
    }

    public void doSignUp(SignUpFormData signUpFormData) {
        stage = Stage.InternalSignUp;
        userManager.signUpInternal(signUpFormData);
        publisher.onNext(new LoginEvent.InternalSignUpStart());
    }

    public void doOauth2() {
        stage = Stage.Oauth2Screen;
        userManager.launchAuthActivity(activity);
    }

    private void showLoginScreen() {
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        loginFragment.setCoordinatorId(getId());

        screenManager.setView(loginFragment, Constants.LOGIN_FRAGMENT_TAG);
    }

    private UserManager.Listener userManagerListener = new UserManager.Listener() {
        @Override
        public void internalSignUpFail() {
            stage = Stage.Idle;
            LoginEvent.InternalSignUpFail signUpFailEvent = new LoginEvent.InternalSignUpFail();
            signUpFailEvent.setPayload("SignUp Failed, make sure that all the fields are correct.");
            publisher.onNext(signUpFailEvent);

            listener.onSignupFailed();
        }

        @Override
        public void internalSignUpSuccess() {
            stage = Stage.Idle;

            LoginEvent loginEvent = new LoginEvent.InternalSignUpSuccess();
            publisher.onNext(loginEvent);

            listener.onSignupSuccess();
        }

        @Override
        public void internalLoginFail() {
            stage = Stage.Idle;

            LoginEvent loginEvent = new LoginEvent.InternalLoginFail();
            publisher.onNext(loginEvent);

            listener.onLoginFailed();
        }

        @Override
        public void internalLoginSuccess() {
            stage = Stage.Idle;
            LoginEvent loginEvent = new LoginEvent.InternalLoginSuccess();
            publisher.onNext(loginEvent);
            listener.onLoginSuccess();
        }

        @Override
        public void oauthProviderPlatformLoginFail() {
            if (stage == Stage.SavedOauthUserValidation) {
                stage = Stage.LoginScreen;
                showLoginScreen();
            }
            else if (stage == Stage.Oauth2Screen) {
                stage = Stage.Idle;
                LoginEvent.InternalLoginFail loginEvent = new LoginEvent.InternalLoginFail();
                loginEvent.setPayload("Login Failed, Your provider did not authorize your credentials.");
                publisher.onNext(loginEvent);
            }

        }

        @Override
        public void oauthHamperPlatformLoginStarted() {
            publisher.onNext(new LoginEvent.InternalLoginStart());
        }

        @Override
        public void oauthHamperPlatformLoginSuccess() {
            stage = Stage.Done;
            publisher.onNext(new LoginEvent.InternalLoginSuccess());
            listener.onLoginSuccess();
        }

        @Override
        public void oauthHamperPlatformLoginFail(Throwable t) {
            if (stage == Stage.SavedOauthUserValidation) {
                stage = Stage.LoginScreen;
                showLoginScreen();
            }
            else {
                stage = Stage.Done;
                LoginEvent.InternalLoginFail loginEvent = new LoginEvent.InternalLoginFail();
                loginEvent.setPayload("Login Failed, Provider data could not be processed.");
                publisher.onNext(loginEvent);
            }

            listener.onLoginFailed();
        }

        @Override
        public void oauthProviderPlatformVerifyEmailSent() {
            Toast.makeText(activity, "Email Verification Needed", Toast.LENGTH_SHORT).show();
        }
    };


    public interface Listener {
        void onLoginSuccess();
        void onLoginFailed();
        void onSignupSuccess();
        void onSignupFailed();
    }

}
