package com.example.covid.rrmeasure;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid.R;

public class RRMeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rr_measure_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RRFragment.newInstance())
                    .commitNow();
        }
    }
}
