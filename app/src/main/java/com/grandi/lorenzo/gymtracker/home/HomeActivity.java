package com.grandi.lorenzo.gymtracker.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.main.StarterActivity;
import com.grandi.lorenzo.gymtracker.taskactivity.SettingsActivity;
import com.grandi.lorenzo.gymtracker.scanner.ScannerActivity;
import com.grandi.lorenzo.gymtracker.taskactivity.ProfileActivity;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class HomeActivity extends FragmentActivity {

    private TextView tv_date, tv_name;
    private ImageButton ib_profile, ib_scanner, ib_settings;

    private String name;
    private boolean training;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (preferenceLoaderLogin().getBoolean(loggedKey.getValue(), false)) {
            preferenceLoaderLogin();
            initViewComponents(this);
            initTaskComponents();
            preferenceSaver(this);
        } else {
            Intent intent = new Intent(this, StarterActivity.class);
            new FlagList(intent);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private SharedPreferences preferenceLoaderLogin() {
        return this.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }

    private void preferenceSaver(Activity activity) {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nameKey.getValue(), this.name);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewComponents(Activity activity) {
        this.tv_name = activity.findViewById(R.id.tv_name);
        this.tv_date = activity.findViewById(R.id.tv_date);

        this.ib_profile = activity.findViewById(R.id.profile_opener);
        this.ib_scanner = activity.findViewById(R.id.scanner_opener);
        this.ib_settings = activity.findViewById(R.id.settings_opener);
        trainingStatus(this.training);

        this.ib_profile.setOnClickListener(v -> {
            preferenceSaver(this);
            this.ib_profile.setPressed(true);

            Intent intent = new Intent(this, ProfileActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
        this.ib_scanner.setOnClickListener(v -> {
            preferenceSaver(this);
            this.ib_scanner.setPressed(true);

            Intent intent = new Intent(this, ScannerActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
        this.ib_settings.setOnClickListener(v -> {
            preferenceSaver(this);
            this.ib_settings.setPressed(true);

            Intent intent = new Intent(this, SettingsActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
    }
    private void initTaskComponents() {
        this.tv_date.setText((new CalendarHandler()).getDate());
        this.name = preferenceLoaderLogin().getString(nameKey.getValue(), nameKey.getValue());
        this.training = preferenceLoaderLogin().getBoolean(trainingKey.getValue(), false);

        if (!this.name.isEmpty() && !this.name.equals(nameKey.getValue())) this.tv_name.setText(this.name);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void trainingStatus(boolean training) {
        if (training)
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_exit));
        else
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_enter));
    }
}