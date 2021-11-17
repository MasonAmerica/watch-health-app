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