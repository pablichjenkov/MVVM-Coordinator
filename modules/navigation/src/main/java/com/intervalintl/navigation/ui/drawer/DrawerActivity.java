package com.intervalintl.navigation.ui.drawer;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.interval.common.CoordinatorScreenManagerDefault;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.CoordinatorActivity;
import com.intervalintl.navigation.NavigationBuilder;
import com.intervalintl.navigation.NavigationCoordinator;
import com.intervalintl.navigation.R;
import com.intervalintl.navigation.ui.drawer.menu.MenuItem;
import com.intervalintl.navigation.ui.NavItemPresenter;
import com.pedrogomez.renderers.RVRendererAdapter;


public abstract class DrawerActivity extends CoordinatorActivity<NavigationBuilder.Component>
        implements NavItemPresenter {


    protected NavigationBuilder.Component navigationComponent;
    protected NavigationCoordinator navigationCoordinator;
    protected DrawerLayout mDrawerLayout;
    protected NavigationDrawer mNavigationDrawer;
    protected RecyclerView mRecyclerView;
    protected RVRendererAdapter<MenuItem> mSideMenuRendererAdapter;

    // Top Bar
//    protected ImageView mTopBarToggleImg;
//    protected TextView mTopBarTitleTxt;
//    protected LinearLayout mTopBarRightContainer;
//    protected boolean showTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
    }

    public void onNavigationCoordinatorBound(NavigationCoordinator navigationCoordinator) {
        this.navigationCoordinator = navigationCoordinator;
        setupView();
    }

    protected void setupView() {
        /*
        mTopBarToggleImg = findViewById(R.id.drawer_activity_top_bar_toggle_img);
        mTopBarTitleTxt = findViewById(R.id.drawer_activity_top_bar_title_txt);
        mTopBarRightContainer = findViewById(R.id.drawer_activity_top_bar_right_container);
        showTopBar = getTopBarVisibility();

        if (!showTopBar) {
            mTopBarToggleImg.setVisibility(View.GONE);
            mTopBarTitleTxt.setVisibility(View.GONE);
            mTopBarRightContainer.setVisibility(View.GONE);

        } else {
            mTopBarToggleImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationCoordinator.toggleDrawer();
                }
            });

        }
*/

        navigationCoordinator.subscribe(DrawerActivity.this
                , new Observer<NavigationCoordinator.Event>() {

            @Override
            public void onChanged(@Nullable NavigationCoordinator.Event event) {

                switch (event.type) {

                    case AdapterCreated:
                        mSideMenuRendererAdapter = (RVRendererAdapter<MenuItem>)event.payload;
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

                }

            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerListener);
        mNavigationDrawer = mDrawerLayout.findViewById(R.id.navigation_drawer);
        mRecyclerView = mNavigationDrawer.findViewById(R.id.navigation_drawer_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(DrawerActivity.this
                , LinearLayoutManager.VERTICAL, false));

    }

    protected boolean getTopBarVisibility() {
        return true;
    }

    /*@Override
    public void setBackIndicator() {

    }*/

    /*@Override
    public ViewGroup getTopBarRightContainer() {
        return mTopBarRightContainer;
    }*/

    protected void setAdapter(RVRendererAdapter<MenuItem> sideMenuRendererAdapter) {
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
            navigationCoordinator.drawerOpenState = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            navigationCoordinator.drawerOpenState = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    };


    @Override
    protected Coordinator onProvideRootCoordinator() {
        return new NavigationCoordinator("DRAWER_ACTIVITY_NAVIGATION_COORDINATOR");
    }

    @Override
    protected NavigationBuilder.Component onProvideRootCoordinatorInput() {

        NavigationBuilder.ParentComponent appComponent = onProvideNavigationBuilderParentComponent();
        NavigationBuilder navigationBuilder = new NavigationBuilder(appComponent);

        NavigationBuilder.Component navigationComponent = navigationBuilder.build(
                DrawerActivity.this,
                new CoordinatorScreenManagerDefault(
                        getSupportFragmentManager(),
                        (ViewGroup) findViewById(R.id.drawerActivityFragmentContainer)
                ),
                DrawerActivity.this
        );

        return navigationComponent;
    }

    protected abstract NavigationBuilder.ParentComponent onProvideNavigationBuilderParentComponent();

}
