package com.example.cosc341project;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MedicationNotYet extends AppCompatActivity {

    private String selectedRemindOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_not_yet);

        // back to reminder screen
        findViewById(R.id.backBtnBox).setOnClickListener(v -> finish());

        // remind in 15 min option
        findViewById(R.id.btnRemind15).setOnClickListener(v -> {
            selectedRemindOption = "15 minutes";
            Toast.makeText(this, "Reminder set for 15 minutes", Toast.LENGTH_SHORT).show();
            finish();
        });

        // remind in 1 hr option
        findViewById(R.id.btnRemind1Hr).setOnClickListener(v -> {
            selectedRemindOption = "1 hour";
            Toast.makeText(this, "Reminder set for 1 hour", Toast.LENGTH_SHORT).show();
            finish();
        });

        // custom time picker thing
        findViewById(R.id.btnRemindAt).setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                String amPm = selectedHour >= 12 ? "PM" : "AM";
                int displayHour = selectedHour % 12;
                if (displayHour == 0) displayHour = 12;
                String timeStr = String.format(Locale.getDefault(), "%d:%02d %s", displayHour, selectedMinute, amPm);

                selectedRemindOption = timeStr;
                TextView btnText = findViewById(R.id.btnRemindAtText);
                btnText.setText("REMIND AT: " + timeStr);
                Toast.makeText(this, "Reminder set for " + timeStr, Toast.LENGTH_SHORT).show();
                finish();
            }, hour, minute, false);

            timePicker.show();
        });
    }
}
