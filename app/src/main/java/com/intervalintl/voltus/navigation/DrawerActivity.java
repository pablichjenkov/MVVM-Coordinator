package com.intervalintl.voltus.navigation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import com.intervalintl.voltus.root.RootFragmentActivity;
import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.ViewTreeModelViewBinder;
import com.pedrogomez.renderers.RVRendererAdapter;


public class DrawerActivity extends RootFragmentActivity {

    protected DrawerActivityViewModel mDrawerViewModel;
    protected ViewTreeModelViewBinder mViewTreeModelViewBinder;
    protected DrawerLayout mDrawerLayout;
    protected NavigationDrawer mNavigationDrawer;
    protected RecyclerView mRecyclerView;
    protected RVRendererAdapter<NavigationItem> mSideMenuRendererAdapter;
    protected FloatingActionButton mButtonDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        setupView();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.drawer_activity_fragment_container;
    }

    protected void setupView() {

        mDrawerViewModel = ViewModelProviders.of(DrawerActivity.this)
                .get(DrawerActivityViewModel.class);

        AssetManager assetManager = getAssets();
        mDrawerViewModel.setAssetManager(assetManager);

        mDrawerViewModel.getObservable().observe(DrawerActivity.this
                , new Observer<DrawerActivityViewModel.Event>() {

            @Override
            public void onChanged(@Nullable DrawerActivityViewModel.Event event) {

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

                    case LinkRequest:
                        Link nextLink = mDrawerViewModel.getNextLink();
                        getLinkHandler().handleLink(nextLink);
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

        mViewTreeModelViewBinder = new ViewTreeModelViewBinder(mDrawerLayout, mDrawerViewModel);

        mButtonDrawerToggle = findViewById(R.id.activity_drawer_fab_drawer_toggle);
        mButtonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerViewModel.toggleDrawer();
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
            mDrawerViewModel.drawerOpenState = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerViewModel.drawerOpenState = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    };

}
