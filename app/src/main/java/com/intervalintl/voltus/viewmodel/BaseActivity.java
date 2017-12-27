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

    protected CoordinatorStore getCoordinatorStore() {
        return coordinatorStore;
    }

    public Coordinator findCoordinatorByTagId(String tagId) {
        Coordinator result = coordinatorStore.get(tagId);
        if (result != null) {
            return result;
        }
        for (Coordinator coordinator : coordinatorStore.all()) {
            result = coordinator.findChildByTagId(tagId);
            if (result != null) {
                return result;
            }
        }

        return result;
    }

}
