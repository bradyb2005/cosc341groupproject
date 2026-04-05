package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnGoToCalendar).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConsistencyCalendar.class);
            startActivity(intent);
        });
    }
}