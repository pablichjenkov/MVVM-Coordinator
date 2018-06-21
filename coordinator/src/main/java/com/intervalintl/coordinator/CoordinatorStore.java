package com.intervalintl.coordinator;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;


public class CoordinatorStore {

    private static final String COORDINATOR_STORE_VIEW_MODEL_ID = "coordinatorStoreViewModel";
    private RootCoordinatorViewModel coordinatorViewModel;


    /* package */ CoordinatorStore(FragmentActivity activity) {

        coordinatorViewModel = ViewModelProviders.of(activity)
                .get(COORDINATOR_STORE_VIEW_MODEL_ID, RootCoordinatorViewModel.class);


    }

    public void setRootCoordinator(Coordinator rootCoordinator) {
        coordinatorViewModel.setRootCoordinator(rootCoordinator);
    }

    public Coordinator getRootCoordinator() {
        return coordinatorViewModel.getRootCoordinator();
    }

    /**
     * This class has to be public and static so can be instantiated using Java Reflection.
     * It will contain the RootCoordinator instance of the given FragmentActivity.
     * */
    public static class RootCoordinatorViewModel extends ViewModel {

        Coordinator rootCoordinator;

        public void setRootCoordinator(Coordinator rootCoordinator) {
            this.rootCoordinator = rootCoordinator;
        }

        public Coordinator getRootCoordinator() {
            return rootCoordinator;
        }

        @Override
        protected void onCleared() {
            rootCoordinator.onCleared();
            rootCoordinator = null;
        }

    }

}
