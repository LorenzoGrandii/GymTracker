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

    private TextView tv_profile_date, tv_temperature, tv_step_counter,tv_humidity;
    private SensorManager sensorManager;
    private int temperature, steps;
    private Sensor s_temperature,step_counter,humidity,speed;

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
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)==null)
            tv_temperature.setText(R.string.available);
        else
        s_temperature = this.sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)==null)
            tv_step_counter.setText(R.string.available);
        else
        step_counter=this.sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)==null)
            tv_humidity.setText(R.string.available);
        else
            humidity=this.sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE)
            this.tv_temperature.setText(event.values[0]+" °C");
        //this.temperature = Math.round(event.values[0]);
        else if (event.sensor.getType()==Sensor.TYPE_STEP_COUNTER)
            this.tv_step_counter.setText(event.values[0]+" passi");
        else if(event.sensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY);
            this.tv_humidity.setText(event.values[0]+" %");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,s_temperature,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,step_counter,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,humidity,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}