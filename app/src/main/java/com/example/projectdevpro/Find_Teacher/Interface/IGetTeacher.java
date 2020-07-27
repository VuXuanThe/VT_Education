package com.example.projectdevpro.Find_Teacher.Interface;

import android.graphics.Bitmap;

public interface IGetTeacher {
    void getDataOfTeacher(String linkAvatarTeacher, Bitmap avatar, String teacherName, String experient, String job,
                          String subject, String gender, int age, String vehicle,
                          String freeTime, int tuition, String processWork, String adress);
    void getTeacherFailure(String error);

}
