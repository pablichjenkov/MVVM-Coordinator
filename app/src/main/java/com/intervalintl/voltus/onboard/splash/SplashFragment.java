package com.intervalintl.voltus.onboard.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.ViewTreeModelViewBinder;


public class SplashFragment extends BaseFragment<SplashViewModel> {

    private ViewGroup rootView;
    private ViewTreeModelViewBinder<ViewGroup, SplashViewModel> viewTreeBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_splash, container, false);
        viewTreeBinder = new ViewTreeModelViewBinder<>(rootView, getViewModel());
        return rootView;
    }


}
