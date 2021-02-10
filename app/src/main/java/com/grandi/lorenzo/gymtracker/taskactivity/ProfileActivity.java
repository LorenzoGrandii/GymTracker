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

    private TextView tv_profile_date, tv_temperature, tv_step_counter,tv_humidity;
    private SensorManager sensorManager;

    private int temperature, steps_counted;
    private Sensor s_temperature,s_step_counter,s_humidity,s_speed;
    private SensorEventListener sensorEventListener;

    private boolean is_temperature_enabled, is_step_counter_enabled, is_humidity_enabled;

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
        this.tv_humidity=findViewById(R.id.tv_humidity);
    }

    private void initTaskComponents() {
        this.tv_profile_date.setText(new CalendarHandler().getDate());
        this.is_temperature_enabled = preferenceLoader().getBoolean(stepCounterKey.getValue(), false);
        this.is_step_counter_enabled = preferenceLoader().getBoolean(temperatureKey.getValue(), false);
        this.is_humidity_enabled = preferenceLoader().getBoolean(humidityKey.getValue(), false);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)==null) {
            tv_temperature.setText(R.string.sensor_not_available);
            tv_temperature.setTextColor(getColor(R.color.colorButtonExitDark));
        }
        else this.s_temperature = this.sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)==null) {
            tv_step_counter.setText(R.string.sensor_not_available);
            tv_step_counter.setTextColor(getColor(R.color.colorButtonExitDark));
        }
        else this.s_step_counter=this.sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)==null) {
            tv_humidity.setText(R.string.sensor_not_available);
            tv_humidity.setTextColor(getColor(R.color.colorButtonExitDark));
        }
        else this.s_humidity=this.sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

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
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this.sensorEventListener);
    }
}