package com.grandi.lorenzo.gymtracker.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grandi.lorenzo.gymtracker.globalClasses.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.login.LoginActivity;

import static com.grandi.lorenzo.gymtracker.globalClasses.KeyLoader.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class StarterActivity extends AppCompatActivity {

    private boolean isSpeakButtonLongPressed = false;

    TextView b_signup;
    Button b_signin;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewComponents(this);
        b_signin.setOnClickListener(v -> {
            v.setPressed(true);
            Intent intent = new Intent(this, LoginActivity.class);
            new FlagList(intent);
            intent.putExtra(EXTRA_ACCOUNT.getValue(), true);
            startActivity(intent);
        });
        b_signup.setOnClickListener(v -> {
            b_signup.setTextColor(getColor(R.color.white));
            Intent intent = new Intent(this, LoginActivity.class);
            new FlagList(intent);
            intent.putExtra(EXTRA_ACCOUNT.getValue(), false);
            startActivity(intent);
        });

        b_signin.setOnLongClickListener(v -> {
            v.setPressed(true);
            Toast.makeText(StarterActivity.this, getString(R.string.signin_hint), Toast.LENGTH_SHORT).show();
            return false;
        });
        b_signup.setOnLongClickListener(v -> {
            b_signup.setTextColor(getColor(R.color.white));
            Toast.makeText(StarterActivity.this, getString(R.string.signup_hint), Toast.LENGTH_SHORT).show();
            isSpeakButtonLongPressed = true;
            return true;
        });
        b_signup.setOnTouchListener(speakTouchListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        b_signup.setTextColor(getColor(R.color.color1));
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener speakTouchListener = (v, event) -> {
        v.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isSpeakButtonLongPressed) {
                isSpeakButtonLongPressed = false;
                b_signup.setTextColor(getColor(R.color.color1));
            }
        }
        return false;
    };

    private void initViewComponents (Activity activity) {
        this.b_signup = findViewById(R.id.b_signup);
        this.b_signup.setTextColor(getColor(R.color.color1));
        this.b_signin = findViewById(R.id.b_signin);
    }


}