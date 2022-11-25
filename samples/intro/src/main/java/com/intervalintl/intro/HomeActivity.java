package com.intervalintl.intro;

import com.intervalintl.navigation.NavigationBuilder;
import com.intervalintl.navigation.ui.drawer.DrawerActivity;


public class HomeActivity extends DrawerActivity {

    @Override
    protected NavigationBuilder.ParentComponent onProvideNavigationBuilderParentComponent() {
        return ((IntroApplication) getApplication()).getAppComponent();
    }

}
