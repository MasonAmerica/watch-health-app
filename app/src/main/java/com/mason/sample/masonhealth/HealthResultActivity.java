package com.mason.sample.masonhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthResultActivity extends Activity {

    private HealthNames type;

    private static final int HEART_RATE_VERY_LIGHT_LOWER_VALUE = 104;
    private static final int HEART_RATE_VERY_LIGHT_HIGHER_VALUE = 114;
    private static final int HEART_RATE_LIGHT_LOWER_VALUE = 114;
    private static final int HEART_RATE_LIGHT_HIGHER_VALUE = 133;
    private static final int HEART_RATE_MODERATE_LOWER_VALUE = 133;
    private static final int HEART_RATE_MODERATE_HIGHER_VALUE = 152;
    private static final int HEART_RATE_HARD_LOWER_VALUE = 152;
    private static final int HEART_RATE_HARD_HIGHER_VALUE = 172;
    private static final int HEART_RATE_MAXIMUM_LOWER_VALUE = 172;
    private static final int HEART_RATE_MAXIMUM_HIGHER_VALUE = 190;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        type = (HealthNames) getIntent().getSerializableExtra("health_enum_key");
        if (type != null) {
            setUpResultView();
        }
    }

    private void setUpResultView() {
        TextView titleText = (TextView) findViewById(R.id.toolbar_title);
        ImageView icon = (ImageView) findViewById(R.id.icon);
        TextView result = (TextView) findViewById(R.id.label);
        TextView unit = (TextView) findViewById(R.id.unit);
        TextView description = (TextView) findViewById(R.id.desc);
        switch (type) {
            case HEART_RATE: {
                titleText.setText(R.string.heart_rate);
                icon.setImageResource(R.drawable.ic_heart_rate);
                icon.setColorFilter(getResources().getColor(R.color.mason_title_text), android.graphics.PorterDuff.Mode.SRC_IN);
                long heartRate = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_HEART_RATE);
                result.setText(Long.toString(heartRate));
                unit.setText(getString(R.string.heart_rate_unit));
                setZoneDesc(description, heartRate);
                break;
            }
            case BREATHING_RATE: {
                titleText.setText(R.string.breathing_rate);
                icon.setImageResource(R.drawable.ic_breathing);
                icon.setColorFilter(getResources().getColor(R.color.mason_title_text), android.graphics.PorterDuff.Mode.SRC_IN);
                long breathingRate = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BREATHING_RATE);
                result.setText(Long.toString(breathingRate));
                unit.setText(getString(R.string.breathing_rate_unit));
                description.setText(R.string.breathing_rate_desc);
                break;
            }
            case BLOOD_OXYGEN: {
                titleText.setText(R.string.blood_oxygen);
                icon.setImageResource(R.drawable.ic_blood_group);
                long oxygenValue = SharedPrefs.getLong(this, SharedPrefs.KEY_SHARED_BLOOD_OXYGEN);
                String oxyValue = getString(R.string.num_percent, Long.toString(oxygenValue));
                result.setText(oxyValue);
                unit.setVisibility(View.GONE);
                description.setText(R.string.blood_oxygen_desc);
                break;
            }
        }
        attachListeners();
    }

    private void setZoneDesc(TextView description, long value) {
        if (value >= HEART_RATE_VERY_LIGHT_LOWER_VALUE && value < HEART_RATE_VERY_LIGHT_HIGHER_VALUE) {
            description.setText(R.string.heart_rate_very_light_desc);
        } else if (value >= HEART_RATE_LIGHT_LOWER_VALUE && value < HEART_RATE_LIGHT_HIGHER_VALUE) {
            description.setText(R.string.heart_rate_light_desc);
        } else if (value >= HEART_RATE_MODERATE_LOWER_VALUE && value < HEART_RATE_MODERATE_HIGHER_VALUE) {
            description.setText(R.string.heart_rate_moderate_desc);
        } else if (value >= HEART_RATE_HARD_LOWER_VALUE && value < HEART_RATE_HARD_HIGHER_VALUE) {
            description.setText(R.string.heart_rate_hard_desc);
        } else if (value >= HEART_RATE_MAXIMUM_LOWER_VALUE && value < HEART_RATE_MAXIMUM_HIGHER_VALUE) {
            description.setText(R.string.heart_rate_max_desc);
        } else {
            description.setText(R.string.heart_rate_normal_desc);
        }
    }

    private void attachListeners() {
        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthResultActivity.this, HealthHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("health_enum_key", type);
                startActivity(intent);
                finish();
            }
        });
    }
}
