package com.example.cosc341project;

import java.util.Map;

public class Medication {
    public String id;
    public String name;
    public String nickname;
    public String instructions;
    public String dosage;
    public String dosageUnit;
    public int dosesRemaining;
    public int refillsRemaining;
    public Map<String, Map<String, Boolean>> schedule; // Day -> (TimeOfDay -> Selected)
    public Map<String, String> notificationTimes; // TimeOfDay -> Time (e.g., "Morning" -> "08:00")

    public Medication() {
        // Default constructor required for calls to DataSnapshot.getValue(Medication.class)
    }

    public Medication(String id, String name, String nickname, String instructions, String dosage, 
                      String dosageUnit, int dosesRemaining, int refillsRemaining, 
                      Map<String, Map<String, Boolean>> schedule, Map<String, String> notificationTimes) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.instructions = instructions;
        this.dosage = dosage;
        this.dosageUnit = dosageUnit;
        this.dosesRemaining = dosesRemaining;
        this.refillsRemaining = refillsRemaining;
        this.schedule = schedule;
        this.notificationTimes = notificationTimes;
    }
}