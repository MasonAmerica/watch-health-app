package com.mason.sample.masonhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bymason.masonui.MasonUtil;

import java.util.concurrent.TimeUnit;

public class HealthHomeActivity extends Activity {
    private HealthNames type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        type = (HealthNames) getIntent().getSerializableExtra("health_enum_key");
        if (type != null) {
            setUpView(type);

        }
    }

    private void setUpView(HealthNames type) {
        ImageView icon = (ImageView) findViewById(R.id.icon);
        TextView readingValue = (TextView) findViewById(R.id.label);
        TextView readingUnit = (TextView) findViewById(R.id.unit);
        switch (type) {
            case HEART_RATE: {
                MasonUtil.setupTitleHeader(this, R.string.heart_rate);
                icon.setImageResource(R.drawable.ic_heart_rate);
                icon.setColorFilter(getResources().getColor(R.color.title_text), android.graphics.PorterDuff.Mode.SRC_IN);
                long heartRate = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_HEART_RATE);
                if (heartRate > 0) {
                    readingValue.setText(Long.toString(heartRate));
                    readingUnit.setText(getString(R.string.heart_rate_unit));
                    long past = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_HEART_RATE_TIME);
                    setTime(past);
                }
                break;
            }
            case BREATHING_RATE: {
                MasonUtil.setupTitleHeader(this, R.string.breathing_rate);
                icon.setImageResource(R.drawable.ic_breathing);
                icon.setColorFilter(getResources().getColor(R.color.title_text), android.graphics.PorterDuff.Mode.SRC_IN);
                long breathingRate = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BREATHING_RATE);
                if (breathingRate > 0) {
                    readingValue.setText(Long.toString(breathingRate));
                    readingUnit.setText(getString(R.string.breathing_rate_unit));
                    long past = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BREATHING_RATE_TIME);
                    setTime(past);
                }
                break;
            }
            case BLOOD_OXYGEN: {
                MasonUtil.setupTitleHeader(this, R.string.blood_oxygen);
                icon.setImageResource(R.drawable.ic_blood_group);
                long oxygenValue = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BLOOD_OXYGEN);
                if (oxygenValue > 0) {
                    String oxyValue = getString(R.string.num_percent, Long.toString(oxygenValue));
                    readingValue.setText(oxyValue);
                    readingUnit.setVisibility(View.GONE);
                    long past = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BLOOD_OXYGEN_TIME);
                    setTime(past);
                }
                break;
            }
        }
        attachListeners();
    }

    private void setTime(long past) {
        TextView timeTextView = (TextView) findViewById(R.id.time_label);
        long now = System.currentTimeMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(now - past);
        long nowDays = TimeUnit.MILLISECONDS.toDays(now);
        long pastDays = TimeUnit.MILLISECONDS.toDays(past);
        long diffDays = nowDays - pastDays;
        if (seconds < 30) {
            Log.d("Time", seconds + " seconds ago");
            timeTextView.setText(R.string.just_now);
        } else if (seconds < 60) {
            Log.d("Time", seconds + " seconds ago");
            timeTextView.setText(getString(R.string.seconds_ago, seconds));
        } else if (seconds < TimeUnit.MINUTES.toSeconds(60)) {
            Log.d("Time", seconds + " seconds ago");
            timeTextView.setText(getString(R.string.minute_ago, TimeUnit.SECONDS.toMinutes(seconds)));
        } else if (seconds < TimeUnit.HOURS.toSeconds(24)) {
            Log.d("Time", seconds + " seconds ago");
            timeTextView.setText(getString(R.string.hours_ago, TimeUnit.SECONDS.toHours(seconds)));
        } else if (diffDays == 1) {
            Log.d("Time", seconds + " seconds ago");
            timeTextView.setText(R.string.yesterday);
        } else {
            System.out.println(diffDays + " days ago");
            timeTextView.setText(getString(R.string.days_ago, diffDays));
        }
    }

    private void attachListeners() {
        Button measureButton = (Button) findViewById(R.id.measure_button);
        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthHomeActivity.this, HealthProcessActivity.class);
                intent.putExtra("health_enum_key", type);
                startActivity(intent);
            }
        });
    }
}
