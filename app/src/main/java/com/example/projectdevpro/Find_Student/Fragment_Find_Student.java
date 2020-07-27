package com.example.projectdevpro.Find_Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.FragmentFindStudentBinding;

public class Fragment_Find_Student extends Fragment {
    FragmentFindStudentBinding binding;

    public static Fragment_Find_Student newInstance() {
        Bundle args = new Bundle();
        Fragment_Find_Student fragment = new Fragment_Find_Student();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_student, container, false);
        return binding.getRoot();
    }

}
