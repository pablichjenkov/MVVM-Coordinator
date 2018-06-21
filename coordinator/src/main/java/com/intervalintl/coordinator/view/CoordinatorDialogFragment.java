package com.intervalintl.coordinator.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.delegate.CoordinatorFragmentBinder;


public abstract class CoordinatorDialogFragment<VM extends Coordinator> extends DialogFragment implements CoordinatedView {

    private CoordinatorFragmentBinder<VM> coordinatorFragmentBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordinatorFragmentBinder = new CoordinatorFragmentBinder<>(this, fragmentBinderCB);
        coordinatorFragmentBinder.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        coordinatorFragmentBinder.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        coordinatorFragmentBinder.onSaveInstanceState(outState);
    }

    @Override
    public void setCoordinatorId(String id) {
        coordinatorFragmentBinder.setCoordinatorId(id);
    }


    private CoordinatorFragmentBinder.Callback<VM> fragmentBinderCB = new CoordinatorFragmentBinder.Callback<VM>() {
        @Override
        public void onViewModelBound(VM viewModelInstance) {
            CoordinatorDialogFragment.this.onCoordinatorBound(viewModelInstance);
        }
    };

    protected abstract void onCoordinatorBound(VM coordinatorViewModel);

}
