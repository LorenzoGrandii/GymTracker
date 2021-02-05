package com.grandi.lorenzo.gymtracker.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.taskactivity.SettingsActivity;
import com.grandi.lorenzo.gymtracker.scanner.ScannerActivity;
import com.grandi.lorenzo.gymtracker.taskactivity.ProfileActivity;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class HomeActivity extends FragmentActivity {

    private TextView tv_date, tv_name, tv_account_id;
    private ImageButton ib_profile, ib_scanner, ib_settings;

    private String name, account_id;
    private boolean training;

    private static final String TAG = "Home";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerLogin();


        preferenceLoader(this);
        initViewComponents(this);
        initTaskComponents();
        preferenceSaver(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendEmail();
        finishAffinity();
    }

    private SharedPreferences preferenceLoader(Activity activity) {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);

        return sharedPreferences;
    }
    private void preferenceSaver(Activity activity) {
        SharedPreferences sharedPreferences;
        sharedPreferences = activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nameKey.getValue(), this.name);
        editor.putString(accountIdKey.getValue(), this.account_id);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewComponents(Activity activity) {
        this.tv_name = activity.findViewById(R.id.tv_name);
        this.tv_account_id = activity.findViewById(R.id.tvLogin);
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
        this.name = preferenceLoader(this).getString(nameKey.getValue(), nameKey.getValue());
        this.account_id = preferenceLoader(this).getString(accountIdKey.getValue(), accountIdKey.getValue());
        this.training = preferenceLoader(this).getBoolean(trainingKey.getValue(), false);

        if (!this.name.isEmpty() && !this.name.equals(nameKey.getValue())) this.tv_name.setText(this.name);
        if (!this.account_id.isEmpty() && !this.account_id.equals(nameKey.getValue()))
            this.tv_account_id.setText(this.account_id);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void trainingStatus(boolean training) {
        if (training)
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_exit));
        else
            this.ib_scanner.setBackground(getDrawable(R.drawable.button_scanner_enter));
    }

    private void registerLogin() {
        String name, date, registration;
        name = this.preferenceLoader(this).getString(nameKey.getValue(), nameKey.getValue());
        date = new CalendarHandler().getDateComplete();
        registration = " > " + name + " logged in ---\t" + date + "\n";
        try {
            FileOutputStream registerFile = openFileOutput(this.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()), MODE_PRIVATE);
            registerFile.write(registration.getBytes());
            registerFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readSavesAtOpen() {
        String registrations;
        try {
            FileInputStream registrationStream = this.openFileInput(this.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()));
            InputStreamReader inputStreamReader = new InputStreamReader(registrationStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)){
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                registrations = stringBuilder.toString();
                registrationStream.close();
                inputStreamReader.close();
            }
            return registrations;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Failed loading files", "");
        }
        return "";
    }

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void sendEmail() {
        // TODO: email registration fix

        String registrations = readSavesAtOpen();
        if (!registrations.isEmpty()) {
            Uri uri = Uri.parse("mailto:roretsuno98@gmail.com")
                    .buildUpon()
                    .appendQueryParameter("subject", "registration event")
                    .appendQueryParameter("body", registrations)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO,uri);


            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
                Log.e("Email sending : ", "done");
            } else
                Log.e("Email sending failed", "");
        }
    }
}