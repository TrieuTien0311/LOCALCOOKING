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

public class ArrangeBottomSheet extends BottomSheetDialogFragment {

    public interface OnSapXepListener {
        void onSapXepSelected(int loaiSapXep);
    }

    // Các hằng số cho loại sắp xếp
    public static final int MAC_DINH = 0;
    public static final int GIO_BAT_DAU_SOM_NHAT = 1;
    public static final int GIO_BAT_DAU_MUON_NHAT = 2;
    public static final int DANH_GIA_CAO_NHAT = 3;
    public static final int GIA_GIAM_DAN = 4;
    public static final int GIA_TANG_DAN = 5;

    private OnSapXepListener listener;
    private int currentSelection = MAC_DINH;

    public static ArrangeBottomSheet newInstance(int currentSelection) {
        ArrangeBottomSheet fragment = new ArrangeBottomSheet();
        Bundle args = new Bundle();
        args.putInt("current_selection", currentSelection);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnSapXepListener(OnSapXepListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentSelection = getArguments().getInt("current_selection", MAC_DINH);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Tạo BottomSheetDialog với background trong suốt để hiển thị bo tròn góc
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            View bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent);
            }
        });

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_arrange, container, false);

        TextView btnDong = view.findViewById(R.id.btnDong);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupSapXep);

        // Set lựa chọn hiện tại
        setCurrentRadioButton(radioGroup, currentSelection);

        // Xử lý khi nhấn "Đóng"
        btnDong.setOnClickListener(v -> dismiss());

        // Xử lý khi chọn radio button
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int loaiSapXep = getLoaiSapXepFromRadioId(checkedId);
            if (listener != null) {
                listener.onSapXepSelected(loaiSapXep);
            }
            dismiss();
        });

        return view;
    }

    private void setCurrentRadioButton(RadioGroup radioGroup, int loaiSapXep) {
        int radioId;
        switch (loaiSapXep) {
            case GIO_BAT_DAU_SOM_NHAT:
                radioId = R.id.radioGiaBatDauSomNhat;
                break;
            case GIO_BAT_DAU_MUON_NHAT:
                radioId = R.id.radioGiaBatDauMuonNhat;
                break;
            case DANH_GIA_CAO_NHAT:
                radioId = R.id.radioDanhGiaCaoNhat;
                break;
            case GIA_GIAM_DAN:
                radioId = R.id.radioGiaGiamDan;
                break;
            case GIA_TANG_DAN:
                radioId = R.id.radioGiaTangDan;
                break;
            default:
                radioId = R.id.radioMacDinh;
                break;
        }
        radioGroup.check(radioId);
    }

    private int getLoaiSapXepFromRadioId(int radioId) {
        if (radioId == R.id.radioGiaBatDauSomNhat) {
            return GIO_BAT_DAU_SOM_NHAT;
        } else if (radioId == R.id.radioGiaBatDauMuonNhat) {
            return GIO_BAT_DAU_MUON_NHAT;
        } else if (radioId == R.id.radioDanhGiaCaoNhat) {
            return DANH_GIA_CAO_NHAT;
        } else if (radioId == R.id.radioGiaGiamDan) {
            return GIA_GIAM_DAN;
        } else if (radioId == R.id.radioGiaTangDan) {
            return GIA_TANG_DAN;
        } else {
            return MAC_DINH;
        }
    }
}