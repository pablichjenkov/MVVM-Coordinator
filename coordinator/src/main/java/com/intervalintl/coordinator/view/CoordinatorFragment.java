package com.intervalintl.coordinator.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.delegate.CoordinatorFragmentBinder;


public abstract class CoordinatorFragment<VM extends Coordinator> extends Fragment implements CoordinatedView {


    private CoordinatorFragmentBinder.Callback<VM> fragmentBinderCB = new CoordinatorFragmentBinder.Callback<VM>() {
        @Override
        public void onViewModelBound(VM viewModelInstance) {
            CoordinatorFragment.this.onCoordinatorBound(viewModelInstance);
        }
    };

    private CoordinatorFragmentBinder<VM> coordinatorFragmentBinder
            = new CoordinatorFragmentBinder<>(this, fragmentBinderCB);;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordinatorFragmentBinder.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        coordinatorFragmentBinder.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        coordinatorFragmentBinder.onSaveInstanceState(outState);
    }

    @Override
    public void setCoordinatorId(String id) {
        coordinatorFragmentBinder.setCoordinatorId(id);
    }

    protected abstract void onCoordinatorBound(VM coordinatorViewModel);

}
