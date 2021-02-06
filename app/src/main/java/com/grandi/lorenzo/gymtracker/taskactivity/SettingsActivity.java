package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        editor.apply();
    }

    private void initViewComponents() {
        // TODO: initialization for all view components
    }
    private void initTaskComponents() {
        // TODO: initialization for all task components
    }

}