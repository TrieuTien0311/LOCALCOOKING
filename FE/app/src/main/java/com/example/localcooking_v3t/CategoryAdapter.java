package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> danhSachDanhMuc;

    public CategoryAdapter(List<Category> danhSachDanhMuc) {
        this.danhSachDanhMuc = danhSachDanhMuc;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = danhSachDanhMuc.get(position);

        holder.txtDish.setText(category.getTenDanhMuc());
        holder.txtTime.setText(category.getThoiGian());
        holder.imgDish.setImageResource(category.getIconDanhMuc());

        // Setup RecyclerView con cho món ăn
        FoodAdapter foodAdapter = new FoodAdapter(category.getDanhSachMon());
        holder.rcvFoods.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rcvFoods.setAdapter(foodAdapter);

        // Xử lý toggle expand/collapse
        holder.btnDown.setOnClickListener(v -> {
            if (holder.rcvFoods.getVisibility() == View.GONE) {
                holder.rcvFoods.setVisibility(View.VISIBLE);
                holder.btnDown.animate().rotation(180).setDuration(300).start();
            } else {
                holder.rcvFoods.setVisibility(View.GONE);
                holder.btnDown.animate().rotation(0).setDuration(300).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachDanhMuc.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtDish, txtTime;
        ImageView imgDish, btnDown;
        RecyclerView rcvFoods;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDish = itemView.findViewById(R.id.txtDish);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgDish = itemView.findViewById(R.id.imgDish);
            btnDown = itemView.findViewById(R.id.btnDown);
            rcvFoods = itemView.findViewById(R.id.rcvFoods);
        }
    }
}