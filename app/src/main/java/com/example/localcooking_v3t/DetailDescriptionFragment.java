package com.example.localcooking_v3t;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailDescriptionFragment extends Fragment {

    public DetailDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailDescriptionFragment newInstance() {
        return new DetailDescriptionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_description, container, false);
    }
}