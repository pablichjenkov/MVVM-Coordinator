package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.util.ArrayMap;

import com.intervalintl.voltus.root.BackPressHandler;
import com.intervalintl.voltus.root.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Don't keep references of Views in this class, this class persist Configuration Changes.
 * */
public abstract class Coordinator implements BackPressHandler, ViewTreeAttachBinder.Callback
        , LifecycleObserver {

    private String tagId;
    protected Router router;
    protected Handler mainHandler;
    private Map<String, Coordinator> children;
    private Coordinator curCoordinator;
    private boolean viewAttached;
    private boolean hasBeenLayout;


    public Coordinator(String tagId) {
        this.tagId = tagId;
        this.children = new ArrayMap<>();
    }

    public abstract void dispatch();
    public abstract void shut();
    public abstract <VM extends BaseViewModel> VM provideViewModel(BaseFragment baseFragment);


    protected Coordinator findChildByTagId(String tagId) {
        return children.get(tagId);
    }

    public void onCreate(Router router) {
        this.router = router;
        mainHandler = new Handler(Looper.getMainLooper());

        for (Coordinator coordinator : children.values()) {
            coordinator.onCreate(router);
        }
    }

    protected void addChild(String tagId, Coordinator coordinator) {
        children.put(tagId, coordinator);
    }


    // region: View Attach Events


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


    // endregion


    // region: Activity events


    @CallSuper
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Coordinator coordinator : children.values()) {
            coordinator.onActivityResult(requestCode, resultCode, data);
        }
    }

}
