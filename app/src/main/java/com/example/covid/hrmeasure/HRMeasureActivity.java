package com.example.covid.hrmeasure;

import com.example.covid.data.HeartRate;
import com.example.covid.data.PersonInfo;
import com.example.covid.data.SPo2Data;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid.R;
import com.example.covid.rrmeasure.RRMeasureActivity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HRMeasureActivity extends AppCompatActivity {
    public static final String APP_TAG = "HRMeasure";

    @BindView(R.id.hr_measure_text) TextView mHrCountTv;
    @BindView(R.id.spo2_measure_text) TextView mSpo2Tv;
    @BindView(R.id.hr_measure_button) Button mHrMeasureBtn;
    @BindView(R.id.spo2_measure_button) Button mSpo2MeasureBtn;
    @BindView(R.id.next_button) Button mNextButton;

    private Context mContext;
    private HealthDataStore mStore;
    private HealthDataReporter mReporter;
    private boolean mIsFetching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_measure_activity);

        ButterKnife.bind(this);
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

        mHrMeasureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appInstalledOrNot("com.sec.android.app.shealth")) {
                    Toast.makeText(mContext, "Please install SHealth and try Again", Toast.LENGTH_LONG).show();
                    return;
                }

                if (isHRSensorAvailable()) {
                    mIsFetching = true;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://shealth.samsung.com/deeplink?sc_id=tracker.heartrate&action"
                            + "=view&destination=track&deeplink_permission=deeplink#Intent;scheme=https;" +
                            "action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;" +
                            "package=com.sec.android.app.shealth;end"));
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "HR Sensor not available. Please skip!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mSpo2MeasureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFetching = true;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://shealth.samsung.com/deeplink?sc_id=tracker.spo2&action"
                        + "=view&destination=track"));
                startActivity(intent);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RRMeasureActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            mReporter = new HealthDataReporter(mStore);
            if (isPermissionAcquired()) {
                mReporter.start(mHealthDataObserver);
            } else {
                requestPermission();
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
            if (!isFinishing()) {
                mStore.connectService();
            }
        }
    };

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private boolean isHRSensorAvailable() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_HEART_RATE);
        return listSensor.size() > 0;
    }

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(HRMeasureActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(HRMeasureActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private boolean isPermissionAcquired() {
        PermissionKey permKeyHr = new PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, PermissionType.READ);
        //PermissionKey permKeyOS = new PermissionKey(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, PermissionType.READ);
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMapHr = pmsManager.isPermissionAcquired(Collections.singleton(permKeyHr));
            //Map<PermissionKey, Boolean> resultMapOS = pmsManager.isPermissionAcquired(Collections.singleton(permKeyOS));
            return  resultMapHr.get(permKeyHr) /*&&   resultMapOS.get(permKeyOS)*/;
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission request fails.", e);
        }
        return false;
    }

    private void requestPermission() {
        PermissionKey permKeyHr = new PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, PermissionType.READ);
        //PermissionKey permKeySpo2 = new PermissionKey(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, PermissionType.READ);
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        Set<PermissionKey> requestSet = new HashSet<PermissionKey>();
        requestSet.add(permKeyHr);
        //requestSet.add(permKeySpo2);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(requestSet, HRMeasureActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            updateHrView("");
                            updateSp02View("");
                            showPermissionAlarmDialog();
                        } else {
                            // Get the current step count and display it
                            mReporter.start(mHealthDataObserver);
                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }

    private HealthDataReporter.HealthDataObserver mHealthDataObserver = (hr, sp02) -> {
        Log.d(APP_TAG, "hr : " + hr + "sp02 : " + sp02);
        HeartRate heartRate = HeartRate.getInstance();
        if (hr > 0) {
            heartRate.hr = hr;
            PersonInfo.getInstance().addToDataList(heartRate);
        } else {
            PersonInfo.getInstance().removeFromDataList(heartRate);
        }
        updateHrView(String.valueOf(hr));

        SPo2Data sPo2Data = SPo2Data.getInstance();
        if (sp02 > 0) {
            sPo2Data.spo2 = sp02;
            PersonInfo.getInstance().addToDataList(sPo2Data);
        } else {
            PersonInfo.getInstance().removeFromDataList(sPo2Data);
        }
        updateSp02View(String.valueOf(sp02));
    };

    private void updateHrView(final String count) {
        runOnUiThread(() -> {
            mHrCountTv.setText(count);
            mNextButton.setText(Integer.parseInt(count) > 0 ? R.string.next : R.string.skip);
        });
    }

    private void updateSp02View(final String count) {
        runOnUiThread(() -> mSpo2Tv.setText(count));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFetching) {
            mIsFetching = false;
            if (!isPermissionAcquired()) {
                requestPermission();
            }
        }
    }
}
