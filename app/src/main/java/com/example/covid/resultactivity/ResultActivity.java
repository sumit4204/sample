package com.example.covid.resultactivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid.R;
import com.example.covid.data.PersonInfo;
import com.github.anastr.speedviewlib.SpeedView;

public class ResultActivity extends AppCompatActivity {
    SpeedView mSpeedView;
    TextView mScoreTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        int score = PersonInfo.getInstance().getScore();
        int min = PersonInfo.getInstance().getMin();
        int max = PersonInfo.getInstance().getMax();

        mSpeedView = findViewById(R.id.speedView);
        int x = (int)(100.0 * (score - min) / (max - min));
        mSpeedView.speedPercentTo(x);
//        mScoreTV = findViewById(R.id.covid_score_tv);
//        mScoreTV.setText(score + "");
    }
}
