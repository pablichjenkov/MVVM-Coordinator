package com.intervalintl.voltus.root;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/* package */ class RouterImplNoReuse extends RouterImpl {

    /* package */ RouterImplNoReuse(Activity activity, Bundle savedInstance, FragmentManager fragmentManager
            , int fragmentContainerResId, String routerId) {

        super(activity, savedInstance, fragmentManager, fragmentContainerResId);
    }

    protected void handleFragmentLink(Link nextLink) {

        if (nextLink.isDialogFragment()) {
            Fragment fragment = nextLink.getFragment(mActivity);
            ((DialogFragment)fragment).show(mFragmentManager, nextLink.fragmentTag);
            return;
        }

        if (mCurLink != null) {
            // If next link has the fragmentTagId of the current link. Do nothing
            if (mCurLink.fragmentTag.equals(nextLink.fragmentTag)) {
                return;
            }
        }

        ensureFragmentLinkHasValidTag(nextLink);
        Fragment fragment = nextLink.getFragment(mActivity);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(mFragmentContainerResId, fragment, nextLink.fragmentTag);
        fragmentTransaction.commitNow();
        mCurLink = nextLink;

    }

}
