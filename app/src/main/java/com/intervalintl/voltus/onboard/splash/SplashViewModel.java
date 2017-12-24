package com.intervalintl.voltus.onboard.splash;

import android.util.Log;

import com.intervalintl.voltus.viewmodel.BaseViewModel;


public class SplashViewModel extends BaseViewModel {

    // TODO: Use PublishSubject from RxJava
    private Listener listener;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onFragmentCreated() {
        super.onFragmentCreated();
    }

    @Override
    public void onViewAttach() {
        super.onViewAttach();
    }

    @Override
    public void onFirstLayout() {
        super.onFirstLayout();
    }

    @Override
    public void onViewDetach() {
        super.onViewDetach();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    // region: Custom methods

    boolean timing;
    public void startSplash() {
        if (!timing) {
            timing = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Thread.sleep( 5000);

                        Log.d("Pablo", "Dispatching splash timeout");
                        listener.onSplashFinished();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        }

    }


    public interface Listener {
        void onSplashFinished();
    }

}
