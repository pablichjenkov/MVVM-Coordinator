package com.intervalintl.voltus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/* package */ class LinkHandlerImpl implements LinkHandler {


    private static final String KEY_CURRENT_LINK = "key_current_link";
    protected Activity mActivity;
    protected FragmentManager mFragmentManager;
    protected Link mCurLink;
    protected int mFragmentContainerResId;


    /* package */ LinkHandlerImpl(Activity activity, Bundle savedInstance, FragmentManager fragmentManager
            , @IdRes int fragmentContainerResId) {

        mActivity = activity;
        mFragmentManager = fragmentManager;
        mFragmentContainerResId = fragmentContainerResId;

        if (savedInstance != null) {
            mCurLink = (Link) savedInstance.getSerializable(KEY_CURRENT_LINK);
        }

    }

    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putSerializable(KEY_CURRENT_LINK, mCurLink);
    }

    public void onDestroy() {
        //Router.unregisterHandler(this);
    }

    @Override
    public void handleLink(Link nextLink) {

        //Router.dispatchLinkInterception(mCurLink, nextLink);

        if (nextLink.type.equals(Link.Type.Fragment)) {
            handleFragmentLink(nextLink);

        } else if (nextLink.type.equals(Link.Type.Activity)) {
            launchActivityFromLink(nextLink);
        }

    }

    @Override
    public Link getCurrentLink() {
        return mCurLink;
    }

    @Override
    public boolean handleBackPress() {

        // if there is not an active link then unregister and return.
        if (mCurLink == null) {
            return false;
        }

        Fragment curFragment = mFragmentManager.findFragmentByTag(mCurLink.fragmentTag);
        boolean childRouteConsumeBack = false;

        // Give higher priority to the current FragmentComponent to handle the back press event first.
        if (curFragment instanceof BackPressHandler) {
            childRouteConsumeBack = ((BackPressHandler)curFragment).handleBackPress();

            if (childRouteConsumeBack) {
                return true;
            }
        }

        // If current FragmentComponent do not consume then dispatch to the global BackPressConsumer.
        if (!childRouteConsumeBack) {
            boolean presenterConsumeBack = false;// = Router.dispatchBackPressedToConsumer(this);

            if (presenterConsumeBack) {
                return true;
            }
        }

        return false;
    }

    protected void handleFragmentLink(Link nextLink) {

        if (DialogFragment.class.isAssignableFrom(nextLink.fragmentClass)) {
            Fragment fragment = Fragment.instantiate(mActivity, nextLink.fragmentClass.getName());
            ((DialogFragment)fragment).show(mFragmentManager, nextLink.fragmentTag);
            return;
        }

        if (mCurLink == null) {
            mCurLink = nextLink;
            addFirstFragmentRoute(nextLink);
            return;
        }

        // If next link has the fragmentTagId of the current link. Do nothing
        if (mCurLink.fragmentTag.equals(nextLink.fragmentTag)) {
            return;
        }

        // prefer hide - show over add - remove
        Fragment curFragment = mFragmentManager.findFragmentByTag(mCurLink.fragmentTag);
        Fragment nextFragment = mFragmentManager.findFragmentByTag(nextLink.fragmentTag);

        if (curFragment != null) {

            if (nextFragment != null) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.show(nextFragment).hide(curFragment).commitNow();
                mCurLink = nextLink;

            } else {

                nextFragment = Fragment.instantiate(mActivity
                        , nextLink.fragmentClass.getName());

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                fragmentTransaction
                        .add(mFragmentContainerResId, nextFragment, nextLink.fragmentTag)
                        .hide(curFragment)
                        .commitNow();

                mCurLink = nextLink;
            }

        }

    }

    private void addFirstFragmentRoute(Link nextLink) {
        ensureFragmentLinkHasValidTag(nextLink);
        Fragment fragment = Fragment.instantiate(mActivity, nextLink.fragmentClass.getName());
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(mFragmentContainerResId, fragment, nextLink.fragmentTag);
        fragmentTransaction.commitNow();
    }

    private void launchActivityFromLink(Link nextLink) {
        Intent launchActivityIntent = new Intent(mActivity, nextLink.activityClass);
        mActivity.startActivity(launchActivityIntent);
    }

    protected static void ensureFragmentLinkHasValidTag(Link route) {
        if (route.fragmentTag == null) {
            // TODO(pablo): Replace bellow throw by reporting to a callback.
            throw new RuntimeException("A Link of type Fragment must specify a " +
                    "fragmentTag attribute.");
        }
    }

}