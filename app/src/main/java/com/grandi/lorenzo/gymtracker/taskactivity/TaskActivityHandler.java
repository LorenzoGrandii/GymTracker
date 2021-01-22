package com.grandi.lorenzo.gymtracker.taskactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.R;
import com.grandi.lorenzo.gymtracker.home.HomeHandler;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class TaskActivityHandler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_handler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeHandler.class);
        new FlagList(intent);
        intent.putExtra(EXTRA_TASK_SELECTOR.getValue(), "");
        startActivity(intent);
    }
}