package com.example.projectdevpro.Find_Teacher.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdevpro.Find_Teacher.Adapter.AdapterShowTeacher;
import com.example.projectdevpro.Find_Teacher.Interface.ClickItemTeacher;
import com.example.projectdevpro.Find_Teacher.Interface.ILoadTeacher;
import com.example.projectdevpro.Find_Teacher.Model.List_Teacher_Presenter;
import com.example.projectdevpro.Fragment_Map_Direction;
import com.example.projectdevpro.GPSLocation;
import com.example.projectdevpro.Helper;
import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.FragmentFindTeacherBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;

import java.io.File;
import java.util.List;
import static androidx.core.app.ActivityCompat.finishAffinity;

public class Fragment_Find_Teacher extends Fragment implements ILoadTeacher{
    private static final String TAG = "FragmentFindTeacher";
    FragmentFindTeacherBinding binding;
    List_Teacher_Presenter presenter;
    private Helper helper;

    private AndroidXMapFragment mapFragment;
    private Map map;

    public static Fragment_Find_Teacher newInstance() {
        Bundle args = new Bundle();
        Fragment_Find_Teacher fragment = new Fragment_Find_Teacher();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_teacher, container, false);
        helper = new Helper(getContext());
        presenter = new List_Teacher_Presenter(this, getContext());
        return binding.getRoot();
    }

    private void initView(List <Teacher> teachers){
        ApplicationContext context = new ApplicationContext(getContext());
        initMapFragment(context, teachers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.rvListTeacher.setLayoutManager(layoutManager);
        AdapterShowTeacher adapterShowTeacher = new AdapterShowTeacher(teachers, getContext());
        binding.rvListTeacher.setAdapter(adapterShowTeacher);
        binding.progressLoad.setVisibility(View.GONE);
        binding.btnCurrentPosition.setVisibility(View.VISIBLE);
        binding.controlButton.setVisibility(View.VISIBLE);
        clickItemTeacher(adapterShowTeacher);
        clickControlButton();
    }

    private void clickControlButton(){
        binding.btnNearTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerMap.setVisibility(View.VISIBLE);
                binding.rvListTeacher.setVisibility(View.INVISIBLE);
            }
        });
        binding.btnListTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerMap.setVisibility(View.INVISIBLE);
                binding.rvListTeacher.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clickItemTeacher(final AdapterShowTeacher adapterShowTeacher){
        adapterShowTeacher.setClickItemTeacher(new ClickItemTeacher() {
            @Override
            public void onClick(Teacher teacher) {
                Bundle args = new Bundle();
                args.putSerializable("Teacher", teacher);
                getFragment(Fragment_Information_Teacher.newInstance(args), "Fragment_Information_Teacher");
            }
        });
    }

    public void getFragment(Fragment fragment, String nameFragment) {
        try {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(nameFragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    private void initMapFragment(ApplicationContext context, final List<Teacher> teachers) {
        String diskCacheRoot = getActivity().getFilesDir().getPath()
                + File.separator + ".isolated-here-maps";

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot);
        if(!success)
            Toast.makeText(getActivity().getApplicationContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_SHORT).show();
        else{
            mapFragment = new AndroidXMapFragment();
            getFragmentManager().beginTransaction().add(R.id.mapfragment, mapFragment, "MAP_TAG").commit();
            mapFragment.init(context, new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(
                        final OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE){
                        map = presenter.setMap(mapFragment);
                        GPSLocation location = new GPSLocation(getContext());
                        GeoCoordinate currentPosition =  new GeoCoordinate(location.getLatitude(), location.getLongitude());
                        map.setCenter(currentPosition, Map.Animation.BOW);
                        clickMarkerTeacher(teachers);
                        clickButtonCurrentPosition();
                        createMarker(teachers);
                    }
                    else{
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                        new AlertDialog.Builder(getActivity()).setMessage(
                                "Error : " + error.name() + "\n\n" + error.getDetails())
                                .setTitle(R.string.engine_init_error)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                finishAffinity(getActivity());
                                            }
                                        }).create().show();
                    }
                }
            });
        }
    }

    @Override
    public void Completeload(List<Teacher> teachers) {
        initView(teachers);
    }

    @Override
    public void LoadFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        binding.progressLoad.setVisibility(View.GONE);
    }

    private void clickMarkerTeacher(final List < Teacher > teachers){
        mapFragment.getMapGesture().addOnGestureListener(new MapGesture.OnGestureListener() {
            @Override
            public void onPanStart() {}
            @Override
            public void onPanEnd() {}
            @Override
            public void onMultiFingerManipulationStart() {}
            @Override
            public void onMultiFingerManipulationEnd() {}
            @Override
            public boolean onMapObjectsSelected(@NonNull List<ViewObject> list) {
                for (ViewObject viewObject : list) {
                    if (viewObject.getBaseType() == ViewObject.Type.USER_OBJECT) {
                        MapObject mapObject = (MapObject) viewObject;
                        if (mapObject.getType() == MapObject.Type.MARKER) {
                            MapMarker marker = (MapMarker) mapObject;
                            if(marker != null){
                                setBottomSheetTeacher(teachers.get(Integer.valueOf(marker.getTitle()) - 1));
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
            @Override
            public boolean onTapEvent(@NonNull PointF pointF) {return false; }
            @Override
            public boolean onDoubleTapEvent(@NonNull PointF pointF) {
                return false;
            }
            @Override
            public void onPinchLocked() {}

            @Override
            public boolean onPinchZoomEvent(float v, @NonNull PointF pointF) {
                return false;
            }

            @Override
            public void onRotateLocked() {}

            @Override
            public boolean onRotateEvent(float v) {
                return false;
            }

            @Override
            public boolean onTiltEvent(float v) {
                return false;
            }

            @Override
            public boolean onLongPressEvent(@NonNull PointF pointF) {
                return false;
            }

            @Override
            public void onLongPressRelease() { }

            @Override
            public boolean onTwoFingerTapEvent(@NonNull PointF pointF) {
                return false;
            }
        }, 0, false);
    }

    private void clickButtonCurrentPosition(){
        binding.btnCurrentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSLocation location = new GPSLocation(getContext());
                map.setCenter(new GeoCoordinate(location.getLatitude(), location.getLongitude()), Map.Animation.BOW);
            }
        });
    }

    private void createMarker(List<Teacher> teachers){
        for (final Teacher teacher: teachers) {
            Image marker_img = new Image();
            marker_img.setBitmap(helper.createAvatarUser(teacher.getAvatar()));
            MapMarker marker = new MapMarker(new GeoCoordinate(teacher.getLatitude(), teacher.getLongitude()), marker_img);
            marker.setDraggable(true);
            marker.setTitle(String.valueOf(teacher.getIdTeacher()));
            map.addMapObject(marker);
        }

    }

    public void setBottomSheetTeacher(final Teacher teacher){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_bottom_sheet_teacher,
                        (LinearLayout)getActivity().findViewById(R.id.bottomSheetContainerTeacher));
        ImageView avatar = bottomSheetView.findViewById(R.id.bsimgAcountTeacher);
        avatar.setImageBitmap(teacher.getAvatar());
        TextView teacherName = bottomSheetView.findViewById(R.id.bstvAcount_name);
        teacherName.setText(teacher.getTeacherName());
        TextView experient = bottomSheetView.findViewById(R.id.bstvExperient);
        experient.setText(teacher.getExperient());
        TextView subject = bottomSheetView.findViewById(R.id.bstvSubject);
        subject.setText(teacher.getSubject());
        TextView gender = bottomSheetView.findViewById(R.id.bstvGender);
        gender.setText(teacher.getGender());
        TextView age = bottomSheetView.findViewById(R.id.bstvAge);
        age.setText(String.valueOf(teacher.getAge()));

        TextView detail = bottomSheetView.findViewById(R.id.tvDetail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Teacher", teacher);
                getFragment(Fragment_Information_Teacher.newInstance(bundle), "Fragment_Information_Teacher");
                bottomSheetDialog.dismiss();
            }
        });

        TextView direction = bottomSheetView.findViewById(R.id.tvDirection);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Teacher", teacher);
                getFragment(Fragment_Map_Direction.newInstance(bundle), "Fragment_Map_Direction");
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}

