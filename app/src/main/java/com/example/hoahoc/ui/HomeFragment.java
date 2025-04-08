package com.example.hoahoc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hoahoc.BaiGiangActivity;
import com.example.hoahoc.TracNghiemActivity;
import com.example.hoahoc.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imgbaigiang, imgtracnghiem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imgbaigiang = binding.imgBaigiang;
        imgtracnghiem = binding.imgTracnghiem;

        imgbaigiang.setOnClickListener(e->{
            Intent intent = new Intent(getActivity(), BaiGiangActivity.class);
            startActivity(intent);
            });

        imgtracnghiem.setOnClickListener(e->{
            Intent intent = new Intent(getActivity(), TracNghiemActivity.class);
            startActivity(intent);
        });

//        imgtracnghiem.setOnClickListener(e->{
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}