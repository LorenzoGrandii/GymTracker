package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;
import com.grandi.lorenzo.gymtracker.main.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsActivity extends AppCompatActivity {

    private boolean isSpeakButtonLongPressed = false;

    private SwitchCompat sc_temperature, sc_stepCounter, sc_humidity;
    private boolean temperature_enabled, stepcounter_enabled, humidity_enabled;
    private Button b_save, b_reset;
    private TextView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViewComponents();
        initTaskComponents();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        new FlagList(intent);
        startActivity(intent);
    }

    private SharedPreferences preferencesLoader() {
        return this.getSharedPreferences(SETTINGS_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }
    private void preferenceSaver() {
        SharedPreferences.Editor editor = preferencesLoader().edit();
        // TODO: save all preferences used
        editor.putBoolean(temperatureKey.getValue(), this.temperature_enabled);
        editor.putBoolean(stepCounterKey.getValue(), this.stepcounter_enabled);
        editor.putBoolean(humidityKey.getValue(), this.humidity_enabled);

        editor.apply();
    }

    private void initViewComponents() {
        // TODO: initialization for all view components
        this.sc_temperature = findViewById(R.id.temperature_switch);
        this.sc_stepCounter = findViewById(R.id.step_counter_switch);
        this.sc_humidity = findViewById(R.id.humidity_switch);

        this.b_save = findViewById(R.id.b_settings_saver);
        this.b_reset = findViewById(R.id.b_settings_reset);
        this.exit = findViewById(R.id.b_exit);

        this.sc_temperature.setChecked(preferencesLoader().getBoolean(temperatureKey.getValue(), true));
        this.sc_stepCounter.setChecked(preferencesLoader().getBoolean(stepCounterKey.getValue(), true));
        this.sc_humidity.setChecked(preferencesLoader().getBoolean(humidityKey.getValue(), false));
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initTaskComponents() {
        // TODO: initialization for all task components

        switchChecker();
        this.exit.setOnClickListener(v -> {
            SharedPreferences.Editor editor_login = getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
            editor_login.clear();
            editor_login.apply();
            SharedPreferences.Editor editor_settings = getSharedPreferences(SETTINGS_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
            editor_settings.clear();
            editor_settings.apply();
            File eventRegister = new File(this.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()));
            eventRegister.delete();

            Intent intent = new Intent(this, MainActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
        this.exit.setOnTouchListener(speakTouchListener);

        b_save.setOnClickListener(v -> {
            this.preferenceSaver();
            Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
        });
        b_reset.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferencesLoader().edit();
            editor.remove(temperatureKey.getValue());
            editor.remove(stepCounterKey.getValue());
            editor.apply();
            this.sc_stepCounter.setChecked(true);
            this.sc_temperature.setChecked(true);
            this.sc_humidity.setChecked(true);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener speakTouchListener = (v, event) -> {
        v.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isSpeakButtonLongPressed) {
                isSpeakButtonLongPressed = false;
                exit.setTextColor(getColor(R.color.color1));
            }
        }
        return false;
    };

    private void switchChecker() {
        this.sc_temperature.setOnCheckedChangeListener((buttonView, isChecked) -> {
            temperature_enabled = isChecked;
        });
        this.sc_stepCounter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stepcounter_enabled = isChecked;
        });
        this.sc_humidity.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            humidity_enabled = isChecked;
        }));
    }
}