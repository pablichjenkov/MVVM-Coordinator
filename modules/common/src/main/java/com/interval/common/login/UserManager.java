package com.interval.common.login;

import android.app.Activity;
import android.content.Intent;


public interface UserManager {

    String AUTHENTICATION_SCHEME = "Bearer ";
    String HEADER_AUTHORIZATION = "Authorization";

    void setListener(Listener listener);


    // region: Internal Auth

    String getToken();

    void loginInternal(LoginFormData loginFormData);

    void signUpInternal(SignUpFormData formData);

    // endregion


    // region: Oauth2 Auth

    void loginPersistedOauthUser();

    void launchAuthActivity(Activity activity);

    void handleAuthActivityResult(int resultCode, Intent data);

    // endregion


    interface Listener {
        // Internal Events
        void internalSignUpFail();
        void internalSignUpSuccess();
        void internalLoginFail();
        void internalLoginSuccess();

        // Oauth Events
        void oauthProviderPlatformLoginFail();
        void oauthProviderPlatformVerifyEmailSent();
        void oauthHamperPlatformLoginStarted();
        void oauthHamperPlatformLoginFail(Throwable t);
        void oauthHamperPlatformLoginSuccess();
    }

}
