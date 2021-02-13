package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grandi.lorenzo.gymtracker.globalClasses.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;
import com.grandi.lorenzo.gymtracker.main.StarterActivity;

import java.io.File;

import static com.grandi.lorenzo.gymtracker.globalClasses.KeyLoader.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsActivity extends AppCompatActivity {

    private boolean isSpeakButtonLongPressed = false;

    private SwitchCompat sc_temperature, sc_stepCounter, sc_humidity;
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
        editor.putBoolean(temperatureKey.getValue(), this.sc_temperature.isChecked());
        editor.putBoolean(stepCounterKey.getValue(), this.sc_stepCounter.isChecked());
        editor.putBoolean(humidityKey.getValue(), this.sc_humidity.isChecked());

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

        this.sc_temperature.setChecked(preferencesLoader().getBoolean(temperatureKey.getValue(), false));
        this.sc_stepCounter.setChecked(preferencesLoader().getBoolean(stepCounterKey.getValue(), false));
        this.sc_humidity.setChecked(preferencesLoader().getBoolean(humidityKey.getValue(), false));
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initTaskComponents() {
        // TODO: initialization for all task components

        b_save.setOnClickListener(v -> {
            this.preferenceSaver();
            Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
        });
        b_reset.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferencesLoader().edit();
            editor.remove(temperatureKey.getValue());
            editor.remove(stepCounterKey.getValue());
            editor.remove(humidityKey.getValue());
            editor.apply();
            this.sc_stepCounter.setChecked(false);
            this.sc_temperature.setChecked(false);
            this.sc_humidity.setChecked(false);
        });
        this.exit.setOnClickListener(v -> {
            SharedPreferences.Editor editor_login = getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
            editor_login.clear();
            editor_login.apply();
            SharedPreferences.Editor editor_settings = getSharedPreferences(SETTINGS_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
            editor_settings.clear();
            editor_settings.apply();
            File eventRegister = new File(this.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()));
            eventRegister.delete();

            Intent intent = new Intent(this, StarterActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
        this.exit.setOnTouchListener(speakTouchListener);
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
}