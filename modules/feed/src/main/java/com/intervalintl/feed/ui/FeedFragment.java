package com.intervalintl.feed.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.intervalintl.coordinator.view.CoordinatorFragment;
import com.intervalintl.feed.FeedCoordinator;
import com.intervalintl.feed.R;
import io.reactivex.disposables.CompositeDisposable;


public class FeedFragment extends CoordinatorFragment<FeedCoordinator> {


    private View rootView;
    private FrameLayout dryCleaningContainer;
    private FrameLayout laundryItemContainer;
    private FrameLayout washFoldContainer;

    private FeedCoordinator feedCoordinator;
    private CompositeDisposable disposables;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        dryCleaningContainer = rootView.findViewById(R.id.fragment_home_dry_cleaning_container);
        washFoldContainer = rootView.findViewById(R.id.fragment_home_wash_fold_container);
        laundryItemContainer = rootView.findViewById(R.id.fragment_home_laundry_item_container);

        setListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //feedCoordinator.subscribe(productEventObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        disposables.clear();
    }

    @Override
    protected void onCoordinatorBound(FeedCoordinator coordinator) {
        feedCoordinator = coordinator;
    }

    private void setListeners() {

        dryCleaningContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //feedCoordinator.doDryCleaning();
            }
        });

        washFoldContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        laundryItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
/*
    private Observer<ProductEvent> feedEventObserver = new Observer<ProductEvent>() {
        @Override
        public void onSubscribe(Disposable d) {
            disposables.add(d);
        }

        @Override
        public void onNext(ProductEvent productEvent) {
            if (productEvent instanceof ProductEvent.ProductFetchSuccess) {
                //((ProductEvent.ProductFetchSuccess)productEvent).getPayload();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
*/
}
