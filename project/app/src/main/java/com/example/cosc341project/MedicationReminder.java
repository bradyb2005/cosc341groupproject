package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MedicationReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reminder);

        // back to main page
        findViewById(R.id.backBtnBox).setOnClickListener(v -> finish());

        // goes to the taken confirmation screen
        findViewById(R.id.btnITookIt).setOnClickListener(v -> {
            Intent intent = new Intent(MedicationReminder.this, MedicationTaken.class);
            startActivity(intent);
        });

        // goes to the remind me later options
        findViewById(R.id.btnNotYet).setOnClickListener(v -> {
            Intent intent = new Intent(MedicationReminder.this, MedicationNotYet.class);
            startActivity(intent);
        });

        // goes to emergency options
        findViewById(R.id.btnEmergencyOptions).setOnClickListener(v -> {
            Intent intent = new Intent(MedicationReminder.this, EmergencyOptions.class);
            startActivity(intent);
        });
    }
}
