package com.example.cosc341project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_options);

        LinearLayout btnCallPerson = findViewById(R.id.btnCallPerson);
        LinearLayout btnCall911 = findViewById(R.id.btnCall911);
        LinearLayout btnCancel = findViewById(R.id.btnCancel);

        btnCallPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with actual phone number if available
                String phoneNumber = "1234567890"; 
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        btnCall911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
