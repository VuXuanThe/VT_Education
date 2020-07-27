package com.example.projectdevpro.Retrofit.Interface;

import com.example.projectdevpro.Object.Teacher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderAPITeacher {
    @GET("teacher")
    Call<List<Teacher>> getTeachers();
}
