package com.intervalintl.onboard.splash;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.coordinator.view.CoordinatorFragment;
import com.intervalintl.onboard.R;


public class SplashFragment extends CoordinatorFragment<SplashViewModel> {

    private ViewGroup rootView;
    private SplashViewModel splashViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }

    @Override
    protected void onCoordinatorBound(SplashViewModel coordinatorViewModel) {
        Log.d("SplashFragment","SplashFragment - binding to SplashViewModel");
        splashViewModel = coordinatorViewModel;
        updateView();
    }

    private void updateView() {
        if (splashViewModel.getStage() == SplashViewModel.Stage.Idle) {
            splashViewModel.startSplashTimeout();
        }
    }

}
