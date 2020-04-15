package com.example.covid.rrmeasure;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RRMeasureViewModel extends ViewModel {

    static int ITEM_COUNT_FOR_MEDIAN_CALC = 4;
    static int THRESHOLD_CONSISTENCY_PERCENTAGE = 20;
    static int MAX_TIME_TO_CALC_IN_MILLIS = 20000;
    static int MAX_TAPS_ALLOWED = 15;

    private int mTapCount;
    private long mPreviousTapTime;
    private boolean mIsCalculatingRR;
    private ArrayList<Long> mTimerList = new ArrayList<>();

    int getTapCount() {
        return mTapCount;
    }

    public void setTapCount(int mTapCount) {
        this.mTapCount = mTapCount;
    }

    void incrementTapCount() {
        this.mTapCount++;
    }

    ArrayList<Long> getTimerList() {
        return mTimerList;
    }

    void setTimerList(ArrayList<Long> mTimerList) {
        this.mTimerList = mTimerList;
    }

    void addTimerListItem(long item) {
        this.mTimerList.add(item);
    }

    long getPreviousTapTime() {
        return mPreviousTapTime;
    }

    void setPreviousTapTime(long mPreviousTapTime) {
        this.mPreviousTapTime = mPreviousTapTime;
    }

    boolean isCalculatingRR() {
        return mIsCalculatingRR;
    }

    void setIsCalculatingRR(boolean mIsCalculatingRR) {
        this.mIsCalculatingRR = mIsCalculatingRR;
    }

    void invalidateData() {
        this.mTapCount = 0;
        this.mTimerList = new ArrayList<>();
        this.mIsCalculatingRR = false;
    }
}
