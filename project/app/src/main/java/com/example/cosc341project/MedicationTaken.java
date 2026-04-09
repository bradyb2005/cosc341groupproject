package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MedicationTaken extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_taken);

        // back to home
        findViewById(R.id.backBtnBox).setOnClickListener(v -> {
            Intent intent = new Intent(MedicationTaken.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // linking to calendar page
        findViewById(R.id.btnViewCalendar).setOnClickListener(v -> {
            Intent intent = new Intent(MedicationTaken.this, ConsistencyCalendar.class);
            startActivity(intent);
        });
    }
}
