package com.intervalintl.coordinator.view.delegate;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;


public class CoordinatorViewState extends View.BaseSavedState {

    public static final Parcelable.Creator<CoordinatorViewState> CREATOR =
            new Parcelable.Creator<CoordinatorViewState>() {

                public CoordinatorViewState createFromParcel(Parcel in) {
                    return new CoordinatorViewState(in);
                }

                public CoordinatorViewState[] newArray(int size) {
                    return new CoordinatorViewState[size];
                }
            };

    public String coordinatorId;


    CoordinatorViewState(Parcelable superState) {
        super(superState);
    }

    private CoordinatorViewState(Parcel in) {
        super(in);
        coordinatorId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(coordinatorId);
    }

}