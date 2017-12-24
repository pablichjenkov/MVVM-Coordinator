package com.intervalintl.voltus.root;

import android.support.v4.app.FragmentManager;


public interface Router extends BackPressHandler {
    void handleLink(Link link);
    Link getCurrentLink();
    FragmentManager getFragmentManager();
}
