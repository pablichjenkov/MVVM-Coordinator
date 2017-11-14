package com.intervalintl.voltus;

import android.arch.lifecycle.ViewModel;
import com.intervalintl.voltus.fragment.Fragment1;
import com.intervalintl.voltus.navigation.NavigationActor;
import java.lang.ref.WeakReference;


public class ActivityModel extends ViewModel {



    public ActivityModel() {}

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    private ScreenManager mScreenManager = new ScreenManager() {

        public Callback mCallback;

        @Override
        public void setScreen() {

        }

        @Override
        public void registerCallback(Callback callback) {
            mCallback = callback;
        }
    };

}