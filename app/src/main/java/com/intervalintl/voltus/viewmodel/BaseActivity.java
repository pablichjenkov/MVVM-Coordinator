package com.intervalintl.voltus.viewmodel;

import android.os.Bundle;
import com.intervalintl.voltus.root.RouterActivity;


public abstract class BaseActivity extends RouterActivity {

    private CoordinatorStore coordinatorStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordinatorStore = new CoordinatorStore(BaseActivity.this);
    }

    public CoordinatorStore getCoordinatorStore() {
        return coordinatorStore;
    }

}
