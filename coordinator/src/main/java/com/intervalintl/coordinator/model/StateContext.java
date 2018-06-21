package com.intervalintl.coordinator.model;

import java.util.ArrayList;


public final class StateContext {

    private ArrayList<StateProvider> stateProviderList;


    public StateContext() {
        this.stateProviderList = new ArrayList<>();
    }


    public boolean registerStateProvider(StateProvider stateProvider) {

        for (StateProvider existingStateProvider : stateProviderList) {
            if (existingStateProvider.getId().equals(stateProvider.getId())) {
                return false;
            }
        }

        return stateProviderList.add(stateProvider);
    }

    public <T extends StateProvider> T getStateProvider(Class<T> serviceClass, String serviceId) {
        StateProvider result = null;

        for (StateProvider stateProvider : stateProviderList) {
            if (serviceClass.isAssignableFrom(stateProvider.getClass())
                    && stateProvider.getId().equals(serviceId)) {

                result = stateProvider;
                break;
            }
        }

        return (T)result;
    }

}
