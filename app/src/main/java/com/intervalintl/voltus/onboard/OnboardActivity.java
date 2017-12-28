package com.intervalintl.voltus.onboard;

import android.content.Intent;
import android.os.Bundle;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.util.Constants;
import com.intervalintl.voltus.viewmodel.BaseActivity;


public class OnboardActivity extends BaseActivity {

    private OnboardCoordinator onboardCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        setupCoordinator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onboardCoordinator.onActivityResult(requestCode, resultCode, data);
    }

    protected void setupCoordinator() {
        onboardCoordinator = getCoordinatorStore().get(Constants.COORDINATOR_ONBOARD_ID);
        if (onboardCoordinator == null) {
            onboardCoordinator = new OnboardCoordinator(Constants.COORDINATOR_ONBOARD_ID);
            getCoordinatorStore().put(Constants.COORDINATOR_ONBOARD_ID, onboardCoordinator);
        }

        setBackPressHandler(onboardCoordinator);
        onboardCoordinator.onCreate(getRouter());
        onboardCoordinator.dispatch();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.onboard_fragment_container;
    }

}
