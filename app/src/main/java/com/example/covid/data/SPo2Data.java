package com.example.covid.data;

public class SPo2Data extends Data {
    private static SPo2Data singleInstance;

    private SPo2Data() {}

    public static SPo2Data getInstance() {
        if (singleInstance == null) {
            singleInstance = new SPo2Data();
        }
        return singleInstance;
    }

    public int spo2;
    public static int wLessThan94 = LOW;
    public static int wAbove94= MED;

    @Override
    public int calculateScore() {
        if(spo2 < 94) {
            return wLessThan94;
        } else {
            return wAbove94;
        }
    }

    @Override
    int getId() {
        return 6;
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
