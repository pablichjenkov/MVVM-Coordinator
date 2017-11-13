package com.intervalintl.voltus;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import java.io.Serializable;


public class Link implements Serializable {

    public enum Type {
        Activity,
        Fragment
    }

    public String routerId;
    public Class<? extends Fragment> fragmentClass;
    public Fragment fragment;
    public String fragmentTag;
    public Class<? extends Activity> activityClass;
    public Type type;


    public String getPath() {
        return fragmentTag != null ? fragmentTag : activityClass.getSimpleName();
    }

    /* package */ Link() {}

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment(Context context) {
        if (fragment == null) {
            fragment = Fragment.instantiate(context, fragmentClass.getName());
        }

        return fragment;
    }


    public static class Builder {

        private Link mLink;

        /* package */ Builder(Link link) {
            mLink = link;
        }

        public static Builder newLink() {
            return new Builder(new Link());
        }


        /** In the weird case that more than one RouterController like BrowserRouter or HashRouter
         *  be specified in the same screen. This method will allow to specify which one to use.
         * */
        public Builder inSection(String routerId) {
            mLink.routerId = routerId;
            return this;
        }

        /*public <V extends View> Builder toViewGroup(Class<V> viewClass) {
            mLink.activityClass = activityClass;
            return this;
        }*/

        /**
         *  Sets the next Fragment view to be enrouted.
         *  @param componentClass The equivalent to specify a Route Component in react-router. Pass
         *                       the class of the desired Fragment to be shown.
         *  @param routePath The equivalent to specify a Route Path in react-router, it must be
         *                     unique.
         * */
        public <F extends Fragment> Builder toRoute(Class<F> componentClass, String routePath) {
            mLink.fragmentClass = componentClass;
            mLink.fragmentTag = routePath;
            mLink.type = Type.Fragment;
            return this;
        }

        /**
         *  Sets the next Activity view to be enrouted.
         *  @param componentClass The equivalent to specify a Route Component in react-router. Pass
         *                       the class of the desired Activity to be shown.
         * */
        public <A extends Activity> Builder toRoute(Class<A> componentClass) {
            mLink.activityClass = componentClass;
            mLink.type = Type.Activity;
            return this;
        }

        public Link build() {
            return mLink;
        }

    }

}