package com.example.covid.data;

public class HeartRate extends Data {
    private static HeartRate singleInstance;

    private HeartRate() {}

    public static HeartRate getInstance() {
        if (singleInstance == null) {
            singleInstance = new HeartRate();
        }
        return singleInstance;
    }

    public int hr;
    public static int wLessThan120 = LOW;
    public static int wAbove120= MED;

    @Override
    public int calculateScore() {
        if(hr < 120) {
            return wLessThan120;
        } else {
            return wAbove120;
        }
    }

    @Override
    int getId() {
        return 3;
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
