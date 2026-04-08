package com.example.cosc341project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ConsistencyCalendar extends AppCompatActivity {

    private TextView currentMonthText;
    private GridView calendarGrid;
    private Calendar currentCalendar;
    private Calendar today;
    private CalendarAdapter adapter;
    
    private TextView detailMedication;
    private TextView detailStatus;
    private View detailsPanel;
    private List<DayInfo> currentDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consistency_calendar);

        currentMonthText = findViewById(R.id.currentMonth);
        calendarGrid = findViewById(R.id.calendarGrid);
        detailMedication = findViewById(R.id.detailMedication);
        detailStatus = findViewById(R.id.detailStatus);
        detailsPanel = findViewById(R.id.detailsPanel);

        // Placeholder: Setting "today" to April 9th
        today = Calendar.getInstance();
        today.set(Calendar.MONTH, Calendar.APRIL);
        today.set(Calendar.DAY_OF_MONTH, 9);
        
        currentCalendar = (Calendar) today.clone();

        updateCalendar();

        // back to main page
        findViewById(R.id.backBtnBox).setOnClickListener(v -> finish());
        
        // jumping to current day (moving callender to current month)
        findViewById(R.id.todayBtnBox).setOnClickListener(v -> resetToToday());

        // linking button to add new medication
        findViewById(R.id.addMedBtnBox).setOnClickListener(v -> {
            Intent intent = new Intent(ConsistencyCalendar.this, AddMedication.class);
            startActivity(intent);
        });

        // linking button for emergency options
        // required to be at bottom of every page
        findViewById(R.id.emergencyBtnBox).setOnClickListener(v -> {
            // This would link to the Emergency Protocol activity
            Toast.makeText(this, "Opening Emergency Protocol...", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.prevMonthBox).setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        findViewById(R.id.nextMonthBox).setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });
        
        showDetailsForDay(today.get(Calendar.DAY_OF_MONTH));
    }

    private void resetToToday() {
        currentCalendar = (Calendar) today.clone();
        updateCalendar();
        showDetailsForDay(today.get(Calendar.DAY_OF_MONTH));
        if (adapter != null) {
            adapter.setSelectedDay(today.get(Calendar.DAY_OF_MONTH));
        }
    }

    private void updateCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        currentMonthText.setText(sdf.format(currentCalendar.getTime()));

        currentDays = new ArrayList<>();
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        boolean isMarch = cal.get(Calendar.MONTH) == Calendar.MARCH;
        Random random = new Random(cal.get(Calendar.MONTH));

        for (int i = 1; i <= maxDays; i++) {
            boolean taken = random.nextInt(10) > 3;
            if (isMarch) {
                if (i == 4 || i == 7 || i == 12 || i == 13 || i == 15) taken = false;
                else if (i < 16) taken = true;
            }
            
            Boolean status = null;
            if (cal.get(Calendar.YEAR) < today.get(Calendar.YEAR) || 
                (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) && cal.get(Calendar.MONTH) < today.get(Calendar.MONTH)) ||
                (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) && i < today.get(Calendar.DAY_OF_MONTH))) {
                status = taken;
            }

            currentDays.add(new DayInfo(i, status));
        }

        adapter = new CalendarAdapter(currentDays);
        calendarGrid.setAdapter(adapter);
        
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            DayInfo day = (DayInfo) adapter.getItem(position);
            if (day != null) {
                showDetailsForDay(day.day);
                adapter.setSelectedDay(day.day);
            }
        });
        
        int rows = (int) Math.ceil(currentDays.size() / 7.0);
        ViewGroup.LayoutParams params = calendarGrid.getLayoutParams();
        params.height = rows * 180; 
        calendarGrid.setLayoutParams(params);
    }

    private void showDetailsForDay(int dayNum) {
        detailMedication.setText("Aspirin");
        
        DayInfo selectedDay = null;
        for (DayInfo day : currentDays) {
            if (day.day == dayNum) {
                selectedDay = day;
                break;
            }
        }

        if (selectedDay == null) return;

        boolean isToday = currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && 
                          currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && 
                          dayNum == today.get(Calendar.DAY_OF_MONTH);

        if (isToday || selectedDay.taken == null) {
            detailStatus.setText("Scheduled for 9:01AM");
            detailStatus.setTextColor(Color.parseColor("#1976D2"));
            detailsPanel.setBackgroundResource(R.drawable.thick_blue_box);
        } else if (selectedDay.taken) {
            detailStatus.setText("✓ Taken at 9:01AM");
            detailStatus.setTextColor(Color.BLACK);
            detailsPanel.setBackgroundResource(R.drawable.thick_green_box);
        } else {
            detailStatus.setText("✗ Missed");
            detailStatus.setTextColor(Color.BLACK);
            detailsPanel.setBackgroundResource(R.drawable.thick_red_box);
        }
    }

    private static class DayInfo {
        int day;
        Boolean taken;
        DayInfo(int day, Boolean taken) { this.day = day; this.taken = taken; }
    }

    private class CalendarAdapter extends BaseAdapter {
        private final List<DayInfo> days;
        private int selectedDay = -1;

        CalendarAdapter(List<DayInfo> days) {
            this.days = days;
            if (currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                selectedDay = today.get(Calendar.DAY_OF_MONTH);
            }
        }
        
        public void setSelectedDay(int day) {
            this.selectedDay = day;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return days.size(); }
        @Override
        public Object getItem(int position) { return days.get(position); }
        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ConsistencyCalendar.this).inflate(R.layout.calendar_day_item, parent, false);
            }

            TextView dayNumber = convertView.findViewById(R.id.dayNumber);
            TextView statusSymbol = convertView.findViewById(R.id.statusSymbol);
            View selectionCircle = convertView.findViewById(R.id.selectionCircle);
            DayInfo day = days.get(position);

            if (day != null) {
                dayNumber.setText(String.valueOf(day.day));
                
                boolean isToday = currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && 
                                  currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && 
                                  day.day == today.get(Calendar.DAY_OF_MONTH);

                if (isToday) {
                    statusSymbol.setVisibility(View.GONE);
                    convertView.setBackgroundColor(Color.parseColor("#BBDEFB"));
                    selectionCircle.setVisibility(View.VISIBLE);
                } else if (day.taken != null) {
                    statusSymbol.setVisibility(View.VISIBLE);
                    statusSymbol.setText(day.taken ? "✓" : "X");
                    convertView.setBackgroundColor(day.taken ? Color.parseColor("#A5D6A7") : Color.parseColor("#EF9A9A"));
                    selectionCircle.setVisibility(day.day == selectedDay ? View.VISIBLE : View.GONE);
                } else {
                    statusSymbol.setVisibility(View.GONE);
                    convertView.setBackgroundColor(Color.WHITE);
                    selectionCircle.setVisibility(day.day == selectedDay ? View.VISIBLE : View.GONE);
                }
            }
            return convertView;
        }
    }
}
