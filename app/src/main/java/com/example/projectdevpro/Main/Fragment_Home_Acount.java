package com.example.projectdevpro.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.projectdevpro.Find_Teacher.View.Fragment_Information_Teacher;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;
import com.example.projectdevpro.ViewPagerAdapter;
import com.example.projectdevpro.databinding.FragmentHomeAcountBinding;


public class Fragment_Home_Acount extends Fragment {
    FragmentHomeAcountBinding binding;
    Teacher teacher = null;

    public static Fragment_Home_Acount newInstance(Bundle args){
        Fragment_Home_Acount fragment = new Fragment_Home_Acount();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_acount, container, false);
        Bundle bundle = getArguments();
        setupViewPager(binding.viewpagerHome, bundle);
        binding.tabHome.setupWithViewPager(binding.viewpagerHome);
        setupTabIcons();
        return binding.getRoot();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_home,
                R.drawable.student,
                R.drawable.teacher,
                R.drawable.ic_manager,
                R.drawable.ic_person_24dp
        };

        binding.tabHome.getTabAt(0).setIcon(tabIcons[0]);
        binding.tabHome.getTabAt(1).setIcon(tabIcons[1]);
        binding.tabHome.getTabAt(2).setIcon(tabIcons[2]);
        binding.tabHome.getTabAt(3).setIcon(tabIcons[3]);
        binding.tabHome.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager, Bundle bundle) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(Fragment_Information_Teacher.newInstance(bundle), "PROFILE");
        adapter.addFrag(Fragment_Information_Teacher.newInstance(bundle), "PROFILE");
        adapter.addFrag(Fragment_Information_Teacher.newInstance(bundle), "PROFILE");
        adapter.addFrag(Fragment_Information_Teacher.newInstance(bundle), "PROFILE");
        adapter.addFrag(Fragment_Information_Teacher.newInstance(bundle), "PROFILE");
        viewPager.setAdapter(adapter);
    }
}
