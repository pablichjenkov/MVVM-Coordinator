package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;
import android.view.View;
import java.lang.ref.WeakReference;


public class BaseViewModel<V extends View> extends ViewModel {

    private boolean attached;
    private WeakReference<V> viewRef;

    @CallSuper
    public void attach(V view) {
        viewRef = new WeakReference<>(view);
        attached = true;
    }

    @CallSuper
    public void detach(V view) {
        viewRef.clear();
        viewRef = null;
        attached = false;
    }

    public void dispatchViewFirstLayout() {}

    protected V getView() {
        return viewRef != null ? viewRef.get() : null;
    }

    public final boolean isAttached() {
        return attached;
    }
}