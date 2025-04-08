package com.example.hoahoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.ExamDetailActivity;
import com.example.hoahoc.R;
import com.example.hoahoc.model.Exam;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private Context context;
    private List<Exam> examList;

    public ExamAdapter(Context context, List<Exam> examList) {
        this.context = context;
        this.examList = examList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        holder.tvExamNumber.setText("Đề số " + exam.getExamNumber());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExamDetailActivity.class);
            intent.putExtra("EXAM_ID", exam.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamNumber;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamNumber = itemView.findViewById(R.id.tvExamNumber);
        }
    }
}
