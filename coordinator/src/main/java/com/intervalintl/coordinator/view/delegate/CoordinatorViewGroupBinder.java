package com.intervalintl.coordinator.view.delegate;

import android.content.Context;
import android.view.ViewGroup;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.CoordinatorActivity;
import com.intervalintl.coordinator.view.CoordinatedView;


public class CoordinatorViewGroupBinder<VM extends Coordinator> implements CoordinatedView {

    private String coordinatorId;
    private ViewGroup viewGroup;
    private Callback<VM> callback;


    public CoordinatorViewGroupBinder(ViewGroup viewGroup, Callback<VM> callback) {
        this.viewGroup = viewGroup;
        this.callback = callback;
    }

    public void onRestoreInstanceState(CoordinatorViewState viewState) {
        coordinatorId = viewState.coordinatorId;
    }

    public void onResume() {

        Context activity = viewGroup.getContext();
        if (!(activity instanceof CoordinatorActivity)) {
            throw new RuntimeException("Fragments that subclass BaseFragment must be attached to " +
                    " a CoordinatorActivity instance");
        }

        CoordinatorActivity coordinatorActivity = (CoordinatorActivity)activity;
        Coordinator coordinator = coordinatorActivity.findCoordinatorById(coordinatorId);

        if (coordinator == null) {
            throw new RuntimeException("Coordinator: " + coordinatorId + " not found in " +
                    "CoordinatorStore. You missed to assign an InteractorId to this Fragment or to " +
                    "save the Coordinator in the same Activity store where this Fragment belongs to");
        }

        callback.onViewModelBound((VM)coordinator);
    }

    public void onSaveInstanceState(CoordinatorViewState viewState) {
        viewState.coordinatorId = coordinatorId;
    }

    @Override
    public void setCoordinatorId(String coordinatorId) {
        this.coordinatorId = coordinatorId;
    }


    public interface Callback<U> {
        void onViewModelBound(U viewModelInstance);
    }

}
