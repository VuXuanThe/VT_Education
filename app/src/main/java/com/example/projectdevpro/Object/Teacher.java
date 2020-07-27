package com.example.projectdevpro.Object;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Teacher implements Serializable {
    private int     idTeacher;
    private String  linkAvatarTeacher;
    private Bitmap  avatar;
    private String  teacherName;
    private String  password;
    private String  experient;
    private String  job;
    private String  subject;
    private String  gender;
    private int     age;
    private String  vehicle;
    private String  freeTime;
    private int     tuition;
    private String  email;
    private String  numberPhone;
    private String  processWork;
    private double  longitude;
    private double  latitude;

    public Teacher(int idTeacher, String linkAvatarTeacher, Bitmap avatar, String teacherName, String password,
                   String experient, String job, String subject, String gender,
                   int age, String vehicle, String freeTime, int tuition,
                   String email, String numberPhone, String processWork, double longitude,
                   double latitude) {
        this.idTeacher = idTeacher;
        this.linkAvatarTeacher = linkAvatarTeacher;
        this.avatar = avatar;
        this.teacherName = teacherName;
        this.password = password;
        this.experient = experient;
        this.job = job;
        this.subject = subject;
        this.gender = gender;
        this.age = age;
        this.vehicle = vehicle;
        this.freeTime = freeTime;
        this.tuition = tuition;
        this.email = email;
        this.numberPhone = numberPhone;
        this.processWork = processWork;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getLinkAvatarTeacher() {
        return linkAvatarTeacher;
    }

    public void setLinkAvatarTeacher(String linkAvatarTeacher) {
        this.linkAvatarTeacher = linkAvatarTeacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExperient() {
        return experient;
    }

    public void setExperient(String experient) {
        this.experient = experient;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public int getTuition() {
        return tuition;
    }

    public void setTuition(int tuition) {
        this.tuition = tuition;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getProcessWork() {
        return processWork;
    }

    public void setProcessWork(String processWork) {
        this.processWork = processWork;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

