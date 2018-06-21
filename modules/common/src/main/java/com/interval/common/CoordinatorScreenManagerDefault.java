package com.interval.common;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.coordinator.view.CoordinatedView;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;


public class CoordinatorScreenManagerDefault implements CoordinatorScreenManager {

    private final FragmentManager fragmentManager;
    private final ViewGroup viewContainer;

    @IdRes
    private final int viewContainerId;


    public CoordinatorScreenManagerDefault(FragmentManager fragmentManager
        , ViewGroup viewContainer) {

        this.fragmentManager = fragmentManager;
        this.viewContainer = viewContainer;
        this.viewContainerId = viewContainer.getId();
    }

    @Override
    public <F extends Fragment & CoordinatedView> void setView(F fragment, String fragmentId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewContainerId, fragment, fragmentId);
        fragmentTransaction.commitNow();
    }

    @Override
    public <V extends View & CoordinatedView> void setView(V view) {
        viewContainer.removeAllViews();
        viewContainer.addView(view);
    }

}
