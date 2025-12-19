package com.example.localcooking_v3t;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MonthViewHolder> {

    private List<CalendarMonth> months;
    private Calendar selectedDate;
    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        void onDateSelected(CalendarDay day);
    }

    public CalendarAdapter(List<CalendarMonth> months, Calendar selectedDate, OnDateSelectedListener listener) {
        this.months = months;
        this.selectedDate = selectedDate;
        this.onDateSelectedListener = listener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_month, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        CalendarMonth calendarMonth = months.get(position);

        // Set month title
        String[] monthNames = {
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
                "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };
        holder.tvMonthYear.setText(monthNames[calendarMonth.getMonth()] + ", " + calendarMonth.getYear());

        // Clear previous views
        holder.gridCalendar.removeAllViews();
        holder.gridCalendar.setColumnCount(7);

        // Add day views to grid
        for (CalendarDay day : calendarMonth.getDays()) {
            View dayView = createDayView(holder.itemView.getContext(), day);

            // --- [FIX QUAN TRỌNG] ---
            // Thiết lập LayoutParams bằng code để chia đều 7 cột
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Quan trọng: Đặt width = 0 để weight hoạt động
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            // weight = 1f giúp các cột chia đều nhau
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            // Thiết lập margin nếu cần (để sát nhau thì để 0)
            params.setMargins(0, 0, 0, 0);

            dayView.setLayoutParams(params);
            // ------------------------

            holder.gridCalendar.addView(dayView);
        }
    }

    private View createDayView(android.content.Context context, CalendarDay day) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Lưu ý: Đảm bảo file xml của bạn tên là item_calendar_day
        View dayView = inflater.inflate(R.layout.item_calendar_day, null);

        FrameLayout frameDay = dayView.findViewById(R.id.frameDay);
        TextView tvDay = dayView.findViewById(R.id.tvDay);

        if (day.isCurrentMonth()) {
            tvDay.setText(String.valueOf(day.getDay()));

            // Check if this day is selected
            boolean isSelected = false;
            if (selectedDate != null) {
                isSelected = selectedDate.get(Calendar.YEAR) == day.getYear() &&
                        selectedDate.get(Calendar.MONTH) == day.getMonth() &&
                        selectedDate.get(Calendar.DAY_OF_MONTH) == day.getDay();
            }

            // Kiểm tra nếu là ngày trong quá khứ
            if (day.isPast()) {
                // Ngày quá khứ: màu xám mờ và không thể click
                tvDay.setTextColor(Color.parseColor("#CCCCCC"));
                tvDay.setBackground(null);
                tvDay.setAlpha(0.5f);
                frameDay.setClickable(false);
                frameDay.setEnabled(false);
            } else if (isSelected) {
                // Selected day style
                tvDay.setBackgroundResource(R.drawable.selected_day_bg);
                tvDay.setTextColor(Color.WHITE);
                tvDay.setAlpha(1.0f);
            } else if (day.isSunday()) {
                // Sunday style
                tvDay.setTextColor(Color.parseColor("#E74C3C"));
                tvDay.setBackground(null);
                tvDay.setAlpha(1.0f);
            } else {
                // Normal day style
                tvDay.setTextColor(Color.parseColor("#000000"));
                tvDay.setBackground(null);
                tvDay.setAlpha(1.0f);
            }

            // Click listener - chỉ cho phép click nếu không phải ngày quá khứ
            if (!day.isPast()) {
                frameDay.setOnClickListener(v -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(day.getYear(), day.getMonth(), day.getDay());
                    selectedDate = cal;
                    if (onDateSelectedListener != null) {
                        onDateSelectedListener.onDateSelected(day);
                    }
                    notifyDataSetChanged();
                });
            }
        } else {
            // Days from other months
            tvDay.setText(String.valueOf(day.getDay()));
            tvDay.setTextColor(Color.parseColor("#CCCCCC"));
            tvDay.setAlpha(0.5f);
            frameDay.setClickable(false);
        }

        return dayView;
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public void updateSelectedDate(Calendar date) {
        this.selectedDate = date;
        notifyDataSetChanged();
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonthYear;
        GridLayout gridCalendar;

        MonthViewHolder(View view) {
            super(view);
            tvMonthYear = view.findViewById(R.id.tvMonthYear);
            gridCalendar = view.findViewById(R.id.gridCalendar);
        }
    }
}