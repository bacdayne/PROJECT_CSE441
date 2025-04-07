package com.example.hoahoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoahoc.R;
import com.example.hoahoc.model.baigiang;

import java.util.List;

public class BaigiangAdapter extends RecyclerView.Adapter<BaigiangAdapter.ViewHolder> {
    private List<baigiang> baigiangs;

    private OnItemClickListener listener;

    public BaigiangAdapter(List<baigiang> baigiangs) {
        this.baigiangs = baigiangs;
    }

    public interface OnItemClickListener {
        void onItemClick(baigiang baigiang);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        baigiang baigiang = baigiangs.get(position);
        holder.bind(baigiang);

        // su kien click item
        holder.itemView.setOnClickListener(e->{
            if (listener != null) {
                listener.onItemClick(baigiang);
            }
        });


    }

    @Override
    public int getItemCount() {
        return baigiangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_chuong;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            text_chuong = itemView.findViewById(R.id.text_chuong);
        }
        public void bind(baigiang baigiang) {
            text_chuong.setText(baigiang.getTenchuong());
        }
    }

}
