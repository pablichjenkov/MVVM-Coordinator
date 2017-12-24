package com.intervalintl.voltus.viewmodel;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import java.security.InvalidParameterException;

/**
 * Attach this View.OnAttachStateChangeListener event listener to any view in the hierarchy.
 */
public final class ViewTreeAttachBinder<V extends View, L extends ViewTreeAttachBinder.Callback>
        implements View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {


    private final L listener;
    private final V view;


    public ViewTreeAttachBinder(V view, L listener) {

        if (listener == null || view == null) {
            throw new InvalidParameterException("ViewTreeAttachBinder does not accept null parameters.");
        }

        this.listener = listener;
        this.view = view;

        view.addOnAttachStateChangeListener(ViewTreeAttachBinder.this);
        // Sometimes we missed the first attach because the child's already been added.
        // Sometimes we didn't. The binding keeps track to avoid double attachment of the ViewModel,
        // and to guard against attachment to two different views simultaneously.
        if (isAttachedToWindow(view)) {
            this.onViewAttachedToWindow(view);
        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeAttachBinder.this);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull View v) {
        if (listener.isViewAttached()) {
            throw new IllegalStateException("This listener has a view already attached");
        }

        listener.onViewAttach();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull View v) {
        listener.onViewDetach();
    }

    @Override
    public void onGlobalLayout() {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(ViewTreeAttachBinder.this);
        listener.onFirstLayout();
    }

    private static boolean isAttachedToWindow(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return view.getWindowToken() != null;
        } else {
            return view.isAttachedToWindow();
        }
    }


    public interface Callback {
        boolean isViewAttached();
        void onViewAttach();
        void onFirstLayout();
        void onViewDetach();
    }

}
