package com.intervalintl.login;

import android.support.v7.app.AppCompatActivity;
import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import dagger.BindsInstance;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class LoginViewModelBuilder {

    ParentComponent parentComponent;


    public LoginViewModelBuilder(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Component build(LoginViewModel.Listener listener) {

        return DaggerLoginViewModelBuilder_Component.builder()
                .setParentComponent(parentComponent)
                .setListener(listener)
                .build();
    }

    @dagger.Module
    public static class Module {}

    @LoginViewModelScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = Module.class
    )
    public interface Component {

        void inject(LoginViewModel loginViewModel);


        @dagger.Component.Builder
        interface Builder {
            Builder setParentComponent(ParentComponent parentComponent);

            @BindsInstance
            Builder setListener(LoginViewModel.Listener listener);

            Component build();
        }

    }

    public interface ParentComponent {
        UserManager getUserManager();
        AppCompatActivity getActivity();
        CoordinatorScreenManager getScreenManager();
    }

    @Scope
    @Retention(CLASS)
    @interface LoginViewModelScope {}

}
