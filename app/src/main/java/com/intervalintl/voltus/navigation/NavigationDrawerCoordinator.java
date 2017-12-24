package com.intervalintl.voltus.navigation;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.navigation.config.NavigationItemInfo;
import com.intervalintl.voltus.root.Router;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.BaseViewModel;
import com.intervalintl.voltus.viewmodel.Coordinator;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;


public class NavigationDrawerCoordinator extends Coordinator {


    private static final String TAG = "DrawerCoordinator";

    public class Event {
        public final EventType type;
        public final Object payload;

        public Event(EventType type) {
            this.type = type;
            this.payload = null;
        }

        public Event(EventType type, Object payload) {
            this.type = type;
            this.payload = payload;
        }

    }

    public enum EventType {
        AdapterCreated,
        AdapterChanged,
        CloseDrawer,
        OpenDrawer,

        LinkRequest
    }

    private AssetManager assetManager;
    private final MutableLiveData<Event> mObservable = new MutableLiveData<>();
    private Router router;
    private MenuItemManager mMenuItemManager;
    public boolean drawerOpenState;
    private boolean mIsRestore;
    private int mSelectedPosition = -1;
    private Link mCurLink;


    public NavigationDrawerCoordinator(String coordinatorId) {
        super(coordinatorId);
    }

    @Override
    public void act() {

    }

    @Override
    public void shut() {

    }

    @Override
    public <VM extends BaseViewModel> VM provideViewModel(BaseFragment baseFragment) {
        return null;
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    public void onCreate(Router router) {
        this.router = router;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void onViewAttach() {
        super.onViewAttach();

        mMenuItemManager = setupItemManager();
        AdapteeCollection<NavigationItem> menuItemCollection = mMenuItemManager.mMenuItemCollection;
        RendererBuilder<NavigationItem> rendererBuilder = provideRendererBuilder();

        RVRendererAdapter<NavigationItem> sideMenuRendererAdapter
                = new RVRendererAdapter<>(rendererBuilder, menuItemCollection);

        mObservable.setValue(new Event(EventType.AdapterCreated, sideMenuRendererAdapter));

        if (!mIsRestore) {
            mIsRestore = true;
            setInitialScreen();

        } else {
            navigateToPosition(mSelectedPosition);
        }

    }

    @Override
    public void onViewDetach() {
        super.onViewDetach();
    }

    public LiveData<Event> getObservable() {
        return mObservable;
    }

    public Link getNextLink() {
        return mCurLink;
    }

    public RendererBuilder<NavigationItem> provideRendererBuilder() {
        RendererBuilder<NavigationItem> rendererBuilder = new RendererBuilder<>();
        rendererBuilder.bind(NavigationItem.class, new NavigationItemRenderer(this));
        rendererBuilder.bind(AdNavigationItem.class, new AdNavigationItemRenderer());
        rendererBuilder.bind(CustomNavigationItem.class, new CustomNavigationItemRenderer());
        return rendererBuilder;
    }

    public void navigateToPosition(int position) {
        if (position < 0 || position >= mMenuItemManager.mMenuItemCollection.size()) {
            return;
        }

        NavigationItem navigationItem = mMenuItemManager.getItem(position);
        handleNavigationItem(navigationItem);
    }

    public void handleNavigationItem(NavigationItem navigationItem) {

        mCurLink = navigationItem.link;
        mObservable.postValue(new Event(EventType.LinkRequest));

        if (navigationItem.isSelectable()) {
            mSelectedPosition = navigationItem.adapterPosition;
            mMenuItemManager.setSelected(navigationItem.adapterPosition);
            mObservable.setValue(new Event(EventType.AdapterChanged));
        }

    }

    public void toggleDrawer() {
        if (drawerOpenState) {
            drawerOpenState = false;
            mObservable.setValue(new Event(EventType.CloseDrawer));

        } else {
            drawerOpenState = true;
            mObservable.setValue(new Event(EventType.OpenDrawer));
        }
    }

    public int getItemCount() {
        return mMenuItemManager.mMenuItemCollection.size();
    }

    public NavigationItem getItem(int position) {
        return mMenuItemManager.getItem(position);
    }

    public boolean onBack() {
        Log.d("DrawerPresenter", "On Back Pressed with current Route: X");

        if (drawerOpenState) {
            toggleDrawer();
            return true;
        }

        return false;
        /*if (topPath.equals(mMenuItemManager.getItem(0).link.getPath())) {
            // TODO: Remove this once created a logic that unregister nested Routers on their parent back press.
            // Right now is a library flaw. The idea is to call each nested Router when the parent is back pressed.
            // Right now only the current fragment is receiving the back pressed event.
            //Router.destroy();
            return false;
        }
        else {
            navigateToPosition(0);
            return true;
        }*/
    }

    private void setInitialScreen() {
        // Add some fragments to the Router since the beginning, this way they are created ahead of time.
        // Good for fragments like the FragmentMap.
        //Router.go(mMenuItemManager.getItem(1).link);

        navigateToPosition(0);
    }

    private MenuItemManager setupItemManager() {

        AdapteeCollection<NavigationItem> navigationMenuCollection = loadNavigationMenuFromAssets();
        MenuItemManager menuItemManager = new MenuItemManager(navigationMenuCollection);

        return menuItemManager;

    }

    private AdapteeCollection<NavigationItem> loadNavigationMenuFromAssets() {

        AdapteeCollection<NavigationItem> navigationItems = new ListAdapteeCollection<>();

        try {

            InputStream inputStream = assetManager.open("navigation_items.json");
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            Type collectionType = new TypeToken<List<NavigationItemInfo>>() {}.getType();

            Gson gson = new Gson();
            List<NavigationItemInfo> menuItemInfoList = gson.fromJson(reader, collectionType);

            int index = 0;
            for (NavigationItemInfo navItemInfo : menuItemInfoList) {
                NavigationItem navigationItem = createNavigationFromScreenInfo(navItemInfo);

                if (navigationItem == null ) {
                    continue;
                }

                navigationItem.adapterPosition = index ++;
                navigationItem.link = createLinkFromScreenInfo(navItemInfo);
                navigationItem.setSelectable(navItemInfo.isSelectable);

                navigationItems.add(navigationItem);

            }

            return navigationItems;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <F extends Fragment, A extends Activity> Link createLinkFromScreenInfo(NavigationItemInfo screenInfo) {

        try {
            Link link = null;
            Class<?> unboundClazz = Class.forName(screenInfo.screenClass);

            if (Fragment.class.isAssignableFrom(unboundClazz)) {

                Class<F> boundClazz = (Class<F>) unboundClazz;

                link = Link.Builder.newLink()
                        .toRoute(boundClazz, screenInfo.screenId)
                        .inSection("Route_ShellActivity")
                        .build();

            } else if (Activity.class.isAssignableFrom(unboundClazz)) {

                Class<A> boundClazz = (Class<A>) unboundClazz;

                link = Link.Builder.newLink()
                        .toRoute(boundClazz)
                        .build();

            } else {
                Log.d(TAG, "NavigationItemInfo.screenClassName must extends Fragment or Activity");
            }

            return link;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private NavigationItem createNavigationFromScreenInfo(NavigationItemInfo screenInfo) {

        try {
            NavigationItem navigationItem = null;
            Class<?> unboundClazz = Class.forName(screenInfo.sideMenuModelClass);

            if (NavigationItem.class.isAssignableFrom(unboundClazz)) {

                navigationItem = (NavigationItem) unboundClazz.newInstance();

            }  else {
                Log.d(TAG, "NavigationItemInfo.sideMenuModelClass must extends NavigationItem");
            }

            return navigationItem;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static class MenuItemManager {


        private AdapteeCollection<NavigationItem> mMenuItemCollection;
        private int selectedPosition = -1;


        public MenuItemManager(AdapteeCollection<NavigationItem> menuItemList) {
            mMenuItemCollection = menuItemList;
        }

        public NavigationItem getItem(int index) {
            return mMenuItemCollection.get(index);
        }

        public int getItemCount() {
            return mMenuItemCollection.size();
        }

        public void setSelected(int newSelectedPosition) {
            if (selectedPosition >= 0) {
                mMenuItemCollection.get(selectedPosition).setSelected(false);
            }

            mMenuItemCollection.get(newSelectedPosition).setSelected(true);
            selectedPosition = newSelectedPosition;
        }
    }

}
