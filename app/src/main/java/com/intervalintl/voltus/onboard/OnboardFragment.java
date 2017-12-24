package com.intervalintl.voltus.onboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.root.FragmentComponent;
import com.intervalintl.voltus.viewmodel.BaseFragment;


public class OnboardFragment extends BaseFragment<OnboardViewModel> {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboard, container, false);
    }

    @Override
    public void onViewModelBound(OnboardViewModel viewModel) {

    }

}
