package com.intervalintl.voltus;


public interface ScreenManager {
    void setScreen();
    void registerCallback(Callback callback);

    interface Callback {
        void onBack();
    }
}
