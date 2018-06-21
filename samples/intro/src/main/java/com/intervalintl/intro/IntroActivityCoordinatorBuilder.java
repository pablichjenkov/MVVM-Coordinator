package com.intervalintl.intro;

import android.support.v7.app.AppCompatActivity;
import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import com.intervalintl.login.LoginCoordinatorBuilder;
import com.intervalintl.onboard.OnboardCoordinatorBuilder;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import dagger.BindsInstance;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class IntroActivityCoordinatorBuilder {

    private ParentComponent parentComponent;


    public IntroActivityCoordinatorBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build(AppCompatActivity activity, CoordinatorScreenManager screenManager) {

        return DaggerIntroActivityCoordinatorBuilder_Component.builder()
                .setParentComponent(parentComponent)
                .setActivity(activity)
                .setScreenManager(screenManager)
                .build();
    }

    @dagger.Module
    public static class Module {}

    @IntroActivityCoordinatorScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component extends
            OnboardCoordinatorBuilder.ParentComponent,
            LoginCoordinatorBuilder.ParentComponent {

        void inject(IntroActivityCoordinator introActivityCoordinator);


        @dagger.Component.Builder
        interface Builder {

            Builder setParentComponent(ParentComponent parentComponent);

            // What this Component provides, you can use the module above as well.
            @BindsInstance
            Builder setActivity(AppCompatActivity activity);

            @BindsInstance
            Builder setScreenManager(CoordinatorScreenManager screenManager);


            Component build();
        }

    }

    /**
     * Here indicates the dependencies this component must take from its parent component.
     * */
    public interface ParentComponent {
        UserManager getUserManager();
    }

    @Scope
    @Retention(CLASS)
    @interface IntroActivityCoordinatorScope {}

}
