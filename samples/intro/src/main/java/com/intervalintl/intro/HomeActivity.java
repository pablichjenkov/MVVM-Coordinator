package com.intervalintl.intro;

import com.intervalintl.navigation.ui.drawer.DrawerActivity;
import com.intervalintl.navigation.NavigationBuilder;


public class HomeActivity extends DrawerActivity {

    @Override
    protected NavigationBuilder.ParentComponent onProvideNavigationBuilderParentComponent() {
        return IntroApplication.instance().getAppComponent();
    }

}
