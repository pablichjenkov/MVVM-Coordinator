package com.intervalintl.voltus.viewmodel;

import android.support.annotation.CallSuper;
import com.intervalintl.voltus.root.BackPressHandler;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Don't keep references of Views in this class, this class persist Configuration Changes.
 * */
public abstract class Coordinator implements BackPressHandler, ViewTreeAttachBinder.Callback {

    private String tagId;
    private final List<Coordinator> children = new CopyOnWriteArrayList<>();
    private Coordinator curCoordinator;
    private boolean viewAttached;
    private boolean hasBeenLayout;


    public Coordinator(String tagId) {
        this.tagId = tagId;
    }

    public abstract void act();
    public abstract void shut();
    public abstract <VM extends BaseViewModel> VM provideViewModel(BaseFragment baseFragment);

    public String getTagId() {
        return tagId;
    }

    @CallSuper
    @Override
    public void onViewAttach() {
        viewAttached = true;
    }

    @CallSuper
    @Override
    public void onFirstLayout() {
        hasBeenLayout = true;
    }

    @CallSuper
    @Override
    public void onViewDetach() {
        viewAttached = false;
        hasBeenLayout = false;
    }

    @Override
    public boolean isViewAttached() {
        return viewAttached;
    }

}
