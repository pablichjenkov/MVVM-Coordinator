package com.intervalintl.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.interval.common.login.LoginEvent;
import com.interval.common.login.LoginFormData;
import com.interval.common.login.SignUpFormData;
import com.intervalintl.coordinator.view.CoordinatorFragment;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class LoginFragment extends CoordinatorFragment<LoginViewModel> {

    private ViewGroup rootView;
    private Button actionBtn;
    private Button toggleBtn;
    private Button oauthBtn;
    private TextView loginErrorInfoTxt;
    private EditText nameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText passwordInput;
    private EditText zipcodeInput;
    private ProgressBar loginProgressBar;
    private LoginViewModel loginViewModel;
    private CompositeDisposable disposables;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);
        loginErrorInfoTxt = (TextView) rootView.findViewById(R.id.login_error_txt);
        nameInput = (EditText) rootView.findViewById(R.id.login_name_edt);
        emailInput = (EditText) rootView.findViewById(R.id.login_email_edt);
        phoneInput = (EditText) rootView.findViewById(R.id.login_phone_edt);
        passwordInput = (EditText) rootView.findViewById(R.id.login_pass_edt);
        zipcodeInput = (EditText) rootView.findViewById(R.id.login_zipcode_edt);
        actionBtn = (Button) rootView.findViewById(R.id.login_action_btn);
        toggleBtn = (Button) rootView.findViewById(R.id.login_toggle_btn);
        oauthBtn = (Button) rootView.findViewById(R.id.oauth_btn);
        loginProgressBar = (ProgressBar) rootView.findViewById(R.id.login_progress_bar);
        return rootView;
    }

    @Override
    protected void onCoordinatorBound(LoginViewModel viewModel) {
        Log.d("LoginFragment","Pablo: LoginFragment - binding to LoginViewModel, state->" + viewModel.getViewState().name());
        loginViewModel = viewModel;
        updateView();

        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginViewModel.getViewState() == LoginViewModel.ViewState.Login) {
                    loginViewModel.doLogin(createLoginForm());
                }
                else if (loginViewModel.getViewState() == LoginViewModel.ViewState.SignUp) {
                    loginViewModel.doSignUp(createSignUpForm());
                }
            }
        });

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginViewModel.getViewState() == LoginViewModel.ViewState.Login) {
                    loginViewModel.setViewState(LoginViewModel.ViewState.SignUp);
                }
                else if (loginViewModel.getViewState() == LoginViewModel.ViewState.SignUp) {
                    loginViewModel.setViewState(LoginViewModel.ViewState.Login);
                }
                updateView();
            }
        });

        oauthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.doOauth2();
            }
        });

        // Subscribe to events
        loginViewModel.subscribe(loginObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }

    private void updateView() {
        if (loginViewModel.getViewState() == LoginViewModel.ViewState.SignUp) {
            nameInput.setVisibility(View.VISIBLE);
            phoneInput.setVisibility(View.VISIBLE);
            zipcodeInput.setVisibility(View.VISIBLE);
            actionBtn.setText("Register");
            toggleBtn.setText("Login");
        }
        else if (loginViewModel.getViewState() == LoginViewModel.ViewState.Login) {
            nameInput.setVisibility(View.GONE);
            phoneInput.setVisibility(View.GONE);
            zipcodeInput.setVisibility(View.GONE);
            actionBtn.setText("Login");
            toggleBtn.setText("Register");
        }
    }

    private LoginFormData createLoginForm() {
        LoginFormData loginFormData = new LoginFormData();
        loginFormData.email = emailInput.getText().toString().trim();
        loginFormData.password = passwordInput.getText().toString().trim();

        return loginFormData;
    }

    private SignUpFormData createSignUpForm() {
        SignUpFormData signUpFormData = new SignUpFormData();
        signUpFormData.name = nameInput.getText().toString().trim();
        signUpFormData.email = emailInput.getText().toString().trim();
        signUpFormData.phone = phoneInput.getText().toString().trim();
        signUpFormData.password = passwordInput.getText().toString().trim();
        signUpFormData.zipcode = zipcodeInput.getText().toString().trim();

        return signUpFormData;
    }

    private void showErrorMessage(String errorText) {
        loginErrorInfoTxt.setVisibility(View.VISIBLE);
        loginErrorInfoTxt.setText(errorText);
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginErrorInfoTxt.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private Observer<LoginEvent> loginObserver = new Observer<LoginEvent>() {

        @Override
        public void onSubscribe(Disposable d) {
            disposables.add(d);
        }

        @Override
        public void onNext(LoginEvent loginEvent) {

            if (loginEvent instanceof LoginEvent.InternalLoginStart) {
                loginProgressBar.setVisibility(View.VISIBLE);

            } else if (loginEvent instanceof LoginEvent.InternalLoginFail) {
                loginProgressBar.setVisibility(View.GONE);
                showErrorMessage(((LoginEvent.InternalLoginFail) loginEvent).getPayload());

            } else if (loginEvent instanceof LoginEvent.InternalLoginSuccess) {
                loginProgressBar.setVisibility(View.GONE);

            } else if (loginEvent instanceof LoginEvent.InternalSignUpStart) {
                loginProgressBar.setVisibility(View.VISIBLE);

            } else if (loginEvent instanceof LoginEvent.InternalSignUpFail) {
                loginProgressBar.setVisibility(View.GONE);
                showErrorMessage(((LoginEvent.InternalSignUpFail) loginEvent).getPayload());

            } else if (loginEvent instanceof LoginEvent.InternalSignUpSuccess) {
                loginProgressBar.setVisibility(View.GONE);
            }

        }

        @Override
        public void onError(Throwable e) {
            // The stream broke, try to recover it.
        }

        @Override
        public void onComplete() {

        }
    };

}
