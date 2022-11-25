package com.intervalintl.intro;

import android.os.Bundle;
import android.view.ViewGroup;
import com.interval.common.CoordinatorScreenManagerDefault;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.CoordinatorActivity;


public class IntroActivity extends CoordinatorActivity<IntroActivityCoordinatorBuilder.Component> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }


    // region: Coordinator Activity

    @Override
    protected Coordinator onProvideRootCoordinator() {
        return new IntroActivityCoordinator("INTRO_ACTIVITY_COORDINATOR");
    }

    @Override
    protected IntroActivityCoordinatorBuilder.Component onProvideRootCoordinatorInput() {

        IntroActivityCoordinatorBuilder introBuilder = new IntroActivityCoordinatorBuilder(
                ((IntroApplication) getApplication()).getAppComponent()
        );

        IntroActivityCoordinatorBuilder.Component introActivityComponent = introBuilder.build(
                IntroActivity.this,
                new CoordinatorScreenManagerDefault(
                        getSupportFragmentManager(),
                        (ViewGroup) findViewById(R.id.introActivityFragmentContainer)
                )
        );

        return introActivityComponent;
    }

    // endregion

}
