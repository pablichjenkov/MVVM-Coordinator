package com.intervalintl.feed;

import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class FeedBuilder {


    ParentComponent parentComponent;


    public FeedBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build() {
          Component component = DaggerFeedBuilder_Component.builder()
                  .setParentComponent(parentComponent)
                  .build();

          return component;
    }

    @dagger.Module
    public static class Module {}

    @FeedCoordinatorScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component {

        void inject(FeedCoordinator feedCoordinator);


        @dagger.Component.Builder
        interface Builder {

            Builder setParentComponent(ParentComponent parentComponent);

            Component build();
        }

    }

    public interface ParentComponent {
        CoordinatorScreenManager getScreenManager();
        UserManager getUserManager();
        //FeedManager getProductManager();
    }

    @Scope
    @Retention(CLASS)
    @interface FeedCoordinatorScope {}

}
