package com.intervalintl.voltus.onboard.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.firebase.ui.auth.AuthUI;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import java.util.Arrays;
import java.util.List;


public class LoginFragment extends BaseFragment<LoginViewModel> {

    private ViewGroup rootView;
    private Button loginButton;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) rootView.findViewById(R.id.login_button);

        return rootView;
    }

    @Override
    protected void onViewModelBound(LoginViewModel viewModel) {
        Log.d("Pablo", "SplashFragment - SplashViewModel bound");
        loginViewModel = viewModel;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginViewModel.loginSuccess();
                firebaseAuth();
            }
        });
    }

    private static final int RC_SIGN_IN = 123;
    public void firebaseAuth() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                //new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

}
