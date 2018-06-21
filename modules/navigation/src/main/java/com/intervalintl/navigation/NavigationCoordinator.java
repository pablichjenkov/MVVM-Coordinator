package com.intervalintl.navigation;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.navigation.config.NavigationItemInfo;
import com.intervalintl.navigation.ui.drawer.menu.AdMenuItem;
import com.intervalintl.navigation.ui.drawer.menu.AdMenuItemRenderer;
import com.intervalintl.navigation.ui.drawer.menu.CustomMenuItem;
import com.intervalintl.navigation.ui.drawer.menu.CustomMenuItemRenderer;
import com.intervalintl.navigation.ui.drawer.menu.MenuItem;
import com.intervalintl.navigation.ui.drawer.menu.MenuItemRenderer;
import com.intervalintl.navigation.ui.NavItemPresenter;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;


public class NavigationCoordinator extends Coordinator<NavigationBuilder.Component> {


    private static final String TAG = "DrawerCoordinator";

    public static class Event {
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
        OpenDrawer
    }

    RVRendererAdapter<MenuItem> sideMenuRendererAdapter;
    private final MutableLiveData<Event> mObservable = new MutableLiveData<>();
    private MenuItemManager mMenuItemManager;
    public boolean drawerOpenState;
    private boolean mIsRestore;
    private int mSelectedPosition = -1;
    private MenuItem mCurMenuItem;

    @Inject
    AppCompatActivity activity;

    @Inject
    NavItemPresenter navItemPresenter;


    public NavigationCoordinator(String coordinatorId) {
        super(coordinatorId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        navItemPresenter = null;
    }

    @Override
    public boolean onBackPressed() {
        if (drawerOpenState) {
            toggleDrawer();
            return true;
        }

        Coordinator curCoordinator = getChildById(mCurMenuItem.navCoordinatorId);
        boolean backPressConsumed = curCoordinator.onBackPressed();

        return backPressConsumed || onNavDrawerHandleBack();
    }

    @Override
    public void onInputStateChange(NavigationBuilder.Component navComponent) {
        navComponent.inject(this);

        mMenuItemManager = createItemManager();
        AdapteeCollection<MenuItem> menuItemCollection = mMenuItemManager.mMenuItemCollection;
        RendererBuilder<MenuItem> rendererBuilder = provideRendererBuilder();

        sideMenuRendererAdapter = new RVRendererAdapter<>(rendererBuilder, menuItemCollection);


        for(Coordinator coordinator : children) {
            ((NavItemCoordinator)coordinator).onInputStateChange(navComponent);
        }

    }

    @Override
    public void start() {

        navItemPresenter.onNavigationCoordinatorBound(this);

        mObservable.setValue(new Event(EventType.AdapterCreated, sideMenuRendererAdapter));

        if (!mIsRestore) {
            mIsRestore = true;
            setInitialScreen();

        } else {
            navigateToPosition(mSelectedPosition);
        }
    }

    public void subscribe(LifecycleOwner lifecycleOwner, Observer<Event> observer) {
        mObservable.observe(lifecycleOwner, observer);
    }

    public RendererBuilder<MenuItem> provideRendererBuilder() {
        RendererBuilder<MenuItem> rendererBuilder = new RendererBuilder<>();
        rendererBuilder.bind(MenuItem.class, new MenuItemRenderer(this));
        rendererBuilder.bind(AdMenuItem.class, new AdMenuItemRenderer());
        rendererBuilder.bind(CustomMenuItem.class, new CustomMenuItemRenderer());
        return rendererBuilder;
    }

    public void navigateToPosition(int position) {
        if (position < 0 || position >= mMenuItemManager.mMenuItemCollection.size()) {
            return;
        }

        MenuItem menuItem = mMenuItemManager.getItem(position);
        handleNavigationItem(menuItem);
    }

    public void handleNavigationItem(MenuItem menuItem) {

        mCurMenuItem = menuItem;
        NavItemCoordinator navItemCoordinator = getChildById(menuItem.navCoordinatorId);
        navItemCoordinator.onSelected(menuItem);

        if (menuItem.isSelectable()) {
            mSelectedPosition = menuItem.adapterPosition;
            mMenuItemManager.setSelected(menuItem.adapterPosition);
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

    protected boolean onNavDrawerHandleBack() {

        if (mCurMenuItem.adapterPosition != 0) {
            navigateToPosition(0);
            return true;
        }

        return false;

    }

    private void setInitialScreen() {
        // We can add some fragments to the Router since the beginning, this way they are created ahead of time.
        // Good for fragments like the FragmentMap.
        //router.go(mMenuItemManager.getItem(1).link);

        navigateToPosition(0);
    }

    private MenuItemManager createItemManager() {
        AdapteeCollection<MenuItem> navigationMenuCollection = loadNavigationMenuFromAssets();
        MenuItemManager menuItemManager = new MenuItemManager(navigationMenuCollection);

        return menuItemManager;
    }

    private AdapteeCollection<MenuItem> loadNavigationMenuFromAssets() {

        AdapteeCollection<MenuItem> navigationItems = new ListAdapteeCollection<>();

        try {

            // TODO(Pablo): Provide this with Dagger from Application component.
            AssetManager assetManager = activity.getAssets();
            InputStream inputStream = assetManager.open("hamper_navigation_items.json");
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            Type collectionType = new TypeToken<List<NavigationItemInfo>>() {}.getType();

            Gson gson = new Gson();
            List<NavigationItemInfo> menuItemInfoList = gson.fromJson(reader, collectionType);

            int index = 0;
            for (NavigationItemInfo navItemInfo : menuItemInfoList) {
                MenuItem menuItem = createNavigationItemFromItemInfo(navItemInfo);

                if (menuItem == null ) {
                    continue;
                }

                NavItemCoordinator navItemCoordinator = createAndAttachNavItemCoordinator(navItemInfo);
                if (navItemCoordinator == null) {
                    continue;
                }

                menuItem.navCoordinatorId = navItemCoordinator.getId();
                menuItem.adapterPosition = index ++;
                menuItem.setSelectable(navItemInfo.isSelectable);

                navigationItems.add(menuItem);
            }

            return navigationItems;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private MenuItem createNavigationItemFromItemInfo(NavigationItemInfo screenInfo) {

        try {

            Class<?> unboundClazz = Class.forName(screenInfo.sideMenuModelClass);

            if (MenuItem.class.isAssignableFrom(unboundClazz)) {

                return  (MenuItem) unboundClazz.newInstance();

            } else {
                Timber.e(screenInfo.sideMenuModelClass
                        .concat(" must extends MenuItem class"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private NavItemCoordinator createAndAttachNavItemCoordinator(NavigationItemInfo navItemInfo) {
        try {
            String childCoordinatorId = navItemInfo.navCoordinatorId;
            NavItemCoordinator navItemCoordinator = getChildById(childCoordinatorId);

            if (navItemCoordinator != null) {
                return navItemCoordinator;
            }

            Class<?> unboundClazz = Class.forName(navItemInfo.navCoordinatorClass);

            if (NavItemCoordinator.class.isAssignableFrom(unboundClazz)) {
                Constructor<?> constructor = unboundClazz.getConstructor(String.class);
                navItemCoordinator = (NavItemCoordinator) constructor.newInstance(childCoordinatorId);
                registerChildForActivityEvents(navItemCoordinator);

                return navItemCoordinator;

            }  else {
                Timber.e("NavigationItemInfo.navCoordinatorClass must extends NavItemCoordinator");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static class MenuItemManager {


        private AdapteeCollection<MenuItem> mMenuItemCollection;
        private int selectedPosition = -1;


        public MenuItemManager(AdapteeCollection<MenuItem> menuItemList) {
            mMenuItemCollection = menuItemList;
        }

        public MenuItem getItem(int index) {
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
