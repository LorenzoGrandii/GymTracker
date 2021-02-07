package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;

import java.util.ArrayList;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class SettingsActivity extends AppCompatActivity {

    private TextInputLayout til_color;
    private AutoCompleteTextView actv_color;

    private ArrayList<String> colors;
    private ArrayAdapter<String> arrayAdapter_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // initViewComponents();
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

        editor.apply();
    }

    //private void initViewComponents() {
        // TODO: initialization for all view components
    //    this.til_color = (TextInputLayout) findViewById(R.id.til_color);
    //    this.actv_color = (AutoCompleteTextView) findViewById(R.id.actv_color);

    //    this.colors = new ArrayList<>();
    //    colors.add("Red");
    //    colors.add("Blue");
    //    colors.add("Green");
    //    colors.add("Orange");

    //    arrayAdapter_color = new ArrayAdapter<>(getApplication(), R.layout.tv_entity_dropdown_menu, colors);
    //    actv_color.setAdapter(arrayAdapter_color);
    //    actv_color.setThreshold(1);

    //}
    private void initTaskComponents() {
        // TODO: initialization for all task components
    }

}