package com.intervalintl.navigation.ui.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.intervalintl.navigation.R;


public class NavigationDrawer extends FrameLayout {


    private View mRootView;
    private RecyclerView mRecyclerView;


    public NavigationDrawer(@NonNull Context context) {
        super(context);
        init();
    }

    public NavigationDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationDrawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationDrawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mRootView = inflater.inflate(R.layout.navigation_drawer, this, true);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.navigation_drawer_recycler);
    }

    /*public void setPresenter(NavigationDrawerCoordinator drawerCoordinator) {

    }

    public void setNavigationItemRenderer() {

    }

    public void setNavigationItems(List<MenuItem> navigationItems) {

    }

    public void setSelectedNavigationItem(int position) {

    }


    public interface Listener {

    }*/

}
