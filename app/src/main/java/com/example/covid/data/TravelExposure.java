package com.example.covid.data;

public class TravelExposure extends Data {
    private static TravelExposure singleInstance;

    private TravelExposure() {}

    public static TravelExposure getInstance() {
        if (singleInstance == null) {
            singleInstance = new TravelExposure();
        }
        return singleInstance;
    }

    public boolean travelHistory;
    public static int wTravelHistory = MED;

    public boolean contactTrace;
    public static int wContactTrace = HIGH;

    @Override
    public int calculateScore() {
        int score = 0;
        score+= (travelHistory) ? wTravelHistory : 0;
        score+= (contactTrace) ? wContactTrace : 0;
        return score;
    }

    @Override
    int getId() {
        return 9;
    }

    @Override
    int getMin() {
        return 0;
    }

    @Override
    int getMax() {
        return 25;
    }
}
