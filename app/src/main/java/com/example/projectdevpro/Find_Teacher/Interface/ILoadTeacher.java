package com.example.projectdevpro.Find_Teacher.Interface;

import com.example.projectdevpro.Object.Teacher;

import java.util.List;

public interface ILoadTeacher {
    void Completeload(List<Teacher> teachers);
    void LoadFailure(String error);
}
