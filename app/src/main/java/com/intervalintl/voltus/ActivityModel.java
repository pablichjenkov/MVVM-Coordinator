package com.intervalintl.voltus;

import android.arch.lifecycle.ViewModel;
import com.intervalintl.voltus.fragment.Fragment1;
import com.intervalintl.voltus.navigation.NavigationActor;
import java.lang.ref.WeakReference;


public class ActivityModel extends ViewModel {

    private WeakReference<BaseActivity> mHostActivity;
    private NavigationActor mNavigationActor;


    public ActivityModel() {}

    public void attachActivity(BaseActivity activity) {
        setup(activity);
    }

    public void detachActivity() {
        mHostActivity.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private BaseActivity activity() {
        return mHostActivity.get();
    }

    private void setup(BaseActivity activity) {
        mHostActivity = new WeakReference<>(activity);
        mNavigationActor = new NavigationActor();
        mNavigationActor.start(mScreenManager);

        activity().setContentView(R.layout.activity_shell);

        Link link = Link.Builder.newLink()
                .toRoute(Fragment1.class, "Fragment1")
                .build();

        activity().getLinkHandler().handleLink(link);
    }


    private ScreenManager mScreenManager = new ScreenManager() {

        public Callback mCallback;

        @Override
        public void setScreen() {

        }

        @Override
        public void registerCallback(Callback callback) {
            mCallback = callback;
        }
    };

}