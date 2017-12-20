package com.intervalintl.voltus.onboard.login;

import com.intervalintl.voltus.viewmodel.BaseViewModel;


public class LoginViewModel extends BaseViewModel {

    // TODO: Use PublishSubject from RxJava
    private Listener listener;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void loginSuccess() {
        listener.onLoginSuccess();
    }


    public interface Listener {
        void onLoginSuccess();
    }

}
