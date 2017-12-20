package com.intervalintl.voltus.onboard;

import android.os.Bundle;
import android.util.Log;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.root.RootFragmentActivity;
import com.intervalintl.voltus.util.InteractorUtil;


public class OnboardActivity extends RootFragmentActivity {


    private OnboardInteractor onboardInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackPressHandler(onboardInteractor);

        onboardInteractor = new OnboardInteractor(InteractorUtil.INTERACTOR_ONBOARD
                , this, getLinkHandler(), this);

        onboardInteractor.act();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.onboard_fragment_container;
    }


    public void onSplashEnd () {
        Log.d("Delete me", "onSplashEnd successfully called");
    }

}
