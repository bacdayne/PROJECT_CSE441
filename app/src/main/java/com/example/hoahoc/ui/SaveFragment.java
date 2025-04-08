package com.example.hoahoc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.LectureDetailsActivity;
import com.example.hoahoc.adapter.LessonAdapter;
import com.example.hoahoc.databinding.FragmentSaveBinding;
import com.example.hoahoc.model.Lesson;
import com.example.hoahoc.DatabaseHelper;

import java.util.List;

public class SaveFragment extends Fragment {

    private FragmentSaveBinding binding;

    private RecyclerView recyclerSaved;
    private LessonAdapter adapter;
    private DatabaseHelper help;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo DatabaseHelper
        help = new DatabaseHelper(requireContext());

        // Lấy danh sách đã lưu
        List<Lesson> danhSachDaLuu = help.getSavedLessons();

        recyclerSaved = binding.recyclerSaved;
        adapter = new LessonAdapter(danhSachDaLuu);
        binding.recyclerSaved.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSaved.setAdapter(adapter);

        // Xử lý khi click vào bài giảng
        adapter.setOnItemClickListener(lesson -> {
            Intent intent = new Intent(getActivity(), LectureDetailsActivity.class);
            intent.putExtra("baigiang", lesson);
            startActivity(intent);
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
