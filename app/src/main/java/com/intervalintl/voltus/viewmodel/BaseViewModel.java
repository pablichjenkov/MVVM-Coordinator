package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;


public class BaseViewModel extends ViewModel {

    private String id;
    private boolean fragmentAttached;
    private boolean viewAttached;
    private boolean hasBeenLayout;


    @CallSuper
    public void onFragmentCreated() {
        fragmentAttached = true;
    }

    @CallSuper
    public void onViewAttach() {
        viewAttached = true;
    }

    @CallSuper
    public void onViewDetach() {
        viewAttached = false;
        hasBeenLayout = false;
    }

    @CallSuper
    public void onFirstViewLayout() {
        hasBeenLayout = true;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isFragmentAttached() {
        return fragmentAttached;
    }

    public final boolean isViewAttached() {
        return viewAttached;
    }

    public boolean hasBeenLayout() {
        return hasBeenLayout;
    }
}