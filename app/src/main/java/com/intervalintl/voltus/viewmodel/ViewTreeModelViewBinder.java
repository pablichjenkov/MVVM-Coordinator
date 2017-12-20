package com.intervalintl.voltus.viewmodel;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import java.security.InvalidParameterException;

/**
 * Attach this View.OnAttachStateChangeListener event listener to any view in the hierarchy.
 */
public final class ViewTreeModelViewBinder<V extends View, VM extends BaseViewModel>
        implements View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {


    private final VM viewModel;
    private final V view;


    public ViewTreeModelViewBinder(V view, VM viewModel) {

        if (viewModel == null || view == null) {
            throw new InvalidParameterException("ViewTreeModelViewBinder does not accept null parameters.");
        }

        this.viewModel = viewModel;
        this.view = view;

        view.addOnAttachStateChangeListener(ViewTreeModelViewBinder.this);
        // Sometimes we missed the first attach because the child's already been added.
        // Sometimes we didn't. The binding keeps track to avoid double attachment of the ViewModel,
        // and to guard against attachment to two different views simultaneously.
        if (isAttachedToWindow(view)) {
            this.onViewAttachedToWindow(view);
        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeModelViewBinder.this);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull View v) {
        if (viewModel.isViewAttached()) {
            throw new IllegalStateException(
                    "BaseViewModel " + viewModel + " is already attached");
        }

        viewModel.onViewAttach();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull View v) {
        viewModel.onViewDetach();
    }

    @Override
    public void onGlobalLayout() {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(ViewTreeModelViewBinder.this);
        viewModel.onFirstViewLayout();
    }

    private static boolean isAttachedToWindow(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return view.getWindowToken() != null;
        } else {
            return view.isAttachedToWindow();
        }
    }

}
