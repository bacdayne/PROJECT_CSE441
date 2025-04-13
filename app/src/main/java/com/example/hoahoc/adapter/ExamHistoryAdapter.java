package com.example.hoahoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.R;
import com.example.hoahoc.model.ExamHistory;

import java.util.List;

public class ExamHistoryAdapter extends RecyclerView.Adapter<ExamHistoryAdapter.HistoryViewHolder> {

    private List<ExamHistory> historyList;
    private OnItemActionListener actionListener;

    public interface OnItemActionListener {
        void onItemClick(ExamHistory history);
        void onDelete(ExamHistory history);
    }

    public ExamHistoryAdapter(List<ExamHistory> historyList, OnItemActionListener listener) {
        this.historyList = historyList;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ExamHistory history = historyList.get(position);
        holder.tvExamNumber.setText("Đề số " + history.getExamId());
        holder.tvCompletionTime.setText("Thời gian: " + history.getCompletionTime());
        holder.tvScore.setText("Điểm: " + history.getScore());
        holder.tvTotalCorrect.setText("Số câu đúng: " + history.getTotalCorrect());

        holder.itemView.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onItemClick(history);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamNumber, tvCompletionTime, tvScore, tvTotalCorrect;
        ImageButton btnDelete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamNumber = itemView.findViewById(R.id.tvExamNumber);
            tvCompletionTime = itemView.findViewById(R.id.tvCompletionTime);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvTotalCorrect = itemView.findViewById(R.id.tvTotalCorrect);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}