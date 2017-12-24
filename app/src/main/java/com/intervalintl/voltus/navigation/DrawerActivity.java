package com.intervalintl.voltus.navigation;

import android.arch.lifecycle.Observer;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.util.CoordinatorUtil;
import com.intervalintl.voltus.viewmodel.BaseActivity;
import com.intervalintl.voltus.viewmodel.ViewTreeAttachBinder;
import com.pedrogomez.renderers.RVRendererAdapter;


public class DrawerActivity extends BaseActivity {

    protected NavigationDrawerCoordinator mDrawerCoordinator;
    protected DrawerLayout mDrawerLayout;
    protected NavigationDrawer mNavigationDrawer;
    protected RecyclerView mRecyclerView;
    protected RVRendererAdapter<NavigationItem> mSideMenuRendererAdapter;
    protected FloatingActionButton mButtonDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        setupCoordinator();
        setupView();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.drawer_activity_fragment_container;
    }

    protected void setupCoordinator() {

        mDrawerCoordinator = getCoordinatorStore().get(CoordinatorUtil.COORDINATOR_NAVIGATION_DRAWER_ID);
        if (mDrawerCoordinator == null) {
            mDrawerCoordinator = new NavigationDrawerCoordinator(CoordinatorUtil.COORDINATOR_NAVIGATION_DRAWER_ID);
            getCoordinatorStore().put(CoordinatorUtil.COORDINATOR_NAVIGATION_DRAWER_ID, mDrawerCoordinator);
        }

        setBackPressHandler(mDrawerCoordinator);
        mDrawerCoordinator.onCreate(getRouter());
        mDrawerCoordinator.dispatch();
    }

    protected void setupView() {
        AssetManager assetManager = getAssets();
        mDrawerCoordinator.setAssetManager(assetManager);

        mDrawerCoordinator.getObservable().observe(DrawerActivity.this
                , new Observer<NavigationDrawerCoordinator.Event>() {

            @Override
            public void onChanged(@Nullable NavigationDrawerCoordinator.Event event) {

                switch (event.type) {

                    case AdapterCreated:
                        mSideMenuRendererAdapter = (RVRendererAdapter<NavigationItem>)event.payload;
                        setAdapter(mSideMenuRendererAdapter);
                        break;

                    case AdapterChanged:
                        mSideMenuRendererAdapter.notifyDataSetChanged();
                        break;

                    case OpenDrawer:
                        openDrawer(Gravity.LEFT);
                        break;

                    case CloseDrawer:
                        closeDrawer(Gravity.LEFT);
                        break;

                    // TODO: Move this logic inside the child coordinator
                    case LinkRequest:
                        Link nextLink = mDrawerCoordinator.getNextLink();
                        getRouter().handleLink(nextLink);
                        break;

                }

            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerListener);
        mNavigationDrawer = mDrawerLayout.findViewById(R.id.navigation_drawer);
        mRecyclerView = mNavigationDrawer.findViewById(R.id.navigation_drawer_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(DrawerActivity.this
                , LinearLayoutManager.VERTICAL, false));

        // It injects a listener into the passed view for attach events
        new ViewTreeAttachBinder(mDrawerLayout, mDrawerCoordinator);

        mButtonDrawerToggle = findViewById(R.id.activity_drawer_fab_drawer_toggle);
        mButtonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerCoordinator.toggleDrawer();
            }
        });

    }

    protected void setAdapter(RVRendererAdapter<NavigationItem> sideMenuRendererAdapter) {
        mRecyclerView.setAdapter(sideMenuRendererAdapter);
    }

    protected void closeDrawer(int gravity) {
        mDrawerLayout.closeDrawer(gravity);
    }

    protected void openDrawer(int gravity) {
        mDrawerLayout.openDrawer(gravity);
    }


    protected DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerCoordinator.drawerOpenState = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerCoordinator.drawerOpenState = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    };

}
