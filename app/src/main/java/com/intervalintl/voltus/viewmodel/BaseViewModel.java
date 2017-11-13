package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;
import android.view.View;
import java.lang.ref.WeakReference;


public class BaseViewModel<V extends View> extends ViewModel {

    private boolean attached;
    private WeakReference<V> mvpViewRef;

    @CallSuper
    public void attach(V view) {
        mvpViewRef = new WeakReference<>(view);
        attached = true;
    }

    @CallSuper
    public void detach(V view) {
        mvpViewRef.clear();
        mvpViewRef = null;
        attached = false;
    }

    public void dispatchViewFirstLayout() {}

    protected V getView() {
        return mvpViewRef != null ? mvpViewRef.get() : null;
    }

    public final boolean isAttached() {
        return attached;
    }
}