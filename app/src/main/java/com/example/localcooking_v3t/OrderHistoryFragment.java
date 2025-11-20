package com.example.localcooking_v3t;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderHistoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ views
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.ViewHienThi);

        // Setup ViewPager2 với adapter
        OrderHistoryPagerAdapter adapter = new OrderHistoryPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đặt trước");
                        break;
                    case 1:
                        tab.setText("Đã hoàn thành");
                        break;
                    case 2:
                        tab.setText("Đã huỷ");
                        break;
                }
            }
        }).attach();
    }

    // Adapter cho ViewPager2 (Inner class)
    private class OrderHistoryPagerAdapter extends FragmentStateAdapter {

        public OrderHistoryPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SuccessfulOrderFragment(); // Tab "Đặt trước"
                case 1:
                    return new SuccessfulOrderFragment(); // Tab "Đã hoàn thành"
                case 2:
                    return new SuccessfulOrderFragment(); // Tab "Đã huỷ"
                default:
                    return new SuccessfulOrderFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // 3 tabs
        }
    }
}