package com.hbbsolution.maid.home.job_near_by_new_version.model;

/**
 * Created by buivu on 11/09/2017.
 */

public class FilterModelSingleton {
    private static FilterModelSingleton instance;
    private FilterModel mFilterModel;
    private boolean isSaved = false;

    private FilterModelSingleton() {
        mFilterModel = new FilterModel();
    }

    public static FilterModelSingleton getInstance() {
        if (instance == null) {
            instance = new FilterModelSingleton();
        }
        return instance;
    }

    public FilterModel getFilterModel() {
        return mFilterModel;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void saveFilterModel(FilterModel filterModel) {
        isSaved = true;
        mFilterModel = filterModel;
    }

    public void saveLocation(Double mLat, Double mLng) {
        isSaved = true;
        mFilterModel.setLat(mLat);
        mFilterModel.setLng(mLng);
    }
}
