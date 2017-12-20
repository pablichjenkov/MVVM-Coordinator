package com.intervalintl.voltus.home;

import android.support.v7.app.AppCompatActivity;

import com.intervalintl.voltus.onboard.splash.SplashFragment;
import com.intervalintl.voltus.root.LinkHandler;


public class HomeComponent {

    private Object listener;
    private LinkHandler linkHandler;
    private SplashFragment splashFragment;
    private AppCompatActivity activity;

    public HomeComponent(Object listener, LinkHandler linkHandler, AppCompatActivity activity) {
        this.listener = listener;
        this.linkHandler = linkHandler;
        this.activity = activity;
    }

}
