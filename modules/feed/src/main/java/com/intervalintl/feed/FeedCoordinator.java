package com.intervalintl.feed;

import com.interval.common.Constants;
import com.interval.common.login.UserManager;
import com.intervalintl.coordinator.Coordinator;
import com.intervalintl.coordinator.view.CoordinatorScreenManager;
import com.intervalintl.feed.ui.FeedFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;


public class FeedCoordinator extends Coordinator<FeedBuilder.Component> {


    public enum Stage {
        Idle,
        LaundryTypeSelection,
        DryCleaning,
        DryCleaningProductSelected,
        WashFold,
        LaundryItem
    }

    @Inject
    CoordinatorScreenManager screenManager;

    @Inject
    UserManager userManager;

    //@Inject
    //ProductManager productManager;

    //private HomeViewModel homeViewModel;
    private Stage curStage = Stage.Idle;
    //private Subject<ProductEvent> publisher;
    private CompositeDisposable disposables;


    public FeedCoordinator(String tagId) {
        super(tagId);
        //publisher = PublishSubject.create();
        disposables = new CompositeDisposable();
        //homeViewModel = new HomeViewModel();
    }

    @Override
    public boolean onBackPressed() {

        if (curStage == Stage.LaundryTypeSelection) {
            return false;
        }
        else if (curStage == Stage.DryCleaning) {
            //presentLaundryTypeSelection();
            return true;
        }
        else if (curStage == Stage.DryCleaningProductSelected) {
            //presentLaundryTypeSelection();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onInputStateChange(FeedBuilder.Component feedComponent) {
        feedComponent.inject(this);
    }

    @Override
    public void start() {
        if (curStage == Stage.Idle || curStage == Stage.LaundryTypeSelection) {
            presentLaundryTypeSelection();
        }
        else if (curStage == Stage.DryCleaning) {
            //presentDryCleaning();
        }
    }

    private void presentLaundryTypeSelection() {
        curStage = Stage.LaundryTypeSelection;

        FeedFragment feedFragment = new FeedFragment();
        feedFragment.setCoordinatorId(getId());

        screenManager.setView(feedFragment, Constants.FEED_FRAGMENT_TAG);
    }

/*
    public void subscribe(Observer<ProductEvent> observer) {
        publisher.subscribe(observer);
    }

    // region: View Events

    public void presentDryCleaning() {

        curStage = Stage.DryCleaning;

        DryCleaningFragment dryCleaningFragment = new DryCleaningFragment();
        dryCleaningFragment.coordinatorId = Constants.COORDINATOR_HOME_ID;
        dryCleaningFragment.viewModelClass = HomeViewModel.class;
        dryCleaningFragment.viewModelId = "homeViewModelDefault";

        Link link = Link.Builder.newLink()
                .toRoute(dryCleaningFragment, "DryCleaningFragment")
                .build();

        screenManager.handleLink(link);
    }

    public void presentShoppingCart() {

        //curStage = Stage.DryCleaning;

        Link link = Link.Builder.newLink()
                .toRoute(CheckoutActivity.class)
                .build();

        screenManager.handleLink(link);
    }

    public void productSelected(ProductTemplate product) {

        //curStage = Stage.DryCleaningProductSelected;

        ProductSelectedFragmentDialog productSelectedDialog = new ProductSelectedFragmentDialog();
        productSelectedDialog.coordinatorId = Constants.COORDINATOR_HOME_ID;
        productSelectedDialog.viewModelClass = HomeViewModel.class;
        productSelectedDialog.viewModelId = "homeViewModelDefault";

        Link link = Link.Builder.newLink()
                .toRoute(productSelectedDialog, "ProductSelectedFragmentDialog")
                .build();

        screenManager.handleLink(link);
    }

    public void productDialogDismissed() {
        screenManager.dispatchDialogDismiss();
    }
*/
    // endregion


    /*private Observer<ProductEvent> productEventObserver = new Observer<ProductEvent>() {
        @Override
        public void onSubscribe(Disposable d) {
            disposables.add(d);
        }

        @Override
        public void onNext(ProductEvent productEvent) {
            // FIXME: Find a different approach to forward the events coming from the manager
            publisher.onNext(productEvent);
        }

        @Override
        public void onError(Throwable e) {
            publisher.onError(e);
        }

        @Override
        public void onComplete() {

        }
    };*/

}
