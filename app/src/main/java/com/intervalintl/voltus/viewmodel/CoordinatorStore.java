package com.intervalintl.voltus.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import java.util.HashMap;


public class CoordinatorStore {

    private static final String INTERACTOR_STORE_VIEW_MODEL_ID = "interactorStoreViewModel";
    private CoordinatorStoreViewModel interactorStoreViewModel;


    /* package */ CoordinatorStore(FragmentActivity activity) {
        ViewModelProvider vmp = ViewModelProviders.of(activity);
        interactorStoreViewModel = vmp.get(INTERACTOR_STORE_VIEW_MODEL_ID, CoordinatorStoreViewModel.class);
    }

    public void put(String key, Coordinator coordinator) {
        interactorStoreViewModel.put(key, coordinator);
    }

    public <T extends Coordinator> T get(String key) {
        return interactorStoreViewModel.get(key);
    }

    /**
     * This class has to be public and static so can be instantiated using Java Reflection.
     * It will contain all the Coordinator instances of the given FragmentActivity.
     * */
    public static class CoordinatorStoreViewModel extends ViewModel {

        private final HashMap<String, Coordinator> mMap = new HashMap<>();


        public CoordinatorStoreViewModel() {}

        final void put(String key, Coordinator coordinator) {
            Coordinator oldCoordinator = mMap.get(key);
            if (oldCoordinator != null) {
                oldCoordinator.shut();
            }
            mMap.put(key, coordinator);
        }

        // Cast is unchecked so make sure the object you are requesting is the type you want
        final <T extends Coordinator> T get(String key) {
            return (T)mMap.get(key);
        }

        @Override
        protected void onCleared() {
            for (Coordinator coordinator : mMap.values()) {
                coordinator.shut();
            }
            mMap.clear();
        }

    }

}
