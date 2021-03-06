package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.globalClasses.FlagList;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.grandi.lorenzo.gymtracker.globalClasses.KeyLoader.*;

public class ProfileActivity extends AppCompatActivity {

    private TextView tv_profile_date, tv_temperature, tv_step_counter,tv_humidity;
    private ScrollView sv_file_reader;

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private boolean is_temperature_enabled, is_step_counter_enabled, is_humidity_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViewComponents();
        initTaskComponents();
    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this.sensorEventListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sensorManager.unregisterListener(this.sensorEventListener);
        Intent intent = new Intent(this, HomeActivity.class);
        new FlagList(intent);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (is_temperature_enabled)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
        if (is_step_counter_enabled)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);
        if (is_humidity_enabled)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
        if (!is_temperature_enabled) {
            this.tv_temperature.setText(R.string.sensor_disabled);
            this.tv_temperature.setTextColor(getColor(R.color.colorButtonExit));
        }
        if (!is_step_counter_enabled) {
            this.tv_step_counter.setText(R.string.sensor_disabled);
            this.tv_step_counter.setTextColor(getColor(R.color.colorButtonExit));
        }
        if (!is_humidity_enabled) {
            this.tv_humidity.setText(R.string.sensor_disabled);
            this.tv_humidity.setTextColor(getColor(R.color.colorButtonExit));
        }
    }

    private SharedPreferences preferenceLoader() {
        return this.getSharedPreferences(SETTINGS_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }

    private void initViewComponents() {
        this.tv_profile_date = findViewById(R.id.tv_date_profile);
        this.tv_temperature = findViewById(R.id.tv_temperature);
        this.tv_step_counter = findViewById(R.id.tv_step_counter);
        this.tv_humidity = findViewById(R.id.tv_humidity);
        this.sv_file_reader = findViewById(R.id.sv_file);
    }

    private void initTaskComponents() {
        this.tv_profile_date.setText(new CalendarHandler().getDate());
        this.is_temperature_enabled = preferenceLoader().getBoolean(temperatureKey.getValue(), false);
        this.is_step_counter_enabled = preferenceLoader().getBoolean(stepCounterKey.getValue(), false);
        this.is_humidity_enabled = preferenceLoader().getBoolean(humidityKey.getValue(), false);

        final RxPermissions rxPermissions = new RxPermissions(this);
        new RxPermissions(this).request(Manifest.permission.ACTIVITY_RECOGNITION)
                .subscribe(granted ->{
                    if(granted)
                        Log.d("TAG", "Is ACTIVITY_RECOGNITION permission granted: $isGranted");
                });

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)==null) {
            tv_temperature.setText(R.string.sensor_not_available);
            tv_temperature.setTextColor(getColor(R.color.colorButtonExitDark));
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)==null) {
            tv_step_counter.setText(R.string.sensor_not_available);
            tv_step_counter.setTextColor(getColor(R.color.colorButtonExitDark));
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)==null) {
            tv_humidity.setText(R.string.sensor_not_available);
            tv_humidity.setTextColor(getColor(R.color.colorButtonExitDark));
        }

        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (tv_temperature != null && event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && is_temperature_enabled) {
                    String str_temperature = Math.round(event.values[0]) + " °C";
                    tv_temperature.setText(str_temperature);
                } if (tv_step_counter != null && event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && is_step_counter_enabled) {
                    String str_step_counter = event.values[0] + " steps";
                    tv_step_counter.setText(str_step_counter);
                }
                if(event.sensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY) {
                    String str_humidity = event.values[0] + " %";
                    tv_humidity.setText(str_humidity);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
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
        svFileReader();
    }

    private String readSavesAtOpen() {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(this.getFilesDir(), REGISTRATION_FILE.getValue())));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.e("FileNotFound", String.valueOf(e));
        } catch (IOException e) {
            Log.e("IOException", String.valueOf(e));
        }

        return stringBuilder.toString();
    }
    private void svFileReader() {
        String registrations = readSavesAtOpen();
        TextView tv_line = new TextView(this);
        if (!registrations.isEmpty()) {
            tv_line.setText(registrations);

            LinearLayout ll_line = new LinearLayout(this);
            ll_line.setOrientation(LinearLayout.VERTICAL);
            ll_line.setGravity(Gravity.CENTER_VERTICAL);
            ll_line.addView(tv_line);
            this.sv_file_reader.addView(ll_line);
        } else {
            tv_line.setText(R.string.no_registrations);

            tv_line.setTextColor(getColor(R.color.colorButtonExit));

            LinearLayout ll_line = new LinearLayout(this);
            ll_line.setOrientation(LinearLayout.VERTICAL);
            ll_line.setGravity(Gravity.CENTER_HORIZONTAL);
            ll_line.addView(tv_line);
            this.sv_file_reader.addView(ll_line);
        }
    }
}