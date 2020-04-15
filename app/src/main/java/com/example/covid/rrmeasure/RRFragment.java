package com.example.covid.rrmeasure;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid.R;
import com.example.covid.data.PersonInfo;
import com.example.covid.data.RespirationRateData;
import com.example.covid.resultactivity.ResultActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RRFragment extends Fragment {

    private RRMeasureViewModel mViewModel;

    private CircleView mTapView;
    private Button mResetButton;
    private TextView mTextView;
//    private CircleMenu mCircleMenu;
//    private View mAnimateView;
    private ProgressBar mProgressBar;
    private LinearLayout mIndicatorContainer;
    private Button mNextButton;

    private ArrayList<CardView> mIndicatorList;

    CountDownTimer mOneMinTimer = new CountDownTimer(RRMeasureViewModel.MAX_TIME_TO_CALC_IN_MILLIS,
            100) {
        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) ((RRMeasureViewModel.MAX_TIME_TO_CALC_IN_MILLIS - millisUntilFinished)
                    * 1000 / RRMeasureViewModel.MAX_TIME_TO_CALC_IN_MILLIS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mProgressBar.setProgress(progress, true);
            } else {
                mProgressBar.setProgress(progress);
            }
        }

        @Override
        public void onFinish() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mProgressBar.setProgress(1000, true);
            } else {
                mProgressBar.setProgress(1000);
            }
            String text = "";
            int tapCount = mViewModel.getTapCount();
            if (tapCount > 3) {
                text += "Inconsistent Taps";
            } else if (tapCount < 1) {
                text += "No Taps";
            } else {
                text += "Insufficient Taps";
            }
            text += "\nPLease try again";
            stopRRCalculation(text, false);
        }
    };

    public static RRFragment newInstance() {
        return new RRFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.rr_measure_fragment, container, false);
        mTextView = root.findViewById(R.id.rr_text_view);
        mTapView = root.findViewById(R.id.tap_button);
        mResetButton = root.findViewById(R.id.reset_button);
        mProgressBar = root.findViewById(R.id.progress_circular);
        mIndicatorContainer = root.findViewById(R.id.indicator_container);
        mNextButton = root.findViewById(R.id.rr_next_button);
        setIndicatorIcons();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RRMeasureViewModel.class);
        final Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mTapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(20);
                if (mViewModel.getTapCount() >= RRMeasureViewModel.MAX_TAPS_ALLOWED) {
                    stopRRCalculation("Max Taps\n Please try again", false);
                    return;
                } else if (mViewModel.getTapCount() == 0) {
                    mViewModel.setPreviousTapTime(System.currentTimeMillis());
                    mViewModel.setIsCalculatingRR(true);
                    mOneMinTimer.start();
                    mResetButton.setVisibility(View.VISIBLE);
                    startRRCalculation();
                } else {
                    long currentTapTime = System.currentTimeMillis();
                    mViewModel.addTimerListItem(currentTapTime - mViewModel.getPreviousTapTime());
                    mViewModel.setPreviousTapTime(currentTapTime);
                    calculateAverageRR();
                }
                mIndicatorList.get(mViewModel.getTapCount()).setCardBackgroundColor(getResources().
                        getColor(R.color.indicator_icon_selected));
                mViewModel.incrementTapCount();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetActivity();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculateAverageRR() {
        ArrayList<Long> timerList = mViewModel.getTimerList();
        List<Long> itemListForMedian = timerList.subList(Math.max(timerList.size() -
                RRMeasureViewModel.ITEM_COUNT_FOR_MEDIAN_CALC, 0), timerList.size());
        int medianListSize = itemListForMedian.size();
        if (medianListSize < RRMeasureViewModel.ITEM_COUNT_FOR_MEDIAN_CALC) {
            return;
        }
        Collections.sort(itemListForMedian);
        double medianTime = itemListForMedian.get(medianListSize/2);
        int maxConsistencyVariation = 0;
        for (long time : itemListForMedian) {
            maxConsistencyVariation = (int) Math.max((Math.abs(time - medianTime) / medianTime) * 100, maxConsistencyVariation);
        }

        if (maxConsistencyVariation < RRMeasureViewModel.THRESHOLD_CONSISTENCY_PERCENTAGE) {
            int rr = (int) (60000 / medianTime);
            RespirationRateData respirationRateData = RespirationRateData.getInstance();
            respirationRateData.rr = rr;
            PersonInfo.getInstance().addToDataList(respirationRateData);
            stopRRCalculation(rr + "/min", true);
        }
    }

    private void stopRRCalculation(String text, boolean isSuccess) {
        mViewModel.setIsCalculatingRR(false);
        mOneMinTimer.cancel();
        mTapView.setEnabled(false);
        mTapView.setClickable(false);
        mTextView.setText(text);
        mResetButton.setVisibility(View.VISIBLE);
        if (mProgressBar.getProgress() != 1000) {
            setCalculationProgress(1000);
        }
        if (isSuccess) {
            mNextButton.setText(R.string.next);
        }
    }

    private void startRRCalculation() {
        mTextView.setText("TAP on Inhalation");
        setCalculationProgress(0);
    }

    private void resetActivity() {
        mTapView.setEnabled(true);
        mTapView.setClickable(true);
        mTextView.setText("TAP to START");
        mViewModel.invalidateData();
        mResetButton.setVisibility(View.GONE);
        mOneMinTimer.cancel();
        setIndicatorIcons();
        setCalculationProgress(0);
        RespirationRateData respirationRateData = RespirationRateData.getInstance();
        PersonInfo.getInstance().removeFromDataList(respirationRateData);
    }

    private void setCalculationProgress(int progress) {
        mProgressBar.setMax(10);
        mProgressBar.setMax(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mProgressBar.setProgress(progress, true);
        } else {
            mProgressBar.setProgress(progress);
        }
    }

    private void setIndicatorIcons() {
        mIndicatorList = new ArrayList<>();
        mIndicatorContainer.removeAllViews();
        for (int i = 0; i < RRMeasureViewModel.MAX_TAPS_ALLOWED; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    30,
                    30
            );
            params.setMarginEnd(10);
            params.setMarginStart(10);

            CardView cardView = new CardView(Objects.requireNonNull(getContext()));
            cardView.setRadius(15f);
            cardView.setCardBackgroundColor(getResources().getColor(R.color.indicator_icon_unselected));
            cardView.setLayoutParams(params);
            cardView.setCardElevation(0);
            mIndicatorContainer.addView(cardView);
            mIndicatorList.add(cardView);
        }
    }
}
