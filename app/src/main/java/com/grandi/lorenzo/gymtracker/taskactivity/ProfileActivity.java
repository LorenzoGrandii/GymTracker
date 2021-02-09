package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class ProfileActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tv_profile_date, tv_temperature, tv_step_counter;
    private SensorManager sensorManager;
    private int temperature, steps;

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
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor s_temperature = this.sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.temperature = Math.round(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}