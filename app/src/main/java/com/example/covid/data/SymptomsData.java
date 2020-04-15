package com.example.covid.data;

public class SymptomsData extends Data {
    private static SymptomsData singleInstance;

    private SymptomsData() {}

    public static SymptomsData getInstance() {
        if (singleInstance == null) {
            singleInstance = new SymptomsData();
        }
        return singleInstance;
    }

    public boolean dryCough;
    public static int wDryCough = MED;

    public boolean lossOfSmell;
    public static int wLossOfSmell = LOW;

    public boolean soreThroat;
    public static int wSoreThroat = MED;

    public boolean weakness;
    public static int wWeakness = MED;

    public boolean appetiteChaneg;
    public static int wAppetiteChange = LOW;

    @Override
    public int calculateScore() {
        int score = 0;
        score+= (dryCough) ? wDryCough : 0;
        score+= (lossOfSmell) ? wLossOfSmell : 0;
        score+= (soreThroat) ? wSoreThroat : 0;
        score+= (weakness) ? wWeakness : 0;
        score+= (appetiteChaneg) ? wAppetiteChange : 0;
        return score;
    }

    @Override
    int getId() {
        return 7;
    }

    @Override
    int getMin() {
        return 0;
    }

    @Override
    int getMax() {
        return 40;
    }
}
