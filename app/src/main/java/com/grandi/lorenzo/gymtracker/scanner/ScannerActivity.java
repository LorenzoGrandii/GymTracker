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

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.task.CalendarHandler;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class ScannerActivity extends AppCompatActivity {

    private boolean trainingStatus;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private TextView tv_date_scanner;
    private SurfaceView surfaceView;
    private Context context;

    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

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
        preferenceSaver();
        qrCodeDispatcher();
        super.onStop();
    }

    @Override
    protected void onPause() {
        preferenceSaver();
        qrCodeDispatcher();
        super.onPause();
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
        this.surfaceView = this.findViewById(R.id.previewView);
    }
    private void initTaskComponents() {
        this.context = this;
        this.trainingStatus = preferenceLoader().getBoolean(trainingKey.getValue(), false);
        this.tv_date_scanner.setText((new CalendarHandler()).getDate());
        this.cameraManager();
    }

    private void cameraManager() {
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        this.cameraSource = new CameraSource.Builder(this, this.barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        cameraSource.start(surfaceView.getHolder());
                    else
                        ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
                cameraSource.release();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                Log.e("Code found","");
                if (qrCodes.size()!=0 && qrCodes.valueAt(0).displayValue.equals(strQRFlag.getValue())) {
                    Log.e("Code right","");
                    SharedPreferences.Editor editor = preferenceLoader().edit();
                    editor.putBoolean(trainingKey.getValue(), !trainingStatus);
                    editor.apply();
                    registerEvent();
                    qrCodeDispatcher();
                }
            }
        });
    }

    private void qrCodeDispatcher() {
        preferenceSaver();
        cameraSource.stop();
        cameraSource.release();
        barcodeDetector.release();
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