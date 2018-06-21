package com.intervalintl.coordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class CoordinatorActivity<Input> extends AppCompatActivity {

    private Coordinator<Input> rootCoordinator;
    private boolean isFreshActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorStore coordinatorStore = new CoordinatorStore(CoordinatorActivity.this);
        rootCoordinator = coordinatorStore.getRootCoordinator();

        if (rootCoordinator == null) {
            isFreshActivity = true;
            rootCoordinator = onProvideRootCoordinator();
            coordinatorStore.setRootCoordinator(rootCoordinator);
        }

        rootCoordinator.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootCoordinator.onResume();

        // Dispatch the input injector through the whole Coordinator's Tree
        Input inputInjector = onProvideRootCoordinatorInput();
        rootCoordinator.onInputStateChange(inputInjector);

        // Start will only be called the first time the Activity is Launched.
        if (isFreshActivity) {
            rootCoordinator.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rootCoordinator.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        rootCoordinator.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        rootCoordinator.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        boolean coordinatorConsumedBack = rootCoordinator.onBackPressed();
        if (!coordinatorConsumedBack) {
            super.onBackPressed();
        }
    }


    //region: Coordinator API

    public <T extends Coordinator> T findCoordinatorById(String tagId) {
        return (T)rootCoordinator.depthFirstSearchById(tagId);
    }

    /**
     * This method is invoked the first time the Activity is Launched. It should return the
     * root Coordinator of this Activity. It wont be called when the Activity is recreated due
     * to configuration changes.
     * */
    protected abstract Coordinator<Input> onProvideRootCoordinator();

    /**
     * This method is invoked every time the Activity is created either the first time or when the
     * Activity is recreated due to configuration changes. Its the time to invoke the
     * Coordinator.onDispatchInputStateChange(InputState injector) passing the right dependency
     * injector.
     * */
    protected abstract Input onProvideRootCoordinatorInput();

    // endregion

}
