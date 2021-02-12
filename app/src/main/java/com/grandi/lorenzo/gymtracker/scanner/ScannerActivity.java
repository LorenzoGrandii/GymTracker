package com.grandi.lorenzo.gymtracker.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class ScannerActivity extends AppCompatActivity {

    private boolean trainingStatus;

    private TextView tv_date_scanner;
    private Context context;

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scanner);
        this.initViewComponents();
        this.initTaskComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferenceSaver();
        qrCodeDispatcher();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferenceSaver();
        qrCodeDispatcher();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.initViewComponents();
        this.initTaskComponents();
    }

    @Override
    public void onBackPressed() {
        qrCodeDispatcher();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    private SharedPreferences preferenceLoader() {
        return this.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE);
    }

    private void preferenceSaver() {
        SharedPreferences.Editor editor = this.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), MODE_PRIVATE).edit();
        editor.putBoolean(trainingKey.getValue(), this.trainingStatus);
        editor.apply();
    }

    private void initViewComponents() {
        this.tv_date_scanner = this.findViewById(R.id.tv_date_scanner);
        Log.e("date_scanner","created");
        codeScannerView = findViewById(R.id.previewView);
        Log.e("previewView","created");
    }
    private void initTaskComponents() {
        this.context = this;
        this.trainingStatus = preferenceLoader().getBoolean(trainingKey.getValue(), false);
        this.tv_date_scanner.setText((new CalendarHandler()).getDate());
        // this.cameraManager();

        codeScanner = new CodeScanner(this, codeScannerView);
        Log.e("codeScanner","created");
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Log.e("callback","set");
            if (result.getText().equals(strQRFlag.getValue())){
                Log.e("Code right","");
                SharedPreferences.Editor editor = preferenceLoader().edit();
                editor.putBoolean(trainingKey.getValue(), !trainingStatus);
                editor.apply();
                registerEvent();
                qrCodeDispatcher();
            }
        }));
        codeScannerView.setOnClickListener(v -> {
            codeScanner.startPreview();
        });
    }



    private void qrCodeDispatcher() {
        preferenceSaver();
        codeScanner.stopPreview();
        codeScanner.releaseResources();
    }

    private void registerEvent() {
            String name, date, registration;
            name = preferenceLoader().getString(nameKey.getValue(), nameKey.getValue());
            date = new CalendarHandler().getDateComplete();
            if (trainingStatus) registration = " > " + name + " left the gym :\t" + date + "\n";
            else registration = " > " + name + " joined the gym :\t" + date + "\n";
            try {
                FileOutputStream eventRegister = openFileOutput(context.getFilesDir().getName().concat(REGISTRATION_FILE.getValue()), Context.MODE_PRIVATE);
                eventRegister.write(registration.getBytes());
                eventRegister.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}