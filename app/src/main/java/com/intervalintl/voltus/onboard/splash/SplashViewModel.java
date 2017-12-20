package com.intervalintl.voltus.onboard.splash;

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
    public void onFirstViewLayout() {
        super.onFirstViewLayout();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep( 3000);
                    listener.onSplashFinished();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }

    @Override
    public void onViewDetach() {
        super.onViewDetach();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public interface Listener {
        void onSplashFinished();
    }

}
