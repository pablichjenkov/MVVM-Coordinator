package com.intervalintl.settings;

import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class SettingsBuilder {


    ParentComponent parentComponent;


    public SettingsBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build() {
          Component component = DaggerSettingsBuilder_Component.builder()
                  .setParentComponent(parentComponent)
                  .build();

          return component;
    }

    @dagger.Module
    public static class Module {}

    @SettingsCoordinatorScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component {

        void inject(SettingsViewModel settingsViewModel);


        @dagger.Component.Builder
        interface Builder {

            Builder setParentComponent(ParentComponent parentComponent);

            Component build();
        }

    }


    public interface ParentComponent {
        UserManager getUserManager();
        CoordinatorScreenManager getScreenManager();
    }

    @Scope
    @Retention(CLASS)
    @interface SettingsCoordinatorScope {}

}
