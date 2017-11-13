package com.intervalintl.voltus.navigation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import com.intervalintl.voltus.BaseActivity;
import com.intervalintl.voltus.Link;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.ViewModelBinding;
import com.pedrogomez.renderers.RVRendererAdapter;


public class ShellActivity extends BaseActivity {

    private NavigationDrawerViewModel mDrawerViewModel;
    private ViewModelBinding mViewModelBinding;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawer mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mButtonDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        setupView();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.shell_activity_content_frame;
    }

    private void setupView() {

        mDrawerViewModel = ViewModelProviders.of(ShellActivity.this)
                .get(NavigationDrawerViewModel.class);

        mDrawerViewModel.getObservable().observe(ShellActivity.this
                , new Observer<NavigationDrawerViewModel.Event>() {

            @Override
            public void onChanged(@Nullable NavigationDrawerViewModel.Event event) {

                switch (event) {

                    case AdapterReady:
                        setAdapter(mDrawerViewModel.getAdapter());
                        break;

                    case AdapterChanged:
                        setAdapter(mDrawerViewModel.getAdapter());
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL, false));

        mViewModelBinding = new ViewModelBinding(mDrawerViewModel, mDrawerLayout);

        mButtonDrawerToggle = findViewById(R.id.activity_shell_fab_drawer_toggle);
        mButtonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerViewModel.toggleDrawer();
            }
        });

    }

    //@Override
    public void setAdapter(RVRendererAdapter<NavigationItem> mSideMenuRendererAdapter) {
        mRecyclerView.setAdapter(mSideMenuRendererAdapter);
    }

    //@Override
    public Context getContext() {
        return this;
    }

    //@Override
    public void closeDrawer(int gravity) {
        mDrawerLayout.closeDrawer(gravity);
    }

    //@Override
    public void openDrawer(int gravity) {
        mDrawerLayout.openDrawer(gravity);
    }


    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

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
