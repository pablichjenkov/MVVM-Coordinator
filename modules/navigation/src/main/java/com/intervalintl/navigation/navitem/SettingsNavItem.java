package com.intervalintl.navigation.navitem;

import android.support.annotation.Keep;
import com.interval.common.Constants;
import com.intervalintl.navigation.NavItemCoordinator;
import com.intervalintl.navigation.NavigationBuilder;
import com.intervalintl.navigation.ui.drawer.menu.MenuItem;
import com.intervalintl.settings.SettingsBuilder;
import com.intervalintl.settings.SettingsViewModel;

/**
 * Although the IDE will mark this class as unused, do not delete. This class is used with Reflection
 * */
@Keep
public class SettingsNavItem extends NavItemCoordinator {


    SettingsViewModel settingsViewModel;


    public SettingsNavItem(String tagId) {
        super(tagId);
        settingsViewModel = new SettingsViewModel(Constants.SETTINGS_VIEWMODEL_ID);
        registerChildForActivityEvents(settingsViewModel);
    }

    @Override
    public void onInputStateChange(NavigationBuilder.Component navInjector) {
        SettingsBuilder builder = new SettingsBuilder(navInjector);
        SettingsBuilder.Component settingComponent = builder.build();
        settingsViewModel.onInputStateChange(settingComponent);
    }

    @Override
    public void start() {
        settingsViewModel.start();
    }

    @Override
    public void onSelected(MenuItem menuItem) {
        settingsViewModel.start();
    }

}
