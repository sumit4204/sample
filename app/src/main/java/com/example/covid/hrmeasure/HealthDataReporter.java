/**
 * Copyright (C) 2014 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.example.covid.hrmeasure;

import android.database.Cursor;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.Calendar;
import java.util.TimeZone;

public class HealthDataReporter {
    private final HealthDataStore mStore;
    private HealthDataObserver mHealthDataObserver;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;

    public HealthDataReporter(HealthDataStore store) {
        mStore = store;
    }

    public void start(HealthDataObserver listener) {
        mHealthDataObserver = listener;
        // Register an observer to listen changes of step count and get today step count
        com.samsung.android.sdk.healthdata.HealthDataObserver.addObserver(mStore, HealthConstants.HeartRate.HEALTH_DATA_TYPE, mObserver);
        //com.samsung.android.sdk.healthdata.HealthDataObserver.addObserver(mStore, HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, mObserver);
        readLastMeasuredData();
    }

    // Read the today's step count on demand
    private void readLastMeasuredData() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                    .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                //.setDataType(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE)
                    .setProperties(new String[] {
                            HealthConstants.HeartRate.HEART_RATE,
                            //HealthConstants.OxygenSaturation.HEART_RATE,
                            //HealthConstants.OxygenSaturation.SPO2
                    })
                    .build();

        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            Log.e(HRMeasureActivity.APP_TAG, "Getting step count fails.", e);
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {
        int count = 0;
        Cursor c = null;
        int sp02 = 0;
        int heartRate = 0;

        try {
            c = result.getResultCursor();
            if (c != null) {
                if (c.getCount() == 0) {
                    Log.d(HRMeasureActivity.APP_TAG, "No data found.");
                }
                while (c.moveToNext()) {
                    //sp02 = c.getInt(c.getColumnIndex(HealthConstants.OxygenSaturation.SPO2));
                    heartRate = (int) c.getLong(c.getColumnIndex(HealthConstants.HeartRate.HEART_RATE));
                    //heartRate = (int) c.getLong(c.getColumnIndex(HealthConstants.OxygenSaturation.HEART_RATE));
                }
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (mHealthDataObserver != null) {
            mHealthDataObserver.onChanged(heartRate, sp02);
        }
    };

    private final com.samsung.android.sdk.healthdata.HealthDataObserver mObserver = new com.samsung.android.sdk.healthdata.HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(HRMeasureActivity.APP_TAG, "Observer receives a data changed event");
            readLastMeasuredData();
        }
    };

    public interface HealthDataObserver {
        void onChanged(int count, int sp02);
    }
}
