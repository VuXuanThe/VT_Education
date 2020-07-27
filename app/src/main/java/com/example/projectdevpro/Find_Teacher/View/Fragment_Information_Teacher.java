package com.example.projectdevpro.Find_Teacher.View;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.Find_Teacher.Interface.IGetTeacher;
import com.example.projectdevpro.Find_Teacher.Model.Information_Teacher_Presenter;
import com.example.projectdevpro.Fragment_Map_Direction;
import com.example.projectdevpro.R;
import com.example.projectdevpro.databinding.FragmentInformationTeacherBinding;

public class Fragment_Information_Teacher extends Fragment implements IGetTeacher {
    private static final String TAG = "FragmentInformationTeacher";
    FragmentInformationTeacherBinding binding;
    Information_Teacher_Presenter presenter;

    public static Fragment_Information_Teacher newInstance(Bundle bundle) {
        Fragment_Information_Teacher fragment = new Fragment_Information_Teacher();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information_teacher, container, false);
        Bundle bundle = getArguments();
        presenter = new Information_Teacher_Presenter(this, getContext());
        presenter.onGetTeacher(bundle);
        Direction(bundle);
        return binding.getRoot();
    }

    private void Direction(final Bundle data){
        binding.btnLocationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(Fragment_Map_Direction.newInstance(data), "FragmentMapDirection");
            }
        });
    }

    @Override
    public void getDataOfTeacher(String linkAvatarTeacher, Bitmap avatar, String teacherName, String experient, String job,
                                 String subject, String gender, int age,
                                 String vehicle, String freeTime, int tuition,
                                 String processWork, String adress) {
        binding.imgAvatarTeacher.setImageBitmap(avatar);
        binding.tvTeacherName.setText(teacherName);
        binding.tvExperient.setText(experient);
        binding.tvJob.setText(job);
        binding.tvSubject.setText(subject);
        binding.tvGender.setText(gender);
        binding.tvAge.setText(String.valueOf(age));
        binding.tvVehicle.setText(vehicle);
        binding.tvFreetime.setText(freeTime);
        binding.tvTuition.setText(String.valueOf(tuition));
        binding.tvProcessWork.setText(processWork);
        binding.tvAdress.setText(adress);
    }

    public void getFragment(Fragment fragment, String nameFragment) {
        try {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(nameFragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTeacherFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}

