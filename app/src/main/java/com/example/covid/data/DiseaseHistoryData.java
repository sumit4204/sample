package com.example.covid.data;

public class DiseaseHistoryData extends Data {
    private static DiseaseHistoryData singleInstance;

    private DiseaseHistoryData() {}

    public static DiseaseHistoryData getInstance() {
        if (singleInstance == null) {
            singleInstance = new DiseaseHistoryData();
        }
        return singleInstance;
    }

    public boolean Diabetes;
    public static int wDiabetes = MED;

    public boolean HighBloodPressure;
    public static int wHighBloodPressure = MED;

    public boolean HeartDisease;
    public static int wHeartDisease = MED;

    public boolean KidneyDisease;
    public static int wKidneyDisease = MED;

    public boolean LungDisease;
    public static int wLungDisease = MED;

    public boolean Stroke;
    public static int wStroke = LOW;

    public boolean ReducedImmunity;
    public static int mReducedImmunity = HIGH;


    @Override
    public int calculateScore() {
        int score = 0;
        score+= (Diabetes) ? wDiabetes : 0;
        score+= (HighBloodPressure) ? wHighBloodPressure : 0;
        score+= (HeartDisease) ? wHeartDisease : 0;
        score+= (KidneyDisease) ? wKidneyDisease : 0;
        score+= (LungDisease) ? wLungDisease : 0;
        score+= (Stroke) ? wStroke : 0;
        score+= (ReducedImmunity) ? mReducedImmunity : 0;
        return score;
    }

    @Override
    int getId() {
        return 2;
    }

    @Override
    int getMin() {
        return 0;
    }

    @Override
    int getMax() {
        return 70;
    }
}
