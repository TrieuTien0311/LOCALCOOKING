package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.model.DanhMucMonAn;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<DanhMucMonAn> danhSachDanhMuc;

    public CategoryAdapter(List<DanhMucMonAn> danhSachDanhMuc) {
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
        DanhMucMonAn danhMuc = danhSachDanhMuc.get(position);

        holder.txtDish.setText(danhMuc.getTenDanhMuc());
        
        // Xử lý icon danh mục - lấy từ iconDanhMuc field
        int iconResource = getIconResource(danhMuc.getIconDanhMuc());
        holder.imgDish.setImageResource(iconResource);

        // Setup RecyclerView con cho món ăn
        FoodAdapter foodAdapter = new FoodAdapter(danhMuc.getDanhSachMon());
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
    
    private int getIconResource(String iconDanhMuc) {
        // Map icon từ database với drawable resource
        if (iconDanhMuc == null || iconDanhMuc.isEmpty()) {
            return R.drawable.ic_main_dish_tt; // Icon mặc định
        }
        
        // Loại bỏ extension (.png, .jpg)
        String iconName = iconDanhMuc.replace(".png", "").replace(".jpg", "");
        
        // Map các icon từ database
        switch (iconName) {
            case "ic_appetizer":
                return R.drawable.ic_appetizer_tt; // Icon món khai vị
            case "ic_main_dish":
                return R.drawable.ic_main_dish_tt; // Icon món chính
            case "ic_dessert":
                return R.drawable.ic_dessert_tt; // Icon món tráng miệng
            default:
                return R.drawable.ic_main_dish_tt; // Icon mặc định
        }
    }

    @Override
    public int getItemCount() {
        return danhSachDanhMuc.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtDish;
        ImageView imgDish, btnDown;
        RecyclerView rcvFoods;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDish = itemView.findViewById(R.id.txtDish);
            imgDish = itemView.findViewById(R.id.imgDish);
            btnDown = itemView.findViewById(R.id.btnDown);
            rcvFoods = itemView.findViewById(R.id.rcvFoods);
        }
    }
}