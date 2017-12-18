package com.intervalintl.voltus.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.root.FragmentComponent;


public class SplashFragment extends FragmentComponent {

    private SplashViewModel splashViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

}
