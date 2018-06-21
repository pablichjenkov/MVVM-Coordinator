package com.intervalintl.navigation;

import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.navigation.ui.drawer.menu.MenuItem;


public abstract class NavItemCoordinator extends Coordinator<NavigationBuilder.Component> {

    public NavItemCoordinator(String tagId) {
        super(tagId);
    }

    public abstract void onSelected(MenuItem menuItem);

}
