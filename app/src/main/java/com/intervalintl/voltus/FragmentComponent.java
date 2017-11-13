package com.intervalintl.voltus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class FragmentComponent extends Fragment implements BackPressHandler {

    private static final String KEY_HIDDEN_STATE = "key_hidden_state";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean prevInstanceWasHidden = savedInstanceState.getBoolean(KEY_HIDDEN_STATE, false);

            if (prevInstanceWasHidden) {
                getFragmentManager()
                        .beginTransaction()
                        .hide(this)
                        .commit();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_HIDDEN_STATE, isHidden());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
