package com.intervalintl.intro;

import android.app.Application;

import com.intervalintl.navigation.NavigationBuilder;

import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
})
public interface AppComponent extends IntroActivityCoordinatorBuilder.ParentComponent
        , NavigationBuilder.ParentComponent {

    // Downstream dependencies will be taken from the Parent extensions
    //UserManager getUserManager();
    //ProductManager getProductManager();


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder setApplication(Application application);

        AppComponent build();
    }

}