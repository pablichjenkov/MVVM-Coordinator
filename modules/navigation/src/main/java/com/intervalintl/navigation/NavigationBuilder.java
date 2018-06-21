package com.intervalintl.navigation;

import android.support.v7.app.AppCompatActivity;
import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import com.intervalintl.feed.FeedBuilder;
import com.intervalintl.navigation.ui.NavItemPresenter;
import com.intervalintl.settings.SettingsBuilder;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import dagger.BindsInstance;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class NavigationBuilder {


    private ParentComponent parentComponent;


    public NavigationBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build(AppCompatActivity activity
            , CoordinatorScreenManager screenManager
            , NavItemPresenter navItemPresenter) {

        return DaggerNavigationBuilder_Component.builder()
                .setParentComponent(parentComponent)
                .setActivity(activity)
                .setScreenManager(screenManager)
                .setNavItemPresenter(navItemPresenter)
                .build();
    }

    @dagger.Module
    public static class Module {}

    @NavigationScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component extends FeedBuilder.ParentComponent
            , SettingsBuilder.ParentComponent {

        void inject(NavigationCoordinator drawerCoordinator);
        void inject(NavItemCoordinator navItemCoordinator);


        @dagger.Component.Builder
        interface Builder {
            Builder setParentComponent(ParentComponent parentComponent);

            @BindsInstance
            Builder setActivity(AppCompatActivity activity);

            @BindsInstance
            Builder setScreenManager(CoordinatorScreenManager screenManager);

            @BindsInstance
            Builder setNavItemPresenter(NavItemPresenter navItemPresenter);

            Component build();
        }

    }


    public interface ParentComponent {
        UserManager getUserManager();
    }

    @Scope
    @Retention(CLASS)
    @interface NavigationScope {}

}
