package com.example.hoahoc.view.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hoahoc.view.view.activity.LessonActivity;
import com.example.hoahoc.view.view.activity.QuestionsActivity;
import com.example.hoahoc.R;
import com.example.hoahoc.view.view.adapter.Photo;
import com.example.hoahoc.view.view.adapter.PhotoViewPagerAdapter;
import com.example.hoahoc.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imgbaigiang, imgtracnghiem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imgbaigiang = binding.imgBaigiang;
        imgtracnghiem = binding.imgTracnghiem;
        // Khởi tạo danh sách ảnh
        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(R.drawable.hoa_1));
        photoList.add(new Photo(R.drawable.hoa_2));
        photoList.add(new Photo(R.drawable.hoa_3));
        photoList.add(new Photo(R.drawable.hoa_4));

        // Gán adapter cho ViewPager
        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(photoList);
        binding.viewPager.setAdapter(adapter);


        imgbaigiang.setOnClickListener(e->{
            Intent intent = new Intent(getActivity(), LessonActivity.class);
            startActivity(intent);
            });

        imgtracnghiem.setOnClickListener(e->{
            Intent intent = new Intent(getActivity(), QuestionsActivity.class);
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