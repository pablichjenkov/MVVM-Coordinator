
# @Deprecated
##### It's been too hard maintaining the Coordinators Arch implementation in Java and Kotlin. 
##### I decide to only support the Kotlin version so go to 
[MVVM-C implemented with Compose Multiplatform](https://github.com/pablichjenkov/macao-sdk)


### Voltus (this is old, visit above links for updated implementation)
Voltus is a research on the MVVM-Coordinator architecture. The idea behind Voltus is making an app
by composing it with small modules rather than a full monolithic mass of code. We call this modules
**Coordinators**, and they form a **Coordinator Tree** that is unique per Activity. The advantage of
Coordinators are many, one of them is that you can compile and test them independently, reducing
build times.

 #### Module: Coordinator
 In this gradle subproject you can see the implementation of the pattern. Coordinators Pattern is 
 basically the State Machine Pattern. Every **Coordinator** has a list of Sub-Coordinators that 
 enter on stage depending on the actions received from external inputs, Sub-Coordinators can be seen
 as Sub-State-Machines as well.
 The **ViewModel** is a Business Logic encapsulation. ViewModels are units of business rules
 implementations, Views register to receive events from these ViewModels, but the **View**
 doesn't keep any state, they are a window to see the ViewModel state but they don't have this
 state. It makes the architecture very robust against rotation or any configuration changes.
 Coordinators and ViewModels are not affected by configuration changes.
 Every time rotation happens the View Tree is destroyed and recreated. When destroying the Views
 unsubscribe from ViewModels, then when re-created they re-subscribe to its previous ViewModel. 

 ##### How to use
 1. First, define your Activity by extending the **CoordinatorActivity\<Input\>** class.
 This is a helper Activity parametrized to the root Coordinator Input type. This Activity handles
 the root Coordinator lifecycle. Under the hood it uses the Android ViewModel from Architecture
 Components to persist the Coordinator Tree upon configuration changes.
 
 ``` java
 
    public class IntroActivityCoordinator extends Coordinator<IntroCoordinatorBuilder.Component> {
    
        @Override
        protected Coordinator onProvideRootCoordinator() {
            // Return an instance of your root Coordinator
            return new IntroCoordinator("INTRO_ACTIVITY_COORDINATOR");
        }
    
        @Override
        protected IntroCoordinatorBuilder.Component onProvideRootCoordinatorInput() {
    
            // Return an instance of your Input.
    
            IntroCoordinatorBuilder introBuilder = new IntroCoordinatorBuilder(
                    IntroApplication.instance().getAppComponent());
    
            IntroCoordinatorBuilder.Component introComponent = introBuilder.build(
                    IntroActivity.this,
                    new CoordinatorScreenManagerDefault(
                            getSupportFragmentManager(),
                            (ViewGroup) findViewById(R.id.introActivityFragmentContainer)
                    )
            );
    
            return introComponent;
        }
    
    }
 
 ```
 
 2. Define your root Coordinator by extending a **Coordiantor\<Input\>** class and override the
 **Coordinator.onInputStateChange(Input inputInjector)** 
 
 ``` java
 
     @Inject
     AppCompatActivity appCompatActivity;
     
 
     @Override
     public void onInputStateChange(LoginViewModelBuilder.Component injector) {
        
        // Inject internal dependencies
        injector.inject(this);
        
        // Dispatch the dependency injector to children coordinators
        if (onboardCoordinator != null) {
            OnboardCoordinatorBuilder onboardCoordinatorBuilder = new OnboardCoordinatorBuilder(inputInjector);
            onboardCoordinator.onInputStateChange(onboardCoordinatorBuilder.build(onboardingListener));
        }

        if (loginCoordinator != null) {
            LoginCoordinatorBuilder loginCoordinatorBuilder = new LoginCoordinatorBuilder(inputInjector);
            loginCoordinator.onInputStateChange(loginCoordinatorBuilder.build(loginListener));
        }
        
     }
     
```
 The **Input** parameter is a **Dependency Injector** that gets pass down to the whole tree. It
 basically contains the different **Dependencies** that will be consumed for every child
 Coordinator. The provided samples use Dagger2 for this purpose but you can implement your own
 injector class or use the **Service Locator** pattern if you are ok with it. 
 
 3. override the **Coordinator.start()** method. This method is called from a 
 parent Coordinator indicating that this Coordinator is now on stage. In the case of the root 
 Coordinator it will be called from its containing Activity after this one resumes.
                                                                       
 ``` java
      @Override
      public void start() {
  
          if (stage == Stage.Idle) {
              stage = Stage.Splash;
              showSplashView();
          }
          
      }
      
      private void showSplashView() {
          SplashFragment splashFragment;
          splashFragment = new SplashFragment();
          splashFragment.setCoordinatorId(getId());
  
          screenManager.setView(splashFragment, Constants.SPLASH_FRAGMENT_TAG);
      }
      
 ```

 Coordinator.start() is called after Coordinator.onInputStateChange(Input inputInjector), so you
 can assume all your Coordinator dependencies are injected already.
 If your Coordinator uses the Activity's ViewTree or in other words, if it is a UI Coordinator.
 Then make sure an instance of **CoordinatorScreenManager** is injected(provided) to you. You will
 use this interface to throw Fragments or ViewGroups into the FragmentContainer of your Activity
 view layout.
 The **':modules:common'** project has a sample implementation of **CoordinatorScreenManager**,
 it's very simple just for the sample purpose. You can implement a richer one using other frameworks
 like the Google's Navigation Component or any fancy View transition library you find out there.

 A Coordinator doesn't care about its parent Coordinator logic, it only do one concern and report 
 back the result by means of a callback listener or contract, that was injected by its parent at 
 some point. The communication between Parent and Child can be also reactive using an 
 Observer-Subject mechanism, that's up to you how you want to expose your result/response to your
 Listener. This principle makes the Coordinators reusable, since you can insert them anywhere
 in the app flow.
 
 See the sample apps for most use cases. Start by using one coordinator from the modules
 independently, and then create compositions of them by creating a parent coordinator that
 contains more than one child Coordinator or ViewModel as well.
