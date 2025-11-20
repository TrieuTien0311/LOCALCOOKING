package com.example.localcooking_v3t;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DetailDescriptionFragment extends Fragment {

    private ImageView btnDown;
    private LinearLayout txtAn;
    private boolean isExpanded = false;

    public DetailDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailDescriptionFragment newInstance() {
        return new DetailDescriptionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_description, container, false);

        // Khởi tạo các view
        btnDown = view.findViewById(R.id.btnDown);
        txtAn = view.findViewById(R.id.txtAn);

        // Xử lý sự kiện click
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Đóng lại
                    btnDown.animate().rotation(0).setDuration(300).start();
                    txtAn.setVisibility(View.GONE);
                    isExpanded = false;
                } else {
                    // Mở ra
                    btnDown.animate().rotation(180).setDuration(300).start();
                    txtAn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
            }
        });

        return view;
    }
}