package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter cho món ăn đặc sắc với infinite scroll
 */
public class SpecialDishAdapter extends RecyclerView.Adapter<SpecialDishAdapter.DishViewHolder> {

    private List<Integer> dishImages;

    public SpecialDishAdapter(List<Integer> dishImages) {
        this.dishImages = dishImages;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_special_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        // Lấy vị trí thực trong danh sách (để tạo infinite scroll)
        int realPosition = position % dishImages.size();
        holder.imgDish.setImageResource(dishImages.get(realPosition));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng rất lớn để tạo hiệu ứng infinite scroll
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return dishImages.size();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.imgDish);
        }
    }
}
