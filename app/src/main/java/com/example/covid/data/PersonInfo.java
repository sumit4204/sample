package com.example.covid.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;

public class PersonInfo {

    private static PersonInfo single_instance;

    @NonNull
    private String name;
    private Boolean isMale;
    private String mobileNumber;

    private PersonInfo() {
    }

    public static PersonInfo getInstance()
    {
        if (single_instance == null)
            single_instance = new PersonInfo();

        return single_instance;
    }

    private HashSet<Data> Datalist = new HashSet<>();

    public void addToDataList(Data data) {
        Datalist.add(data);
    }

    public void removeFromDataList(Data data) {
        Datalist.remove(data);
    }

    public void addData(int position, boolean isYesClicked) {
        switch (position) {
            case 0 : {
                TravelExposure travelExposure = TravelExposure.getInstance();
                travelExposure.travelHistory = isYesClicked;
                addToDataList(travelExposure);
                break;
            }
            case 1 : break;
            case 2 : {
                TravelExposure travelExposure = TravelExposure.getInstance();
                travelExposure.contactTrace = isYesClicked;
                addToDataList(travelExposure);
                break;
            }
            case 3 : {
                SymptomsData dryCough = SymptomsData.getInstance();
                dryCough.dryCough = isYesClicked;
                addToDataList(dryCough);
                break;
            }
            case 4 : {
                SymptomsData soreThroat = SymptomsData.getInstance();
                soreThroat.soreThroat = isYesClicked;
                addToDataList(soreThroat);
                break;
            }
            case 5 : {
                AdditionalSymptomsData shortnessOfBreath = AdditionalSymptomsData.getInstance();
                shortnessOfBreath.breathless = isYesClicked;
                addToDataList(shortnessOfBreath);
            }
        }
    }

    public int getScore() {
        int score = 0;
        for(Data data : Datalist) {
            score += data.calculateScore();
        }
        return score;
    }

    public int getMin() {
        int min = 0;
        for(Data data : Datalist) {
            min += data.getMin();
        }
        return min;
    }

    public int getMax() {
        int max = 0;
        for(Data data : Datalist) {
            max += data.getMax();
        }
        return max;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
