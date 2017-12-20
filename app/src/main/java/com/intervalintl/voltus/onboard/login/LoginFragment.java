package com.intervalintl.voltus.onboard.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.intervalintl.voltus.R;
import com.intervalintl.voltus.viewmodel.BaseFragment;
import com.intervalintl.voltus.viewmodel.ViewTreeModelViewBinder;


public class LoginFragment extends BaseFragment<LoginViewModel> {

    private ViewGroup rootView;
    private ViewTreeModelViewBinder<ViewGroup, LoginViewModel> viewTreeBinder;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().loginSuccess();
            }
        });

        viewTreeBinder = new ViewTreeModelViewBinder<>(rootView, getViewModel());
        return rootView;
    }

}
