package com.example.localcooking_v3t;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DetailDescriptionFragment extends Fragment {

    private ImageView btnDown;
    private LinearLayout txtAn;
    private boolean isExpanded = false;
    private RecyclerView rcvCategories;
    private CategoryAdapter categoryAdapter;
    private Class lopHoc;

    public DetailDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailDescriptionFragment newInstance(Class lopHoc) {
        DetailDescriptionFragment fragment = new DetailDescriptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.lopHoc = lopHoc;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_description, container, false);

        // Khởi tạo các view
        btnDown = view.findViewById(R.id.btnDown);
        txtAn = view.findViewById(R.id.txtAn);
        rcvCategories = view.findViewById(R.id.rcvCategories);

        // Xử lý sự kiện click expand/collapse thông tin giáo viên
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    btnDown.animate().rotation(0).setDuration(300).start();
                    txtAn.setVisibility(View.GONE);
                    isExpanded = false;
                } else {
                    btnDown.animate().rotation(180).setDuration(300).start();
                    txtAn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Setup RecyclerView cho lịch trình lớp học
        if (lopHoc != null && lopHoc.getLichTrinhLopHoc() != null) {
            rcvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
            categoryAdapter = new CategoryAdapter(lopHoc.getLichTrinhLopHoc());
            rcvCategories.setAdapter(categoryAdapter);
        }
    }

    public void setLopHoc(Class lopHoc) {
        this.lopHoc = lopHoc;
    }
}