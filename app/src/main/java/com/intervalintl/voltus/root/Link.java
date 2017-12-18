package com.intervalintl.voltus.root;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import java.io.Serializable;


public class Link implements Serializable {

    public enum Type {
        Activity,
        Fragment
    }

    public String sectionId;
    private Class<? extends Fragment> fragmentClass;
    private transient Fragment fragment;
    public String fragmentTag;
    public Class<? extends Activity> activityClass;
    public Type type;


    public String getPath() {
        return fragmentTag != null ? fragmentTag : activityClass.getSimpleName();
    }

    /* package */ Link() {}

    /* package */ void setFragment(Fragment fragment) {
        this.fragment = fragment;
        this.fragmentClass = fragment.getClass();
    }

    public Fragment getFragment(Context context) {
        if (fragment == null && fragmentClass != null) {
            fragment = Fragment.instantiate(context, fragmentClass.getName());
        }

        return fragment;
    }

    public boolean isDialogFragment() {
        return DialogFragment.class.isAssignableFrom(fragmentClass);
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
        public Builder inSection(String sectionId) {
            mLink.sectionId = sectionId;
            return this;
        }

        /*public <V extends View> Builder toViewGroup(Class<V> viewClass) {
            mLink.activityClass = activityClass;
            return this;
        }*/

        /**
         *  Sets the next Fragment view to be enrouted.
         *  @param fragmentClass The equivalent to specify a Route Component in react-router. Pass
         *                       the class of the desired Fragment to be shown.
         *  @param routePath The equivalent to specify a Route Path in react-router, it must be
         *                     unique.
         * */
        public <F extends Fragment> Builder toRoute(Class<F> fragmentClass, String routePath) {
            mLink.fragmentClass = fragmentClass;
            mLink.fragmentTag = routePath;
            mLink.type = Type.Fragment;
            return this;
        }

        public <F extends Fragment> Builder toRoute(F fragmentInstance, String routePath) {
            mLink.setFragment(fragmentInstance);
            mLink.fragmentTag = routePath;
            mLink.type = Type.Fragment;
            return this;
        }

        /**
         *  Sets the next Activity view to be enrouted.
         *  @param activityClass The equivalent to specify a Route Component in react-router. Pass
         *                       the class of the desired Activity to be shown.
         * */
        public <A extends Activity> Builder toRoute(Class<A> activityClass) {
            mLink.activityClass = activityClass;
            mLink.type = Type.Activity;
            return this;
        }

        public Link build() {
            return mLink;
        }

    }

}