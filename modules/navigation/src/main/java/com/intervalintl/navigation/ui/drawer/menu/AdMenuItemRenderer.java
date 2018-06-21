package com.intervalintl.navigation.ui.drawer.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.intervalintl.navigation.R;
import com.pedrogomez.renderers.Renderer;


public class AdMenuItemRenderer extends Renderer<AdMenuItem> {


    private View rootView;
    private TextView adText;


    @Override
    protected void setUpView(View rootView) {
        this.rootView = rootView;
        this.adText = (TextView) rootView.findViewById(R.id.cell_navigation_ad_menu_item_txt);
    }

    @Override
    protected void hookListeners(View rootView) {

    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_navigation_ad_menu_item, parent, false);
    }

    @Override
    public void render() {
        String text = getContent().adText;
        adText.setText(text);
    }

}
