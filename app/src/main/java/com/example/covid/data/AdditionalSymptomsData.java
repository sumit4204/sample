package com.example.covid.data;

public class AdditionalSymptomsData extends Data {
    private static AdditionalSymptomsData singleInstance;

    private AdditionalSymptomsData() {}

    public static AdditionalSymptomsData getInstance() {
        if (singleInstance == null) {
            singleInstance = new AdditionalSymptomsData();
        }
        return singleInstance;
    }

    public boolean severeCough;
    public static int wSevereCough = HIGH;

    public boolean breathless;
    public static int wBreathless = HIGH;

    public boolean drowsiness;
    public static int wDrowsiness = LOW;

    public boolean painInChest;
    public static int wPainInChest = MED;

    public boolean severeWeakness;
    public static int wSevereWeakness = HIGH;


    @Override
    public int calculateScore() {
        int score = 0;
        score+= (severeCough) ? wSevereCough : 0;
        score+= (breathless) ? wBreathless : 0;
        score+= (drowsiness) ? wDrowsiness : 0;
        score+= (painInChest) ? wPainInChest : 0;
        score+= (severeWeakness) ? wSevereWeakness : 0;
        return score;
    }

    @Override
    int getId() {
        return 0;
    }

    @Override
    int getMin() {
        return 0;
    }

    @Override
    int getMax() {
        return 60;
    }
}
