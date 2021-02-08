package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class SettingsActivity extends AppCompatActivity {

    private Spinner s_color;
    private String color;
    private SwitchCompat sc_temperature, sc_stepCounter;
    private boolean temperature_enabled, stepcounter_enabled;
    private Button b_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (this != null) {
            initViewComponents();
            initTaskComponents();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preferenceSaver();
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
        editor.putString(colorKey.getValue(), this.color);
        editor.putBoolean(temperatureKey.getValue(), this.temperature_enabled);
        editor.putBoolean(stepCounterKey.getValue(), this.stepcounter_enabled);

        editor.apply();
    }

    private void initViewComponents() {
        // TODO: initialization for all view components
        this.s_color = findViewById(R.id.s_color);
        this.color = String.valueOf(s_color.getSelectedItem());

        this.sc_temperature = findViewById(R.id.temperature_switch);
        this.sc_stepCounter = findViewById(R.id.step_counter_switch);

        this.b_save = findViewById(R.id.settings_saver);
    }
    private void initTaskComponents() {
        // TODO: initialization for all task components
        this.sc_temperature.setChecked(preferencesLoader().getBoolean(temperatureKey.getValue(), false));
        this.sc_stepCounter.setChecked(preferencesLoader().getBoolean(stepCounterKey.getValue(), false));
        changeListener();
    }

    private void changeListener() {
        // return true on any setting change -> button state clickable and color primary set

        this.sc_temperature.setOnCheckedChangeListener((buttonView, isChecked) -> {
            temperature_enabled = isChecked;
        });
        this.sc_stepCounter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stepcounter_enabled = isChecked;
        });
        b_save.setOnClickListener(v -> {
            this.preferenceSaver();
        });
    }
}