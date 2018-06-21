package com.intervalintl.navigation.ui.drawer.menu;

import android.support.annotation.Keep;
import java.util.List;


@Keep
public class MenuItem {


    public List<MenuItem> mChildren;
    //public Link link;
    public String navCoordinatorId;
    public int adapterPosition;
    private boolean isSelectable;
    private boolean isSelected;

    public List<MenuItem> getChildren() {
        return mChildren;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public boolean isSelectable() {
        return isSelectable;
    }
}
