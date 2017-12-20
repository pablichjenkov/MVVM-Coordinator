package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.intervalintl.voltus.root.FragmentComponent;


public class BaseFragment<VM extends BaseViewModel> extends FragmentComponent {

    private static final String KEY_VIEW_MODEL_ID = "key_view_model_id";
    private static final String KEY_VIEW_MODEL_CLASS = "key_view_model_class";

    // TODO: Find a way to force Subclasses of BaseFragment to initialize this fields before committing a FragmentTransaction
    public String viewModelId;
    public Class<VM> viewModelClass;
    public Listener<VM> listener;
    private VM viewModelInstance;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            viewModelId = savedInstanceState.getString(KEY_VIEW_MODEL_ID);
            viewModelClass = (Class<VM>) savedInstanceState.getSerializable(KEY_VIEW_MODEL_CLASS);
        }

        viewModelInstance = ViewModelProviders.of(this).get(viewModelId, viewModelClass);
        viewModelInstance.setId(viewModelId);
        // pass the viewmodel to the parent interactor so it gets injected
        if (listener != null) {
            listener.onViewModelReady(viewModelInstance);
        }
        viewModelInstance.onFragmentCreated();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_VIEW_MODEL_ID, viewModelId);
        outState.putSerializable(KEY_VIEW_MODEL_CLASS, viewModelClass);
        super.onSaveInstanceState(outState);
    }

    public void setListener(Listener<VM> listener) {
        this.listener = listener;
    }

    protected VM getViewModel() {
        return viewModelInstance;
    }


    public interface Listener<VM extends BaseViewModel> {
        void onViewModelReady(VM viewModel);
    }

}
