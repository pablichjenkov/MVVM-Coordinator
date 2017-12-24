package com.intervalintl.voltus.onboard.splash;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.BaseFragment;


public class SplashFragment extends BaseFragment<SplashViewModel> {

    private ViewGroup rootView;
    private SplashViewModel splashViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }

    @Override
    protected void onViewModelBound(SplashViewModel viewModel) {
        Log.d("Pablo", "SplashFragment - SplashViewModel bound");
        splashViewModel = viewModel;
        splashViewModel.startSplash();
    }

}
