package com.example.covid.data;

public class AgeData extends Data {
    private static AgeData singleInstance;

    private AgeData() {}

    public static AgeData getInstance() {
        if (singleInstance == null) {
            singleInstance = new AgeData();
        }
        return singleInstance;
    }

    public int age;

    public static int wLessThan12 = LOW;
    public static int w12to60 = MED;
    public static int wAbove60 = HIGH;

    @Override
    public int calculateScore() {
        if(age < 12) {
            return wLessThan12;
        } else if (age <= 60) {
            return w12to60;
        } else {
            return wAbove60;
        }
    }

    @Override
    int getId() {
        return 1;
    }

    @Override
    int getMin() {
        return 5;
    }

    @Override
    int getMax() {
        return 15;
    }
}
