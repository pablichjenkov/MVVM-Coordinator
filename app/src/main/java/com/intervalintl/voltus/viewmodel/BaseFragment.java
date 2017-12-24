package com.intervalintl.voltus.viewmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.intervalintl.voltus.root.FragmentComponent;

// TODO(Pablo): Implement LifeCycle Interface to be notified when the Fragment completely destroys and nullify its ViewModel
public abstract class BaseFragment<VM extends BaseViewModel> extends FragmentComponent {

    private static final String KEY_INTERACTOR_ID = "key_interactor_id";
    private static final String KEY_VIEW_MODEL_ID = "key_view_model_id";
    private static final String KEY_VIEW_MODEL_CLASS = "key_view_model_class";

    public String interactorId;
    public String viewModelId;
    public Class<VM> viewModelClass;
    private VM viewModelInstance;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            interactorId = savedInstanceState.getString(KEY_INTERACTOR_ID);
            viewModelId = savedInstanceState.getString(KEY_VIEW_MODEL_ID);
            viewModelClass = (Class<VM>) savedInstanceState.getSerializable(KEY_VIEW_MODEL_CLASS);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentActivity activity = getActivity();
        if ((activity instanceof BaseActivity) == false) {
            throw new RuntimeException("Fragments that subclass BaseFragment must be attached to " +
                    " a BaseActivity instance");
        }

        BaseActivity baseActivity = (BaseActivity)activity;
        CoordinatorStore coordinatorStore = baseActivity.getCoordinatorStore();
        Coordinator coordinator = coordinatorStore.get(interactorId);

        if (coordinator == null) {
            throw new RuntimeException("Coordinator not found in CoordinatorStore" +
                    ". You missed to assign an InteractorId to this Fragment or to save the " +
                    "Coordinator in the same Activity store where this Fragment belongs to");
        }

        // viewModelInstance should be injected here by its Coordinator.
        viewModelInstance = coordinator.provideViewModel(this);
        onViewModelBound(viewModelInstance);
    }

    protected abstract void onViewModelBound(VM viewModel);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_INTERACTOR_ID, interactorId);
        outState.putString(KEY_VIEW_MODEL_ID, viewModelId);
        outState.putSerializable(KEY_VIEW_MODEL_CLASS, viewModelClass);
        super.onSaveInstanceState(outState);
    }

}
