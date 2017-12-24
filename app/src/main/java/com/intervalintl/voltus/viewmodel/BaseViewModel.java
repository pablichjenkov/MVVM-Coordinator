package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;

/**
 * Don't keep references of Views in this class, this class persist Configuration Changes.
 * */
public class BaseViewModel extends ViewModel implements ViewTreeAttachBinder.Callback {

    private String id;
    private boolean fragmentAttached;
    private boolean viewAttached;
    private boolean hasBeenLayout;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @CallSuper
    @Override
    public void onViewAttach() {
        viewAttached = true;
    }

    @CallSuper
    @Override
    public void onViewDetach() {
        viewAttached = false;
        hasBeenLayout = false;
    }

    @CallSuper
    @Override
    public void onFirstLayout() {
        hasBeenLayout = true;
    }

    @CallSuper
    @Override
    public final boolean isViewAttached() {
        return viewAttached;
    }

    public boolean hasBeenLayout() {
        return hasBeenLayout;
    }

    @CallSuper
    public void onFragmentCreated() {
        fragmentAttached = true;
    }

    public boolean isFragmentAttached() {
        return fragmentAttached;
    }

}