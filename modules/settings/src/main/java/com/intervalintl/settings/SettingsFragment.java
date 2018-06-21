package com.intervalintl.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.coordinator.view.CoordinatorFragment;


public class SettingsFragment extends CoordinatorFragment<SettingsViewModel> {

    SettingsViewModel settingsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    protected void onCoordinatorBound(SettingsViewModel coordinatorViewModel) {
        settingsViewModel = coordinatorViewModel;
    }

}
