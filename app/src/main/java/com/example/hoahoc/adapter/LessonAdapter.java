package com.example.hoahoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.R;
import com.example.hoahoc.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private List<Lesson> Lessons;

    private OnItemClickListener listener;

    public LessonAdapter(List<Lesson> Lessons) {
        this.Lessons = Lessons;
    }

    public interface OnItemClickListener {
        void onItemClick(Lesson Lesson);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Lesson> newData) {
        Lessons.clear();
        Lessons.addAll(newData);
        notifyDataSetChanged();
    }



    // but su kien item


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_chuong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson Lesson = Lessons.get(position);
        holder.bind(Lesson);

        // su kien click item
        holder.itemView.setOnClickListener(e->{
            if (listener != null) {
                listener.onItemClick(Lesson);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_chuong;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            text_chuong = itemView.findViewById(R.id.text_chuong);
        }
        public void bind(Lesson Lesson) {
            text_chuong.setText(Lesson.getTenchuong());
        }
    }

}
