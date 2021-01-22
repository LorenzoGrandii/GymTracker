package com.grandi.lorenzo.gymtracker.scanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeHandler;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;


import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class ScannerActivity extends AppCompatActivity {

    private boolean trainingStatus;

    private TextView tv_date_scanner;

    private CodeScanner codeScanner;
    private CodeScannerView scanner_view;
    private Vibrator v;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        initViewComponents();
        initTaskComponents();
        this.codeScanner.setDecodeCallback(result -> runOnUiThread(() -> qrCodeBind(result.getText())));
        scanner_view.setOnClickListener(v -> {
            this.codeScanner.startPreview();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferenceSaver(this);
        codeScanner.releaseResources();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferenceSaver(this);
        codeScanner.releaseResources();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        qrCodeDispatcher();
    }


    private SharedPreferences preferenceLoader(Activity activity) {
        return activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }
    private void preferenceSaver(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
        editor.apply();
    }
    private void initViewComponents () {
        this.scanner_view = this.findViewById(R.id.view_scanner);
        this.tv_date_scanner = this.findViewById(R.id.tv_date_scanner);
    }
    private void initTaskComponents () {
        this.codeScanner = new CodeScanner(this, scanner_view);
        this.trainingStatus = preferenceLoader(this).getBoolean(trainingKey.getValue(), false);
        this.tv_date_scanner.setText((new CalendarHandler()).getDate());
        this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void qrCodeBind(String qrcode) {
        if (qrcode.equals(strQRFlag.getValue())){
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            SharedPreferences.Editor editor = preferenceLoader(this).edit();
            editor.putBoolean(trainingKey.getValue(), !this.trainingStatus);
            editor.apply();
            qrCodeDispatcher();
        } else
            Toast.makeText(this, getString(R.string.toast_qrcode_scanned_error), Toast.LENGTH_SHORT).show();
    }

    private void qrCodeDispatcher() {
        this.codeScanner.releaseResources();
        Intent intent = new Intent(this, HomeHandler.class);
        new FlagList(intent);
        preferenceSaver(this);
        startActivity(intent);
    }
}