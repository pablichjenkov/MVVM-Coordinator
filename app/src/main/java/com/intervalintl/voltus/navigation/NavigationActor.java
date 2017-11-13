package com.intervalintl.voltus.navigation;

import android.arch.lifecycle.LifecycleObserver;
import com.intervalintl.voltus.ScreenManager;


public class NavigationActor implements LifecycleObserver {





    public NavigationActor() {

    }

    public void start(ScreenManager screenManager) {
        screenManager.registerCallback(mCallback);
    }


    ScreenManager.Callback mCallback = new ScreenManager.Callback() {
        @Override
        public void onBack() {

        }
    };



}
