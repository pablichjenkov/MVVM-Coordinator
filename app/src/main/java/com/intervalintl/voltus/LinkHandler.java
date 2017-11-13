package com.intervalintl.voltus;

public interface LinkHandler extends BackPressHandler {
    void handleLink(Link link);
    Link getCurrentLink();
}
