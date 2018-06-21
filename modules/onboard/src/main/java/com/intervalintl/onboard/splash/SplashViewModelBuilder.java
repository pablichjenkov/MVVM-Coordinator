package com.intervalintl.onboard.splash;

import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import dagger.BindsInstance;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class SplashViewModelBuilder {

    ParentComponent parentComponent;


    public SplashViewModelBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build(SplashViewModel.Listener listener) {

        return DaggerSplashViewModelBuilder_Component.builder()
                .setParentComponent(parentComponent)
                .setListener(listener)
                .build();
    }

    @dagger.Module
    public static class Module {}

    @SplashViewModelScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component {

        void inject(SplashViewModel splashViewModel);


        @dagger.Component.Builder
        interface Builder {
            Builder setParentComponent(ParentComponent parentComponent);

            @BindsInstance
            Builder setListener(SplashViewModel.Listener listener);

            Component build();
        }

    }

    public interface ParentComponent {
        UserManager getUserManager();
        CoordinatorScreenManager getScreenManager();
    }

    @Scope
    @Retention(CLASS)
    @interface SplashViewModelScope {}

}
