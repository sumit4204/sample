package com.example.covid.data;

public class TempData extends Data {
    private static TempData singleInstance;

    private TempData() {}

    public static TempData getInstance() {
        if (singleInstance == null) {
            singleInstance = new TempData();
        }
        return singleInstance;
    }

    public float temp;
    public static int wLessThan36 = LOW;
    public static int w36to37 = LOW;
    public static int w37to39 = MED;
    public static int wAbove39 = HIGH;

    @Override
    public int calculateScore() {
        if(temp < 36) {
            return wLessThan36;
        } else if (temp <= 37) {
            return w36to37;
        } else if (temp <= 39 ) {
            return w37to39;
        } else {
            return wAbove39;
        }
    }

    @Override
    int getId() {
        return 8;
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
