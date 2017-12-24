package com.intervalintl.voltus.root;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public abstract class RouterActivity extends AppCompatActivity {


    private RouterImpl mRouterDelegate;


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRouterDelegate = new RouterImpl(RouterActivity.this
                , savedInstanceState
                , getSupportFragmentManager()
                , getFragmentContainerId());
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outBundle) {
        mRouterDelegate.onSaveInstanceState(outBundle);
        super.onSaveInstanceState(outBundle);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        mRouterDelegate.onDestroy();
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void onBackPressed() {
        if (!mRouterDelegate.handleBackPress()) {
            super.onBackPressed();
        }
    }

    protected void setBackPressHandler(BackPressHandler backPressHandler) {
        mRouterDelegate.setBackPressHandler(backPressHandler);
    }

    protected Router getRouter() {
        return mRouterDelegate;
    }

    protected abstract @IdRes int getFragmentContainerId();

}
