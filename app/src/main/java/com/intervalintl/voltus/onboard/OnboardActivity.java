package com.intervalintl.voltus.onboard;

import android.os.Bundle;
import android.util.Log;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.home.HomeComponent;
import com.intervalintl.voltus.root.BackPressHandler;
import com.intervalintl.voltus.root.RootFragmentActivity;


public class OnboardActivity extends RootFragmentActivity {


    private OnboardComponent onboardComponent;
    private HomeComponent homeComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackPressHandler(backPressHandler);
        onboardComponent = new OnboardComponent(this, getLinkHandler(), this);
        onboardComponent.onStage();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.onboard_fragment_container;
    }


    int count = 3;
    BackPressHandler backPressHandler = new BackPressHandler() {
        @Override
        public boolean handleBackPress() {
            count --;
            return count > 0;
        }
    };

    public void onSplashEnd () {
        Log.d("Delete me", "onSplashEnd successfully called");
    }

}
