package com.intervalintl.coordinator.view.delegate;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.intervalintl.coordinator.Coordinator;


public class SampleViewGroup<VM extends Coordinator> extends ViewGroup {

    private CoordinatorViewGroupBinder<VM> coordinatorBinder;


    public SampleViewGroup(Context context) {
        super(context);
        init();
    }

    public SampleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SampleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        coordinatorBinder = new CoordinatorViewGroupBinder<>(this, viewGroupBinderCB);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {}

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof CoordinatorViewState) {
            CoordinatorViewState viewState = ((CoordinatorViewState) state);
            coordinatorBinder.onRestoreInstanceState(viewState);
            super.onRestoreInstanceState(viewState.getSuperState());
        }
        else {
            super.onRestoreInstanceState(state);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superParcelable = super.onSaveInstanceState();
        CoordinatorViewState viewState = new CoordinatorViewState(superParcelable);
        coordinatorBinder.onSaveInstanceState(viewState);

        return viewState;
    }


    private CoordinatorViewGroupBinder.Callback<VM> viewGroupBinderCB = new CoordinatorViewGroupBinder.Callback<VM>() {
        @Override
        public void onViewModelBound(VM viewModelInstance) {
            // TODO(Pablo): Can define an abstract method and call it from here.
        }
    };

}
