package com.example.projectdevpro.Login;

import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.Main.Fragment_Home_Acount;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.FragmentLoginBinding;

public class Fragment_Login extends Fragment implements ILogin{
    private static final String TAG = "FragmentLogin";
    FragmentLoginBinding binding;
    private Login_Presenter presenter;

    public static Fragment_Login newInstance(){
        Bundle args = new Bundle();
        Fragment_Login fragment = new Fragment_Login();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        presenter = new Login_Presenter(getContext(), this);
        clickLogin();
        showPassword();
        return binding.getRoot();
    }

    private void clickLogin(){
        presenter.rememberLogin();
        binding.btnLoginOK.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                binding.Load.setVisibility(View.VISIBLE);
                binding.edtPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                binding.edtUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
                login();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login(){
        String numberPhone = binding.edtUsername.getText().toString();
        String passWord = binding.edtPassword.getText().toString();
        boolean rememberPassword = binding.cbRememberPassword.isChecked();
        presenter.onLogin(numberPhone, passWord, rememberPassword);
    }

    private void showPassword(){
        binding.imgShowPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    binding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.edtPassword.setSelection(binding.edtPassword.getText().length());
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    binding.edtPassword.setTransformationMethod(null);
                }
                return false;
            }
        });
    }

    @Override
    public void loginSuccess(Teacher teacher) {
        binding.Load.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Teacher", teacher);
        getFragment(Fragment_Home_Acount.newInstance(bundle), "FragmentHomeAcount");
    }

    @Override
    public void loginFail(String mess) {
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rememberLogin(String userName, String passWord, boolean checkRemember) {
        binding.edtUsername.setText(userName);
        binding.edtPassword.setText(passWord);
        binding.cbRememberPassword.setChecked(checkRemember);
    }

    public void getFragment(Fragment fragment, String nameFragment) {
        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(nameFragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }
}