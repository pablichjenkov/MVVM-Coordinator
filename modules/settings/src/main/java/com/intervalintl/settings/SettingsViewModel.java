package com.intervalintl.settings;

import com.interval.common.Constants;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import javax.inject.Inject;


public class SettingsViewModel extends Coordinator<SettingsBuilder.Component> {


    @Inject
    CoordinatorScreenManager screenManager;


    public SettingsViewModel(String id) {
        super(id);
    }

    @Override
    public void onInputStateChange(SettingsBuilder.Component injector) {
        injector.inject(this);
    }

    @Override
    public void start() {
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setCoordinatorId(getId());

        screenManager.setView(settingsFragment, Constants.SETTINGS_FRAGMENT_TAG);
    }

}
