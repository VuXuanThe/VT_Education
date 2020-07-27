package com.example.projectdevpro.Find_Teacher.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.Find_Teacher.Interface.ILoadTeacher;
import com.example.projectdevpro.GPSLocation;
import com.example.projectdevpro.LoadImageUser.IloadImage;
import com.example.projectdevpro.LoadImageUser.LoadImage;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;
import com.example.projectdevpro.Retrofit.Interface.JsonPlaceHolderAPITeacher;
import com.example.projectdevpro.Retrofit.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_Teacher_Presenter extends Fragment implements IloadImage {
    ILoadTeacher iLoadTeacher;
    Context context;
    //public static final String Base_URL = "https://demo3535021.mockable.io/";
    public static final String Base_URL = "https://demo8558222.mockable.io/";
    String error = null;
    List<Bitmap> avatars = new ArrayList<>();
    List<Teacher> teachers = null;
    LoadImage loadImage = new LoadImage(getContext(), this);
    private Map map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List_Teacher_Presenter(ILoadTeacher iLoadTeacher, Context context) {
        this.iLoadTeacher = iLoadTeacher;
        this.context = context;
        LoadListTeacher();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void LoadListTeacher(){
        JsonPlaceHolderAPITeacher jsonPlaceHolderAPITeacher =
                RetrofitClient.getClient(Base_URL).create(JsonPlaceHolderAPITeacher.class);
        Call<List<Teacher>> call = jsonPlaceHolderAPITeacher.getTeachers();
        call.enqueue(new Callback<List<Teacher>>() {
            @Override
            public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                if(!response.isSuccessful()){
                    error = context.getResources().getString(R.string.error);
                    iLoadTeacher.LoadFailure(error);
                    return;
                }
                teachers = response.body();
                for (Teacher teacher: teachers) loadImage.new loadImageFromInternet()
                        .execute(teacher.getLinkAvatarTeacher());
            }
            @Override
            public void onFailure(Call<List<Teacher>> call, Throwable t) {
                error = context.getResources().getString(R.string.error);
                iLoadTeacher.LoadFailure(error);
            }
        });
    }

    @Override
    public void getBitmap(Bitmap bitmap) {
        Bitmap avatar = getResizedBitmap(bitmap, 500);
        avatars.add(avatar);
        if (avatars.size() == teachers.size()){
            for(int i = 0; i < teachers.size(); i++)
                teachers.get(i).setAvatar(avatars.get(i));
            iLoadTeacher.Completeload(teachers);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Map setMap(final AndroidXMapFragment mapFragment){
                map = mapFragment.getMap();
                map.setZoomLevel(15);
                map.setTilt((map.getMinTilt() + map.getMaxTilt())/2);
                map.setProjectionMode(Map.Projection.MERCATOR);
                Image marker_img_current_position = new Image();
                try {
                    marker_img_current_position.setImageResource(R.drawable.ic_current_position2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                map.getPositionIndicator().setMarker(marker_img_current_position);
                map.getPositionIndicator().setVisible(true);
                return map;
    }
}
