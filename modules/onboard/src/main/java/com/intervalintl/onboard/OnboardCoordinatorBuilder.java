package com.intervalintl.onboard;

import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import com.intervalintl.onboard.splash.SplashViewModelBuilder;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import dagger.BindsInstance;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class OnboardCoordinatorBuilder {

    private ParentComponent parentComponent;


    public OnboardCoordinatorBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build(OnboardCoordinator.Listener listener) {

        return DaggerOnboardCoordinatorBuilder_Component.builder()
                .setParentComponent(parentComponent)
                .setListener(listener)
                .build();
    }

    @dagger.Module
    public static class Module {}

    @OnboardCoordinatorScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component extends
            SplashViewModelBuilder.ParentComponent {

        void inject(OnboardCoordinator onboardCoordinator);


        @dagger.Component.Builder
        interface Builder {
            Builder setParentComponent(ParentComponent parentComponent);

            @BindsInstance
            Builder setListener(OnboardCoordinator.Listener listener);

            Component build();
        }

    }


    public interface ParentComponent {
        UserManager getUserManager();
        CoordinatorScreenManager getScreenManager();
    }


    @Scope
    @Retention(CLASS)
    @interface OnboardCoordinatorScope {}

}
