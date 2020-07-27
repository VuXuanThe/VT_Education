package com.example.projectdevpro.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.projectdevpro.Capacity_Test.Fragment_Capacity_Test;
import com.example.projectdevpro.Find_Student.Fragment_Find_Student;
import com.example.projectdevpro.Find_Teacher.View.Fragment_Find_Teacher;
import com.example.projectdevpro.Login.Fragment_Login;
import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.ActivityMainBinding;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Hide Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (!checkRequiredPermissions()) checkRequiredPermissions();
        getFragmentManager().popBackStack();
        init();
    }

    public void init(){
        Login();
        FindTeacher();
        FindStudent();
        CapacityTest();
    }

    public void CapacityTest(){
        binding.viewCapacityTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(Fragment_Capacity_Test.newInstance(), "Fragment_Capacity_Test");
            }
        });
    }

    public void FindTeacher(){
        binding.viewFindTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(Fragment_Find_Teacher.newInstance(), "Fragment_Find_Teacher");
            }
        });
    }

    public void FindStudent(){
        binding.viewFindStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(Fragment_Find_Student.newInstance(), "Fragment_Find_Student");
            }
        });
    }

    public void Login(){
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(Fragment_Login.newInstance(), "Fragment_Login");
            }
        });
    }

    public void getFragment(Fragment fragment, String nameFragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(nameFragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    private boolean checkRequiredPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.WAKE_LOCK};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.message_request_permission_read_phone_state),
                    20000, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}