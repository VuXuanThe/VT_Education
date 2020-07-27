package com.example.projectdevpro.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.LoadImageUser.IloadImage;
import com.example.projectdevpro.LoadImageUser.LoadImage;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;
import com.example.projectdevpro.Retrofit.Interface.JsonPlaceHolderAPITeacher;
import com.example.projectdevpro.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Presenter extends Fragment implements IloadImage {
    private Context context;
    private ILogin iLogin;
    public static final String Base_URL = "https://demo8558222.mockable.io/";
    private String mess = null;
    List<Teacher> teachers = null;
    LoadImage loadImage = new LoadImage(getContext(), this);
    SharedPreferences sharedPreferences;

    public Login_Presenter(Context context, ILogin iLogin) {
        this.context = context;
        this.iLogin = iLogin;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public void rememberLogin(){
        iLogin.rememberLogin(sharedPreferences.getString("userName", ""),
                sharedPreferences.getString("password", ""),
                sharedPreferences.getBoolean("remember", false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onLogin(final String numberPhone, final String password, final boolean rememberPassword){
        if (numberPhone.isEmpty()){
            mess = context.getResources().getString(R.string.Notice_EnterUsername);
            iLogin.loginFail(mess);
        }
        else if (password.isEmpty()){
            mess = context.getResources().getString(R.string.Notice_EnterPassword);
            iLogin.loginFail(mess);
        }
        else{
            JsonPlaceHolderAPITeacher jsonPlaceHolderAPITeacher =
                    RetrofitClient.getClient(Base_URL).create(JsonPlaceHolderAPITeacher.class);
            Call<List<Teacher>> call = jsonPlaceHolderAPITeacher.getTeachers();
            call.enqueue(new Callback<List<Teacher>>() {
                @Override
                public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                    if(!response.isSuccessful()){
                        mess = context.getResources().getString(R.string.error);
                        iLogin.loginFail(mess);
                        return;
                    }
                    boolean check = false;
                    for (Teacher teacher: response.body())
                        if(teacher.getNumberPhone().equals(numberPhone) && teacher.getPassword().equals(password)){
                            iLogin.loginSuccess(teacher);
                            if(rememberPassword == true){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userName", teacher.getNumberPhone());
                                editor.putString("password", teacher.getPassword());
                                editor.putBoolean("remember", rememberPassword);
                                editor.commit();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("userName");
                                editor.remove("password");
                                editor.remove("remember");
                                editor.commit();
                            }
                            check = true;
                            break;
                        }
                    if(check == false){
                        mess = context.getResources().getString(R.string.Error_Acount);
                        iLogin.loginFail(mess);
                    }
                }
                @Override
                public void onFailure(Call<List<Teacher>> call, Throwable t) {
                    mess = context.getResources().getString(R.string.error);
                    iLogin.loginFail(mess);
                }
            });
        }
    }

    @Override
    public void getBitmap(Bitmap bitmap) {

    }
}
