package com.example.projectdevpro.Find_Teacher.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.projectdevpro.Find_Teacher.Interface.IGetTeacher;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Information_Teacher_Presenter{
    IGetTeacher iGetTeacher;
    Context context;

    public Information_Teacher_Presenter(IGetTeacher iGetTeacher, Context context) {
        this.iGetTeacher = iGetTeacher;
        this.context = context;
    }

    public void onGetTeacher(Bundle bundle){
        if(bundle != null){
            Teacher teacher = (Teacher) bundle.getSerializable("Teacher");
            iGetTeacher.getDataOfTeacher(teacher.getLinkAvatarTeacher(), teacher.getAvatar(), teacher.getTeacherName(), teacher.getExperient(),
                    teacher.getJob(), teacher.getSubject(), teacher.getGender(), teacher.getAge(),
                    teacher.getVehicle(), teacher.getFreeTime(), teacher.getTuition(),
                    teacher.getProcessWork(), Adress(teacher.getLongitude(), teacher.getLatitude()));
        }
        else
            iGetTeacher.getTeacherFailure(context.getResources().getString(R.string.error));
    }

    private String Adress(double longitude, double latitude){
        Geocoder geocoder;
        String address = null;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}
