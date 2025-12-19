package com.example.localcooking_v3t;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CALENDAR = 1001;

    private ImageView btnBack;
    private TextView tvBack;
    private TextView tvSelectedDate;
    private RecyclerView recyclerCalendar;
    private Button btnConfirm;

    private CalendarAdapter calendarAdapter;
    private Calendar selectedCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        getWindow().setStatusBarColor(Color.parseColor("#FFCCAA"));

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        // EdgeToEdge setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupCalendar();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        recyclerCalendar = findViewById(R.id.recyclerCalendar);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    private void setupCalendar() {
        // Get current date or date from intent
        String receivedDate = getIntent().getStringExtra("selected_date");
        if (receivedDate != null && !receivedDate.isEmpty()) {
            // Parse the received date (format: "T4, 19/12/2024")
            parseDateString(receivedDate);
        }

        // Generate 4 months of calendar data (current month + 3 next months)
        int currentMonth = selectedCalendar.get(Calendar.MONTH);
        int currentYear = selectedCalendar.get(Calendar.YEAR);
        List<CalendarMonth> months = CalendarGenerator.generateMonths(currentMonth, currentYear, 4);

        // Setup RecyclerView
        calendarAdapter = new CalendarAdapter(
                months,
                selectedCalendar,
                day -> {
                    // Update selected date
                    selectedCalendar.set(day.getYear(), day.getMonth(), day.getDay());
                    updateSelectedDateText();
                }
        );

        recyclerCalendar.setLayoutManager(new LinearLayoutManager(this));
        recyclerCalendar.setAdapter(calendarAdapter);
        recyclerCalendar.setHasFixedSize(false);

        // Update initial date text
        updateSelectedDateText();
    }
    
    /**
     * Parse date string từ format "T4, 19/12/2024" thành Calendar
     */
    private void parseDateString(String dateString) {
        try {
            // Tách phần ngày/tháng/năm (bỏ phần thứ)
            String[] parts = dateString.split(", ");
            if (parts.length == 2) {
                String[] dateParts = parts[1].split("/");
                if (dateParts.length == 3) {
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1; // Calendar month is 0-based
                    int year = Integer.parseInt(dateParts[2]);
                    
                    selectedCalendar.set(year, month, day);
                }
            }
        } catch (Exception e) {
            // Nếu parse lỗi, giữ nguyên ngày hiện tại
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        if (tvBack != null) {
            tvBack.setOnClickListener(v -> finish());
        }

        btnConfirm.setOnClickListener(v -> {
            // Return selected date to previous activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_date", formatDate(selectedCalendar));
            resultIntent.putExtra("date_millis", selectedCalendar.getTimeInMillis());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void updateSelectedDateText() {
        tvSelectedDate.setText(formatDate(selectedCalendar));
    }

    private String formatDate(Calendar calendar) {
        String dayOfWeek;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: dayOfWeek = "CN"; break;
            case Calendar.MONDAY: dayOfWeek = "T2"; break;
            case Calendar.TUESDAY: dayOfWeek = "T3"; break;
            case Calendar.WEDNESDAY: dayOfWeek = "T4"; break;
            case Calendar.THURSDAY: dayOfWeek = "T5"; break;
            case Calendar.FRIDAY: dayOfWeek = "T6"; break;
            case Calendar.SATURDAY: dayOfWeek = "T7"; break;
            default: dayOfWeek = "";
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return String.format(Locale.getDefault(), "%s, %02d/%02d/%d", dayOfWeek, day, month, year);
    }
}