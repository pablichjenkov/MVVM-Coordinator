package com.intervalintl.coordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.util.ObjectsCompat;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Don't keep references of Views, Activities or Fragments in this class, this class persist
 * Configuration Changes.
 * */
public abstract class Coordinator<InputInjector> {

    private String id;
    protected ArrayList<Coordinator> children;


    public Coordinator(String id) {
        this.id = id;
        this.children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }


    // region: Coordinator API

    /**
     * This method is originated from the activity that hosts the root coordinator all the way down
     * through the Coordinators tree. Upon invocation, coordinators must do the following
     * 1- Must update its internal dependencies with the new ones coming in the Input.
     * 2- Update children coordinators with the new Input for each children.
     * */
    public abstract void onInputStateChange(InputInjector inputInjector);

    /**
     * This method will be called from parent coordinators, signalling that this coordinator is able
     * to start processing its stage machine.
     * */
    public abstract void start();

    protected <T extends Coordinator> T getChildById(String childId) {
        for (Coordinator coordinator : children) {
            if (coordinator.getId().equals(childId)) {
                return (T)coordinator;
            }
        }
        return null;
    }

    /* package */ <T extends Coordinator> T depthFirstSearchById(String id) {

        if (this.id.equals(id)) {
            return (T)this;
        }

        if (children != null) {
            for (Coordinator childCoordinator : children) {
                Coordinator result = childCoordinator.depthFirstSearchById(id);
                if (result != null) {
                    return (T)result;
                }
            }
        }

        return null;
    }

    final protected void registerChildForActivityEvents(Coordinator coordinator) {
        if (!children.contains(coordinator)) {
            children.add(coordinator);
        }
    }

    final protected void unregisterChildForActivityEvents(String childId) {
        Coordinator coordinator = getChildById(childId);
        if (coordinator != null) {
            children.remove(coordinator);
        }
    }

    /**
     * This method is called by the system when the Coordinator Host Activity is about to be killed.
     * Nullify all the resources like ViewModel references and Activity, Fragment or View references
     * here to avoid memory leaks.
     * */
    @CallSuper
    public void onCleared() {
        // Clean children coordinator
        for (Coordinator coordinator : children) {
            coordinator.onCleared();
        }
        children.clear();
    }

    // endregion


    // region: Activity Lifecycle Events

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        for (Coordinator coordinator : children) {
            coordinator.onCreate(savedInstanceState);
        }
    }

    @CallSuper
    public void onResume() {
        for (Coordinator coordinator : children) {
            coordinator.onResume();
        }
    }

    @CallSuper
    public void onPause() {
        for (Coordinator coordinator : children) {
            coordinator.onPause();
        }
    }

    /**
     * By default this method distribute the result to its children until one of them consume it.
     * For a different behavior override it.
     * */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean consumed = false;
        Iterator<Coordinator> iterator = children.iterator();

        while (iterator.hasNext() && !consumed) {
            consumed = iterator.next().onActivityResult(requestCode, resultCode, data);
        }

        return consumed;
    }

    @CallSuper
    public void onDestroy() {
        for (Coordinator coordinator : children) {
            coordinator.onDestroy();
        }
    }

    /**
     * By default this method distribute the event to its children until one of them consume it.
     * For a different behavior override it.
     * */
    public boolean onBackPressed() {
        boolean consumed = false;
        Iterator<Coordinator> iterator = children.iterator();

        while (iterator.hasNext() && !consumed) {
            consumed = iterator.next().onBackPressed();
        }

        return consumed;
    }

    // endregion


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Coordinator) {
            return ((Coordinator)obj).getId().equals(this.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(id);
    }

}
