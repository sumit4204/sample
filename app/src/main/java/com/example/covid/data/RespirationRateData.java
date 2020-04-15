package com.example.covid.data;

public class RespirationRateData extends Data {
    private static RespirationRateData singleInstance;

    private RespirationRateData() {}

    public static RespirationRateData getInstance() {
        if (singleInstance == null) {
            singleInstance = new RespirationRateData();
        }
        return singleInstance;
    }

    public int rr;
    public static int wLessThan20 = LOW;
    public static int w20to30 = MED;
    public static int wAbove30 = MED;

    @Override
    public int calculateScore() {
        if(rr < 20) {
            return wLessThan20;
        } else if (rr <= 30) {
            return w20to30;
        } else {
            return wAbove30;
        }
    }

    @Override
    int getId() {
        return 5;
    }

    @Override
    int getMin() {
        return 5;
    }

    @Override
    int getMax() {
        return 10;
    }


}
