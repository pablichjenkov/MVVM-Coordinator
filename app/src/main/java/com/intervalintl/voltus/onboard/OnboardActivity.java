package com.intervalintl.voltus.onboard;

import android.os.Bundle;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.util.CoordinatorUtil;
import com.intervalintl.voltus.viewmodel.BaseActivity;


public class OnboardActivity extends BaseActivity {

    private OnboardCoordinator onboardCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        setupCoordinator();
    }

    protected void setupCoordinator() {

        onboardCoordinator = getCoordinatorStore().get(CoordinatorUtil.COORDINATOR_ONBOARD_ID);
        if (onboardCoordinator == null) {
            onboardCoordinator = new OnboardCoordinator(CoordinatorUtil.COORDINATOR_ONBOARD_ID);
            getCoordinatorStore().put(CoordinatorUtil.COORDINATOR_ONBOARD_ID, onboardCoordinator);
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
