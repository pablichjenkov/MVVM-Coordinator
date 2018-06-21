package com.intervalintl.navigation.ui.drawer.menu;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.intervalintl.navigation.NavigationCoordinator;
import com.intervalintl.navigation.R;
import com.pedrogomez.renderers.Renderer;


public class MenuItemRenderer extends Renderer<MenuItem> {


    private View rootView;
    private ImageView menuItemIcon;
    private TextView menuItemName;
    private NavigationCoordinator navigationCoordinator;


    public MenuItemRenderer(NavigationCoordinator navigationCoordinator) {
        this.navigationCoordinator = navigationCoordinator;
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
                navigationCoordinator.handleNavigationItem(getContent());
                navigationCoordinator.toggleDrawer();
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
        MenuItem menuItem = getContent();

        menuItemName.setText(menuItem.navCoordinatorId);

        menuItemIcon.setImageResource(R.color.colorAccent);

        if (menuItem.isSelectable() && menuItem.isSelected()) {
            rootView.setBackgroundColor(Color.DKGRAY);

        } else {
            rootView.setBackgroundColor(Color.LTGRAY);
        }
    }

}
