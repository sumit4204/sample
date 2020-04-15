package com.example.covid.data;

public class ProgressData extends Data {
    private static ProgressData singleInstance;

    private ProgressData() {}

    public static ProgressData getInstance() {
        if (singleInstance == null) {
            singleInstance = new ProgressData();
        }
        return singleInstance;
    }

    public ProgressDetails progressDetails = ProgressDetails.IMPROVED;

    enum ProgressDetails {
        IMPROVED,
        NO_CHANGES,
        WORSENED,
        WORSENED_CONSIDERABLY
    }

    public static int wImproved = MED;
    public static int wNoChanges = MED;
    public static int wWorsened = HIGH;
    public static int wCritical = HIGH;

    @Override
    public int calculateScore() {
        if(progressDetails == ProgressDetails.IMPROVED) {
            return wImproved;
        } else if( progressDetails == ProgressDetails.NO_CHANGES) {
            return wNoChanges;
        } else if( progressDetails == ProgressDetails.WORSENED) {
            return wWorsened;
        } else {
            return wCritical;
        }
    }

    @Override
    int getId() {
        return 4;
    }

    @Override
    int getMin() {
        return 10;
    }

    @Override
    int getMax() {
        return 15;
    }

}
