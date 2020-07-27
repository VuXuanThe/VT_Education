package com.example.projectdevpro.Capacity_Test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.FragmentCapacityTestBinding;

public class Fragment_Capacity_Test extends Fragment {
    FragmentCapacityTestBinding binding;

    public static Fragment_Capacity_Test newInstance() {
        Bundle args = new Bundle();
        Fragment_Capacity_Test fragment = new Fragment_Capacity_Test();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_capacity_test, container, false);
        return binding.getRoot();
    }
}

