package com.intervalintl.voltus.navigation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intervalintl.voltus.root.Link;
import com.intervalintl.voltus.R;
import com.pedrogomez.renderers.Renderer;


public class NavigationItemRenderer extends Renderer<NavigationItem> {


    private View rootView;
    private ImageView menuItemIcon;
    private TextView menuItemName;
    private DrawerActivityViewModel drawerViewModel;


    public NavigationItemRenderer(DrawerActivityViewModel drawerViewModel) {
        this.drawerViewModel = drawerViewModel;
    }

    @Override
    protected void setUpView(View rootView) {
        this.rootView = rootView;
        this.menuItemIcon = rootView.findViewById(R.id.cell_navigation_menu_item_img);
        this.menuItemName = rootView.findViewById(R.id.cell_navigation_menu_item_txt_name);
    }

    @Override
    protected void hookListeners(View rootView) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerViewModel.handleNavigationItem(getContent());
                drawerViewModel.toggleDrawer();
            }
        });
    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_navigation_menu_item, parent, false);
    }

    @Override
    public void render() {
        NavigationItem navigationItem = getContent();

        Link link = navigationItem.link;
        menuItemName.setText(link.getPath());

        menuItemIcon.setImageResource(R.color.colorAccent);

        if (navigationItem.isSelectable() && navigationItem.isSelected()) {
            rootView.setBackgroundColor(Color.DKGRAY);

        } else {
            rootView.setBackgroundColor(Color.LTGRAY);
        }
    }

}
