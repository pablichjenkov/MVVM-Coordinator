package com.intervalintl.voltus;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {


    private LinkHandlerImpl mLinkHandlerDelegate;


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLinkHandlerDelegate = new LinkHandlerImpl(BaseActivity.this
                , savedInstanceState
                , getSupportFragmentManager()
                , getFragmentContainerId());
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outBundle) {
        mLinkHandlerDelegate.onSaveInstanceState(outBundle);
        super.onSaveInstanceState(outBundle);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        mLinkHandlerDelegate.onDestroy();
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void onBackPressed() {
        if (!mLinkHandlerDelegate.handleBackPress()) {
            super.onBackPressed();
        }
    }

    protected LinkHandler getLinkHandler() {
        return mLinkHandlerDelegate;
    }

    public abstract @IdRes int getFragmentContainerId();

}
