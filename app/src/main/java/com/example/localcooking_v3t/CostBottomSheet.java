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

import java.text.DecimalFormat;
import java.util.List;

public class CostBottomSheet extends BottomSheetDialogFragment {

    public interface OnFilterAppliedListener {
        void onFilterApplied(
                int minCost,
                int maxCost,
                int sortType // 0: mặc định, 1: giá giảm dần, 2: giá tăng dần
        );
    }

    private OnFilterAppliedListener listener;

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    private TextView tvStart, tvEnd, btnDong, btnApDung, btnXoaLoc;
    private RangeSlider slider;
    private RadioGroup radioGroup;
    private int currentSort = 0; // 0: mặc định, 1: giá giảm dần, 2: giá tăng dần
    private DecimalFormat formatter = new DecimalFormat("#,###");

    // Bo góc cho BottomSheet
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
        View view = inflater.inflate(R.layout.bottom_sheet_cost, container, false);

        // Ánh xạ các view
        slider = view.findViewById(R.id.timeRangeSlider);
        tvStart = view.findViewById(R.id.cvStart);
        tvEnd = view.findViewById(R.id.cvEnd);
        btnDong = view.findViewById(R.id.btnDong);
        btnApDung = view.findViewById(R.id.btnApDung);
        btnXoaLoc = view.findViewById(R.id.btnXoaLoc);
        radioGroup = view.findViewById(R.id.radioGroupSapXep);

        // Thiết lập khoảng cách tối thiểu giữa 2 thumb
        slider.setMinSeparationValue(100000f);

        // Xử lý sự kiện thay đổi slider
        slider.addOnChangeListener((s, value, fromUser) -> {
            if (!fromUser) return;

            List<Float> values = s.getValues();
            int minCost = Math.round(values.get(0));
            int maxCost = Math.round(values.get(1));

            // Đảm bảo khoảng cách tối thiểu 100,000
            if (maxCost - minCost < 100000) {
                int active = s.getActiveThumbIndex();
                if (active == 0) {
                    minCost = maxCost - 100000;
                } else {
                    maxCost = minCost + 100000;
                }
                s.setValues((float) minCost, (float) maxCost);
            }

            // Cập nhật TextView
            tvStart.setText(formatter.format(minCost) + " ₫");
            tvEnd.setText(formatter.format(maxCost) + " ₫");
        });

        // Hiển thị giá trị ban đầu
        List<Float> initialValues = slider.getValues();
        tvStart.setText(formatter.format(initialValues.get(0).intValue()) + " ₫");
        tvEnd.setText(formatter.format(initialValues.get(1).intValue()) + " ₫");

        // Xử lý RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdMacDinh) currentSort = 0;
            else if (checkedId == R.id.rdGiaGiamDan) currentSort = 1;
            else if (checkedId == R.id.rdGiaTangDan) currentSort = 2;
        });

        // Nút Đóng
        btnDong.setOnClickListener(v -> dismiss());

        // Nút Xóa lọc - reset về giá trị mặc định
        btnXoaLoc.setOnClickListener(v -> {
            slider.setValues(500000f, 3000000f);
            radioGroup.check(R.id.rdMacDinh);
            currentSort = 0;
            tvStart.setText(formatter.format(500000) + " ₫");
            tvEnd.setText(formatter.format(3000000) + " ₫");
        });

        // Nút Áp dụng
        btnApDung.setOnClickListener(v -> {
            List<Float> values = slider.getValues();
            int minCost = Math.round(values.get(0));
            int maxCost = Math.round(values.get(1));

            if (listener != null) {
                listener.onFilterApplied(minCost, maxCost, currentSort);
            }
            dismiss();
        });

        return view;
    }
}