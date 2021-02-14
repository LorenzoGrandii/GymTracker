package com.grandi.lorenzo.gymtracker.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.globalClasses.FlagList;
import com.grandi.lorenzo.gymtracker.main.StarterActivity;

import static com.grandi.lorenzo.gymtracker.globalClasses.KeyLoader.*;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_handler);
        Intent intent = getIntent();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (intent.getBooleanExtra(EXTRA_ACCOUNT.getValue(), false)) {
            ft.replace(R.id.f_login_container, new FragmentLogin());
            ft.commit();
        } else if (!intent.getBooleanExtra(EXTRA_ACCOUNT.getValue(), false)){
            ft.replace(R.id.f_login_container, new FragmentNewAccount());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StarterActivity.class);
        new FlagList(intent);
        startActivity(intent);
    }
}