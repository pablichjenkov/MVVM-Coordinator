package com.intervalintl.voltus.viewmodel;

import com.intervalintl.voltus.root.BackPressHandler;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class Interactor implements BackPressHandler {

    private String tagId;
    private final List<Interactor> children = new CopyOnWriteArrayList<>();
    private Interactor curInteractor;


    public Interactor(String tagId) {
        this.tagId = tagId;
    }

    public abstract void act();
    public abstract void shut();

    public String getTagId() {
        return tagId;
    }

}
