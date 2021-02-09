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

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;
import com.grandi.lorenzo.gymtracker.main.MainActivity;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsActivity extends AppCompatActivity {

    private boolean isSpeakButtonLongPressed = false;

    private SwitchCompat sc_temperature, sc_stepCounter;
    private boolean temperature_enabled, stepcounter_enabled;
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

        editor.apply();
    }

    private void initViewComponents() {
        // TODO: initialization for all view components
        this.sc_temperature = findViewById(R.id.temperature_switch);
        this.sc_stepCounter = findViewById(R.id.step_counter_switch);

        this.b_save = findViewById(R.id.b_settings_saver);
        this.b_reset = findViewById(R.id.b_settings_reset);
        this.exit = findViewById(R.id.b_exit);

        this.sc_temperature.setChecked(preferencesLoader().getBoolean(temperatureKey.getValue(), true));
        this.sc_stepCounter.setChecked(preferencesLoader().getBoolean(stepCounterKey.getValue(), true));
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
            Intent intent = new Intent(this, MainActivity.class);
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

    private void switchChecker() {
        this.sc_temperature.setOnCheckedChangeListener((buttonView, isChecked) -> {
            temperature_enabled = isChecked;
            SharedPreferences.Editor editor = preferencesLoader().edit();
            editor.putBoolean(trainingKey.getValue(), temperature_enabled);
            editor.apply();
        });
        this.sc_stepCounter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stepcounter_enabled = isChecked;
            SharedPreferences.Editor editor = preferencesLoader().edit();
            editor.putBoolean(stepCounterKey.getValue(), stepcounter_enabled);
            editor.apply();
        });
        b_save.setOnClickListener(v -> {
            this.preferenceSaver();
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
        b_reset.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferencesLoader().edit();
            editor.clear();
            editor.apply();
            this.sc_stepCounter.setChecked(true);
            this.sc_temperature.setChecked(true);
        });
    }
}