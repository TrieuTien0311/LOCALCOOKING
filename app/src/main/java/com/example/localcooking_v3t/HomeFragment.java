package com.example.localcooking_v3t;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color; // [MỚI] Import Color
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window; // [MỚI] Import Window
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat; // [MỚI]
import androidx.core.view.WindowInsetsControllerCompat; // [MỚI]
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private ImageView ivLogo, ivArrow;
    private TextView tvAppName, tvHello, tvCurrentLocation, tvDestination, tvDate;
    private TextView tvViewAll;
    private Button btnSearch;

    private ActivityResultLauncher<Intent> calendarLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String selectedDate = result.getData().getStringExtra("selected_date");
                        if (tvDate != null) {
                            tvDate.setText(selectedDate);
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ view
        ivLogo = view.findViewById(R.id.ivLogo);
        ivArrow = view.findViewById(R.id.ivArrow);
        tvAppName = view.findViewById(R.id.tvAppName);
        tvHello = view.findViewById(R.id.tvHello);
        tvCurrentLocation = view.findViewById(R.id.tvCurrentLocation);
        tvDestination = view.findViewById(R.id.tvDestination);
        tvDate = view.findViewById(R.id.tvDate);
        tvViewAll = view.findViewById(R.id.tvViewAll);
        btnSearch = view.findViewById(R.id.btnSearch);

        // Xử lý sự kiện
        btnSearch.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tìm kiếm...", Toast.LENGTH_SHORT).show();
        });

        tvViewAll.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Xóa tất cả lịch sử", Toast.LENGTH_SHORT).show();
        });

        ivArrow.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Xem thông tin tài khoản", Toast.LENGTH_SHORT).show();
        });

        tvDate.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CalendarActivity.class);
            calendarLauncher.launch(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = requireActivity().getWindow();

        window.setStatusBarColor(Color.parseColor("#FFC59D"));

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }
    }
}