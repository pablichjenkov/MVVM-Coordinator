package com.intervalintl.voltus.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.R;
import com.pedrogomez.renderers.Renderer;


public class CustomNavigationItemRenderer extends Renderer<CustomNavigationItem> {


    private View rootView;


    @Override
    protected void setUpView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    protected void hookListeners(View rootView) {

    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_navigation_custom_menu_item, parent, false);
    }

    @Override
    public void render() {

    }

}
