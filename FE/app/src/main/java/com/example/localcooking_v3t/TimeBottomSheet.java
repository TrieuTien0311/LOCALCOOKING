package com.example.localcooking_v3t;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

public class TimeBottomSheet extends BottomSheetDialogFragment {

    public interface OnTimeRangeSelectedListener {
        void onTimeRangeSelected(int startHour, int startMinute, int endHour, int endMinute);
    }
    public interface OnFilterAppliedListener {
        void onFilterApplied(
                int startHour, int startMinute,
                int endHour, int endMinute,
                int sortType // 0: mặc định, 1: sớm nhất, 2: muộn nhất
        );
    }
    private OnFilterAppliedListener listener;

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }
    private TextView tvStart, tvEnd, btnDong, btnApDung, btnXoaLoc;
    private RangeSlider slider;
    private RadioGroup radioGroup;
    private int currentSort = 0; // 0: mặc định, 1: sớm nhất, 2: muộn nhất

    // THÊM ĐOẠN NÀY ĐỂ CÓ BO GÓC
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            View sheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (sheet != null) sheet.setBackgroundResource(android.R.color.transparent);
        });
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_time, container, false);

        slider = view.findViewById(R.id.timeRangeSlider);
        tvStart = view.findViewById(R.id.cvStart);
        tvEnd = view.findViewById(R.id.cvEnd);
        btnDong = view.findViewById(R.id.btnDong);
        btnApDung = view.findViewById(R.id.btnApDung);
        btnXoaLoc = view.findViewById(R.id.btnXoaLoc);
        radioGroup = view.findViewById(R.id.radioGroupSapXep);

        slider.addOnChangeListener((s, value, fromUser) -> {
            if (!fromUser) return;
            List<Float> values = s.getValues();
            int startIdx = Math.round(values.get(0));
            int endIdx = Math.round(values.get(1));

            if (endIdx - startIdx < 1) {
                int active = s.getActiveThumbIndex();
                if (active == 0) startIdx = endIdx - 1;
                else endIdx = startIdx + 1;
                s.setValues((float) startIdx, (float) endIdx);
            }

            String startText = formatTime(startIdx);
            String endText = formatTime(endIdx);

            tvStart.setText("Từ " + startText);
            tvEnd.setText("Đến " + endText);
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdMacDinh) currentSort = 0;
            else if (checkedId == R.id.rdGiaGiamDan) currentSort = 1;
            else if (checkedId == R.id.rdGiaTangDan) currentSort = 2;
        });

        btnDong.setOnClickListener(v -> dismiss());

        btnXoaLoc.setOnClickListener(v -> {
            slider.setValues(0f, 28f);
            radioGroup.check(R.id.rdMacDinh);
            tvEnd.setText("Đến 22:00");
            tvStart.setText("Từ 08:00");
        });

        btnApDung.setOnClickListener(v -> {
            List<Float> values = slider.getValues();
            int startIdx = Math.round(values.get(0));
            int endIdx = Math.round(values.get(1));

            int startHour = 8 + startIdx / 2;
            int startMin = (startIdx % 2 == 0) ? 0 : 30;
            int endHour = 8 + endIdx / 2;
            int endMin = (endIdx % 2 == 0) ? 0 : 30;

            if (listener != null) {
                listener.onFilterApplied(startHour, startMin, endHour, endMin, currentSort);
            }
            dismiss();
        });

        return view;
    }
    private String formatTime(int index) {
        int hour = 8 + index / 2;
        int minute = (index % 2 == 0) ? 0 : 30;
        return String.format("%02d:%02d", hour, minute);
    }
}