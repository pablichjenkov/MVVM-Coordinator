package com.intervalintl.coordinator.view.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.CoordinatorActivity;
import com.intervalintl.coordinator.view.CoordinatedView;


public class CoordinatorFragmentBinder<VM extends Coordinator> implements CoordinatedView {

    private static final String KEY_COORDINATOR_ID = "key_coordinator_id";

    private String coordinatorId;
    private Fragment fragment;
    private Callback<VM>  callback;


    public CoordinatorFragmentBinder(Fragment fragment, Callback<VM> callback) {
        this.fragment = fragment;
        this.callback = callback;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            coordinatorId = savedInstanceState.getString(KEY_COORDINATOR_ID);
        }
    }

    public void onResume() {

        FragmentActivity activity = fragment.getActivity();
        if ((activity instanceof CoordinatorActivity) == false) {
            throw new RuntimeException("Fragments that subclass BaseFragment must be attached to " +
                    " a CoordinatorActivity instance");
        }

        CoordinatorActivity coordinatorActivity = (CoordinatorActivity)activity;
        Coordinator coordinator = coordinatorActivity.findCoordinatorById(coordinatorId);

        if (coordinator == null) {
            throw new RuntimeException("Coordinator: " + coordinatorId + " not found in " +
                    "CoordinatorStore. You missed to assign the coordinatorId associated with this " +
                    "Fragment or attach the Coordinator to the Coordinator Tree that belongs to " +
                    "Activity: " + activity.getClass().getSimpleName());
        }

        callback.onViewModelBound((VM)coordinator);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_COORDINATOR_ID, coordinatorId);
    }

    @Override
    public void setCoordinatorId(String coordinatorId) {
        this.coordinatorId = coordinatorId;
    }


    public interface Callback<U> {
        void onViewModelBound(U viewModelInstance);
    }

}
