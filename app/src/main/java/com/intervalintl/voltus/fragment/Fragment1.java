package com.intervalintl.voltus.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.root.FragmentComponent;
import com.intervalintl.voltus.R;


public class Fragment1 extends FragmentComponent {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment1, container, false);
    }

}
