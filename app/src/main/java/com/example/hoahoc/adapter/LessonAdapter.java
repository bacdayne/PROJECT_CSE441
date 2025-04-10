package com.example.hoahoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.R;
import com.example.hoahoc.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private List<Lesson> lessons;
    private boolean isAdmin = false;

    private OnItemClickListener clickListener;
    private OnItemActionListener actionListener;

    public interface OnItemClickListener {
        void onItemClick(Lesson lesson);
    }

    public interface OnItemActionListener {
        void onEdit(Lesson lesson);
        void onDelete(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.actionListener = listener;
    }

    public void setAdminMode(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void updateData(List<Lesson> newData) {
        lessons.clear();
        lessons.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isAdmin) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chi_tiet_admin, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chi_tiet_chuong, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.bind(lesson);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(lesson);
            }
        });

        if (isAdmin) {
            holder.btnEdit.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onEdit(lesson);
                }
            });
            holder.btnDelete.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onDelete(lesson);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_chuong;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_chuong = itemView.findViewById(R.id.text_chuong);

            if (isAdmin) {
                btnEdit = itemView.findViewById(R.id.btn_edit);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }
        }

        public void bind(Lesson lesson) {
            text_chuong.setText(lesson.getTenchuong());
        }
    }
}
