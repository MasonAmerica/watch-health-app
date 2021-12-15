package com.mason.sample.masonhealth;
/**
 * Copyright(C) 2021 Mason America. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bymason.masonui.MasonUtil;

public class HealthProcessActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private HealthNames type;
    private ImageView graphIcon;
    /*QTI Defined Sensor Types*/
    private static final int qSensorTypeBase = 33171000;

    private static final int sensorTypeRespiratory = qSensorTypeBase + 5;
    private static final int sensorTypeSpo2 = qSensorTypeBase + 6;
    private SensorEventListener sensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measuring_activity);
        type = (HealthNames) getIntent().getSerializableExtra("health_enum_key");
        if (type != null) {
            startHealthMeasuring(type);
        }
    }

    private void startHealthMeasuring(HealthNames type) {
        graphIcon = (ImageView) findViewById(R.id.graph);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        graphIcon.startAnimation(animation);

        getPermission();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        switch (type) {
            case HEART_RATE: {
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
                MasonUtil.setupTitleHeader(this, R.string.heart_rate);
                break;
            }
            case BREATHING_RATE: {
                sensor = sensorManager.getDefaultSensor(sensorTypeRespiratory);
                MasonUtil.setupTitleHeader(this, R.string.breathing_rate);
                break;
            }
            case BLOOD_OXYGEN: {
                sensor = sensorManager.getDefaultSensor(sensorTypeSpo2);
                MasonUtil.setupTitleHeader(this, R.string.blood_oxygen);
                break;
            }
        }
        startMeasure();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMeasure();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasure();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        graphIcon.setAnimation(null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (type) {
            case HEART_RATE: {
                if (event.sensor.getType() == Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT) {
                    Log.d("Heart Rate off body", "TYPE_LOW_LATENCY_OFF_BODY_DETECT event has changed" + event.values[0]);
                    if (event.values[0] == 0) {
                        showErrorDialog(true);
                    }
                    break;
                }
                if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
                    Log.d("HeartRate", "Heart Rate event has changed to." + event.values[0]);
                    float heartRateFloat = event.values[0];
                    int values = Math.round(heartRateFloat);
                    if (values > 0) {
                        invokeActivity(values);
                    }
                } else {
                    showErrorDialog(false);
                }
                break;
            }

            case BREATHING_RATE: {
                if (event.sensor.getType() == Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT) {
                    Log.d("Breathing Rate off body", "TYPE_LOW_LATENCY_OFF_BODY_DETECT event has changed" + event.values[0]);
                    if (event.values[0] == 0) {
                        showErrorDialog(true);
                    }
                    break;
                }
                if (event.sensor.getType() == sensorTypeRespiratory) {
                    Log.d("Breathing Rate", "Breathing Rate event has changed to event." + event.values[1]);
                    float breathingRateFloat = event.values[1];
                    int values = Math.round(breathingRateFloat/4);
                    if (values > 0) {
                        invokeActivity(values);
                    }
                } else {
                    showErrorDialog(false);
                }
                break;
            }
            case BLOOD_OXYGEN: {
                if (event.sensor.getType() == Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT) {
                    Log.d("Blood oxygen off body", "TYPE_LOW_LATENCY_OFF_BODY_DETECT event has changed" + event.values[0]);
                    if (event.values[0] == 0) {
                        showErrorDialog(true);
                    }
                    break;
                }
                if (event.sensor.getType() == sensorTypeSpo2) {
                    Log.d("Blood oxygen", "Blood oxygen event has changed to event." + event.values[1]);
                    float bloodOxygenFloat = event.values[1];
                    int values = Math.round(bloodOxygenFloat);
                    if (values > 0) {
                        invokeActivity(values);
                    }
                } else {
                    showErrorDialog(false);
                }
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("TAG", "Accuracy has changed to $accuracyLevel." + accuracy);
    }

    private void startMeasure() {
        boolean sensorRegistered = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST, null);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT), SensorManager.SENSOR_DELAY_FASTEST);
        Log.d("Sensor Status:", " Sensor registered: " + (sensorRegistered ? "yes" : "no"));
    }

    private void stopMeasure() {
        sensorManager.unregisterListener(this);
    }

    private void invokeActivity(int value) {
        switch (type) {
            case HEART_RATE: {
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_HEART_RATE, value);
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_HEART_RATE_TIME, System.currentTimeMillis());
                break;
            }
            case BREATHING_RATE: {
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_BREATHING_RATE, value);
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_BREATHING_RATE_TIME, System.currentTimeMillis());
                break;
            }
            case BLOOD_OXYGEN: {
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_BLOOD_OXYGEN, value);
                SharedPrefs.save(this, SharedPrefs.KEY_SHARED_BLOOD_OXYGEN_TIME, System.currentTimeMillis());
                break;
            }
        }
        Intent intent = new Intent(HealthProcessActivity.this, HealthResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("health_enum_key", type);
        startActivity(intent);
        finish();
    }

    private void showErrorDialog(boolean isOffBodyDetect) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        ImageView errorIcon = (ImageView) dialog.findViewById(R.id.error_icon);
        TextView errorDesc = (TextView) dialog.findViewById(R.id.error_desc);
        if (isOffBodyDetect) {
            errorIcon.setImageResource(R.drawable.ic_proximity_error);
        } else {
            errorIcon.setImageResource(R.drawable.ic_error);
        }
        switch (type) {
            case HEART_RATE: {
                title.setText(R.string.heart_rate);
                if (isOffBodyDetect) {
                    errorDesc.setText(R.string.proximity_error_message);
                } else {
                    errorDesc.setText(getResources().getString(R.string.error_message, getString(R.string.heart), getString(R.string.rate)));
                }
                break;
            }
            case BREATHING_RATE: {
                title.setText(R.string.breathing_rate);
                if (isOffBodyDetect) {
                    errorDesc.setText(R.string.proximity_error_message);
                } else {
                    errorDesc.setText(getResources().getString(R.string.error_message, getString(R.string.breathing), getString(R.string.rate)));
                }
                break;
            }
            case BLOOD_OXYGEN: {
                title.setText(R.string.blood_oxygen);
                if (isOffBodyDetect) {
                    errorDesc.setText(R.string.proximity_error_message);
                } else {
                    errorDesc.setText(getResources().getString(R.string.error_message, getString(R.string.blood), getString(R.string.oxygen)));
                }
                break;
            }
        }
        attachListeners(dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    private void attachListeners(Dialog dialog) {
        Button tryAgain = (Button) dialog.findViewById(R.id.try_again);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startHealthMeasuring(type);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getPermission() {
        requestPermissions(new String[]{Manifest.permission.BODY_SENSORS}, 1);
    }
}
