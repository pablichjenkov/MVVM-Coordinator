package com.intervalintl.voltus.navigation;

import com.intervalintl.voltus.Link;
import java.util.List;


public class NavigationItem {


    public List<NavigationItem> mChildren;
    public Link link;
    public int adapterPosition;
    private boolean isSelectable;
    private boolean isSelected;

    public List<NavigationItem> getChildren() {
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
