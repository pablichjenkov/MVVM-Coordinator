package com.intervalintl.coordinator.view;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Implementers of this interface will be in charge of rendering whatever view the coordinator wants
 * to present.
 */
public interface CoordinatorScreenManager {
    <F extends Fragment & CoordinatedView> void setView(F fragment, String fragmentId);
    <V extends View & CoordinatedView> void setView(V view);
}
