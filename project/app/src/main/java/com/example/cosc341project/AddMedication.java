package com.example.cosc341project;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddMedication extends AppCompatActivity {

    private TextInputEditText nameInput, nicknameInput, instructionsInput, dosageInput, dosesRemainInput, refillsRemainInput;
    private TextInputEditText timeMorn, timeNoon, timeEve, timeNight;
    private Spinner dosageUnitsSpinner;
    private Button saveButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medication);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance("https://cosc341project-66346-default-rtdb.firebaseio.com/").getReference();

        // Initialize Views
        nameInput = findViewById(R.id.nameInput);
        nicknameInput = findViewById(R.id.nicknameInput);
        instructionsInput = findViewById(R.id.instructionsInput);
        dosageInput = findViewById(R.id.dosageInput);
        dosesRemainInput = findViewById(R.id.dosesRemainInput);
        refillsRemainInput = findViewById(R.id.refillsRemainInput);
        
        timeMorn = findViewById(R.id.editTextTime);
        timeNoon = findViewById(R.id.editTextTime2);
        timeEve = findViewById(R.id.editTextTime3);
        timeNight = findViewById(R.id.editTextTime4);

        dosageUnitsSpinner = findViewById(R.id.DosageUnits);
        saveButton = findViewById(R.id.saveButton);

        // Setup Spinner
        String[] units = {"mg", "mcg", "g", "ml", "pills", "capsules"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosageUnitsSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication();
            }
        });
    }

    private void saveMedication() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameInput.setError("Required");
            return;
        }

        String id = mDatabase.child("medications").push().getKey();
        
        Map<String, Map<String, Boolean>> schedule = new HashMap<>();
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] times = {"Morn", "Noon", "Eve", "Night"};

        for (String day : days) {
            Map<String, Boolean> daySchedule = new HashMap<>();
            for (String time : times) {
                int resId = getResources().getIdentifier("cb" + day + time, "id", getPackageName());
                CheckBox cb = findViewById(resId);
                daySchedule.put(time, cb.isChecked());
            }
            schedule.put(day, daySchedule);
        }

        Map<String, String> notificationTimes = new HashMap<>();
        notificationTimes.put("Morning", timeMorn.getText().toString());
        notificationTimes.put("Noon", timeNoon.getText().toString());
        notificationTimes.put("Evening", timeEve.getText().toString());
        notificationTimes.put("Night", timeNight.getText().toString());

        Medication medication = new Medication(
                id,
                name,
                nicknameInput.getText().toString(),
                instructionsInput.getText().toString(),
                dosageInput.getText().toString(),
                dosageUnitsSpinner.getSelectedItem().toString(),
                Integer.parseInt(dosesRemainInput.getText().toString().isEmpty() ? "0" : dosesRemainInput.getText().toString()),
                Integer.parseInt(refillsRemainInput.getText().toString().isEmpty() ? "0" : refillsRemainInput.getText().toString()),
                schedule,
                notificationTimes
        );

        if (id != null) {
            mDatabase.child("medications").child(id).setValue(medication)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddMedication.this, "Medication Saved", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddMedication.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}