package com.mason.sample.masonhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bymason.masonui.MasonUtil;
import com.mason.sample.masonhealth.view.ItemView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MasonUtil.setupTitleHeader(this, R.string.app_name);
        attachListeners();
    }

    private void attachListeners() {
        View healthView = (ItemView) findViewById(R.id.health_rate);
        healthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthNames value = HealthNames.HEART_RATE;
                invokeActivity(value);
            }
        });
        View breatheView = (ItemView) findViewById(R.id.breathing_rate);
        breatheView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              boolean isBreathingRateReady = false;
                if (isBreathingRateReady) {
                    HealthNames value = HealthNames.BREATHING_RATE;
                    invokeActivity(value);
                } else {
                    Toast.makeText(MainActivity.this, R.string.in_progress,Toast.LENGTH_SHORT).show();
                }
            }
        });

        View oxygenView = (ItemView) findViewById(R.id.blood_oxygen);
        oxygenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBloodOxygenReady = false;
                if (isBloodOxygenReady) {
                    HealthNames value = HealthNames.BLOOD_OXYGEN;
                    invokeActivity(value);
                } else {
                    Toast.makeText(MainActivity.this, R.string.in_progress,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void invokeActivity(HealthNames values) {
        Intent intent = new Intent(MainActivity.this, HealthHomeActivity.class);
        intent.putExtra("health_enum_key", values);
        startActivity(intent);
    }
}