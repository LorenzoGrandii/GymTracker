package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Spinner;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class SettingsActivity extends AppCompatActivity {

    private Spinner s_color;
    private String color;

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

        editor.apply();
    }

    private void initViewComponents() {
        // TODO: initialization for all view components
        this.s_color = findViewById(R.id.s_color);
        this.color = String.valueOf(s_color.getSelectedItem());

    }
    private void initTaskComponents() {
        // TODO: initialization for all task components
    }
}