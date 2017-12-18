package com.intervalintl.voltus.root;

public interface LinkHandler extends BackPressHandler {
    void handleLink(Link link);
    Link getCurrentLink();
}
