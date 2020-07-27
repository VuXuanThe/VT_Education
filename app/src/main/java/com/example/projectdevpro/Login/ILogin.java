package com.example.projectdevpro.Login;

import com.example.projectdevpro.Object.Teacher;

public interface ILogin {
    void loginSuccess(Teacher teacher);
    void loginFail(String mess);
    void rememberLogin(String userName, String passWord, boolean checkRemember);
}
