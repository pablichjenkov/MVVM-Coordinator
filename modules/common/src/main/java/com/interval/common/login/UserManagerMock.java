package com.interval.common.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.interval.common.Constants;
import java.util.concurrent.TimeUnit;


public class UserManagerMock implements UserManager {

    protected static final String AUTHENTICATION_SCHEME = "Bearer ";
    protected static final String HEADER_AUTHORIZATION = "Authorization";

    private Listener listener;
    private String authToken;

    public UserManagerMock() {}

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }


    // region: Hamper Internal Auth

    @Override
    public String getToken() {
        return authToken;
    }

    @Override
    public void loginInternal(LoginFormData loginFormData) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    listener.internalLoginSuccess();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    listener.internalLoginFail();
                }
            }
        }).start();
    }

    @Override
    public void signUpInternal(SignUpFormData formData) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    listener.internalSignUpSuccess();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    listener.internalSignUpFail();
                }
            }
        }).start();
    }

    // endregion


    // region: Oauth2 Auth

    @Override
    public void loginPersistedOauthUser() {
        listener.oauthProviderPlatformLoginFail();
    }

    @Override
    public void launchAuthActivity(Activity activity) {
        Intent intent = new Intent(activity, FirebaseMockActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN_COORDINATOR_FIREBASE_AUTH);
    }

    @Override
    public void handleAuthActivityResult(int resultCode, Intent data) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("UserManagerMock", "Should Handle Firebase Activity result and " +
                            "send the resulted data to our internal server. Upon success, fire the " +
                            "respective events");

                    listener.oauthHamperPlatformLoginStarted();
                    TimeUnit.SECONDS.sleep(2);
                    listener.oauthHamperPlatformLoginSuccess();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    listener.oauthHamperPlatformLoginFail(e);
                }
            }
        }).start();

    }

    // endregion

}
