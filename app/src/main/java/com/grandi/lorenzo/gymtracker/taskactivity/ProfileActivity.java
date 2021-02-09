package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class ProfileActivity extends AppCompatActivity {

    private TextView tv_profile_date, tv_temperature, tv_step_counter;
    private SensorManager sensorManager;
    private boolean is_temperature_enabled, is_step_counter_enabled;
    private int temperature, step_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViewComponents();
        initTaskComponents();
    }

    private SharedPreferences preferenceLoader() {
        return this.getSharedPreferences(SETTINGS_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }

    private void initViewComponents() {
        this.tv_profile_date = findViewById(R.id.tv_date_profile);
        this.tv_temperature = findViewById(R.id.tv_temperature);
        this.tv_step_counter = findViewById(R.id.tv_step_counter);
    }
    private void initTaskComponents() {
        this.tv_profile_date.setText(new CalendarHandler().getDate());
        this.is_temperature_enabled = preferenceLoader().getBoolean(stepCounterKey.getValue(), false);
        this.is_step_counter_enabled = preferenceLoader().getBoolean(temperatureKey.getValue(), false);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (tv_temperature != null && event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && is_temperature_enabled) {
                    temperature = Math.round(event.values[0]);
                    tv_temperature.setText(temperature);
                } else if (tv_step_counter != null && event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && is_step_counter_enabled) {
                    // TODO: Stepcounter handler -> Simona
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        if (is_temperature_enabled)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
        if (is_step_counter_enabled)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);
        if (!is_temperature_enabled) {
            this.tv_temperature.setText(R.string.sensor_disabled);
            this.tv_temperature.setTextColor(getColor(R.color.colorButtonExit));
        } if (!is_step_counter_enabled) {
            this.tv_step_counter.setText(R.string.sensor_disabled);
            this.tv_step_counter.setTextColor(getColor(R.color.colorButtonExit));
        }
    }
}