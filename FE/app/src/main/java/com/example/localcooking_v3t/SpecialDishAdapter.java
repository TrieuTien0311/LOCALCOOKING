package com.example.localcooking_v3t;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.MonAn;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter cho món ăn đặc sắc với infinite scroll
 * Hỗ trợ cả ảnh từ URL (server) và drawable (local)
 */
public class SpecialDishAdapter extends RecyclerView.Adapter<SpecialDishAdapter.DishViewHolder> {

    private List<DishItem> dishItems;
    private Context context;

    // Class để lưu thông tin món ăn (có thể là URL hoặc drawable)
    public static class DishItem {
        private String imageUrl;      // URL ảnh từ server
        private int drawableResId;    // Resource ID từ drawable
        private boolean isFromServer; // true = từ server, false = từ drawable

        // Constructor cho ảnh từ server
        public DishItem(String imageUrl) {
            this.imageUrl = imageUrl;
            this.isFromServer = true;
        }

        // Constructor cho ảnh từ drawable
        public DishItem(int drawableResId) {
            this.drawableResId = drawableResId;
            this.isFromServer = false;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getDrawableResId() {
            return drawableResId;
        }

        public boolean isFromServer() {
            return isFromServer;
        }
    }

    // Constructor mới nhận List<DishItem>
    public SpecialDishAdapter(Context context, List<DishItem> dishItems) {
        this.context = context;
        this.dishItems = dishItems;
    }

    // Constructor cũ để tương thích ngược (nhận List<Integer> drawable IDs)
    public SpecialDishAdapter(List<Integer> dishImages) {
        this.dishItems = new ArrayList<>();
        for (Integer resId : dishImages) {
            this.dishItems.add(new DishItem(resId));
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // Cập nhật dữ liệu
    public void updateData(List<DishItem> newItems) {
        this.dishItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_special_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        // Lấy vị trí thực trong danh sách (để tạo infinite scroll)
        int realPosition = position % dishItems.size();
        DishItem item = dishItems.get(realPosition);

        if (item.isFromServer() && item.getImageUrl() != null) {
            // Load ảnh từ URL server
            Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .apply(new RequestOptions()
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.hue)
                    .error(R.drawable.hue))
                .into(holder.imgDish);
        } else {
            // Load ảnh từ drawable
            holder.imgDish.setImageResource(item.getDrawableResId());
        }
    }

    @Override
    public int getItemCount() {
        if (dishItems == null || dishItems.isEmpty()) {
            return 0;
        }
        // Trả về số lượng rất lớn để tạo hiệu ứng infinite scroll
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return dishItems != null ? dishItems.size() : 0;
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.imgDish);
        }
    }
}
