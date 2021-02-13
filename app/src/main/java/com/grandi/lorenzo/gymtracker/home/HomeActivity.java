package com.grandi.lorenzo.gymtracker.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.grandi.lorenzo.gymtracker.CaptureActivity;
import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.main.StarterActivity;
import com.grandi.lorenzo.gymtracker.taskactivity.SettingsActivity;
import com.grandi.lorenzo.gymtracker.taskactivity.ProfileActivity;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import java.io.FileOutputStream;
import java.util.Arrays;

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
        this.training = trainingStatus(preferenceLoaderLogin().getBoolean(trainingKey.getValue(), false));

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

            //Intent intent = new Intent(this, ScannerActivity.class);
            //new FlagList(intent);
            //startActivity(intent);

            IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActivity.this);
            intentIntegrator.setPrompt("For flash use volume up key");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(CaptureActivity.class);
            intentIntegrator.initiateScan();
        });
        this.ib_settings.setOnClickListener(v -> {
            preferenceSaver(this);
            this.ib_settings.setPressed(true);

            Intent intent = new Intent(this, SettingsActivity.class);
            new FlagList(intent);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult.getContents() != null) {
            if (Arrays.equals(intentResult.getContents().toCharArray(), strQRFlag.getValue().toCharArray())) {
                SharedPreferences.Editor editor = preferenceLoaderLogin().edit();
                this.training = switchTrainingStatus(this.training);
                editor.putBoolean(trainingKey.getValue(), this.training);
                editor.apply();
                registerEvent();
            } else {
                Toast.makeText(this, R.string.no_scan_detected, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED);
            }
        } else {
            Toast.makeText(this, R.string.scan_code, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
        }
    }

    private void registerEvent() {
        String name, date, registration;
        name = preferenceLoaderLogin().getString(nameKey.getValue(), nameKey.getValue());
        date = new CalendarHandler().getDateComplete();
        if (training) registration = " > " + name + " left the gym :\t" + date + "\n";
        else registration = " > " + name + " joined the gym :\t" + date + "\n";
        try {
            FileOutputStream eventRegister = openFileOutput(this.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()), Context.MODE_PRIVATE);
            eventRegister.write(registration.getBytes());
            eventRegister.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTaskComponents() {
        this.tv_date.setText((new CalendarHandler()).getDate());
        this.name = preferenceLoaderLogin().getString(nameKey.getValue(), nameKey.getValue());

        if (!this.name.isEmpty() && !this.name.equals(nameKey.getValue())) this.tv_name.setText(this.name);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean trainingStatus(boolean _training) {
        if (_training)
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_exit));
        else
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_enter));
        return _training;
    }
    private boolean switchTrainingStatus(boolean _training) {
        return !_training;
    }
}