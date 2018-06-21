package com.intervalintl.navigation.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.intervalintl.navigation.R;


public class TopBar extends FrameLayout {


    private Context mContext;

    public TopBar(@NonNull Context context) {
        super(context);
        init();
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mContext = getContext();
        LayoutInflater.from(mContext).inflate(R.layout.widget_top_bar, this, true);
        setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }

}
