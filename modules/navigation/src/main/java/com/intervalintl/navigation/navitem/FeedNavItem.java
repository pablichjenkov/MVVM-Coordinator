package com.intervalintl.navigation.navitem;

import android.support.annotation.Keep;
import com.interval.common.Constants;
import com.intervalintl.feed.FeedBuilder;
import com.intervalintl.feed.FeedCoordinator;
import com.intervalintl.navigation.NavItemCoordinator;
import com.intervalintl.navigation.NavigationBuilder;
import com.intervalintl.navigation.ui.drawer.menu.MenuItem;

/**
 * Although the IDE will mark this class as unused, do not delete. This class is used with Reflection
 * */
@Keep
public class FeedNavItem extends NavItemCoordinator {


    FeedCoordinator feedCoordinator;


    public FeedNavItem(String tagId) {
        super(tagId);
        feedCoordinator = new FeedCoordinator(Constants.FEED_COORDINATOR_ID);
        registerChildForActivityEvents(feedCoordinator);
    }

    @Override
    public void onInputStateChange(NavigationBuilder.Component navInjector) {
        FeedBuilder builder = new FeedBuilder(navInjector);
        FeedBuilder.Component settingComponent = builder.build();
        feedCoordinator.onInputStateChange(settingComponent);
    }

    @Override
    public void start() {
        feedCoordinator.start();
    }

    @Override
    public void onSelected(MenuItem menuItem) {
        feedCoordinator.start();
    }

}
