package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.model.MonAn;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<MonAn> danhSachMon;
    private int currentPosition = 0;

    public FoodAdapter(List<MonAn> danhSachMon) {
        this.danhSachMon = danhSachMon;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        MonAn monAn = danhSachMon.get(position);

        holder.txtFood.setText(monAn.getTenMon());
        holder.txtGioiThieu.setText(monAn.getGioiThieu() != null ? monAn.getGioiThieu() : "");
        holder.txtNguyenLieu.setText(monAn.getNguyenLieu() != null ? monAn.getNguyenLieu() : "");
        
        // Sử dụng placeholder image vì chưa có hình ảnh từ API
        holder.imgMonAn.setImageResource(R.drawable.ic_main_dish_tt);

        // Xử lý nút Previous
        holder.btnPre.setOnClickListener(v -> {
            if (currentPosition > 0) {
                currentPosition--;
                notifyDataSetChanged();
            }
        });

        // Xử lý nút Next
        holder.btnNext.setOnClickListener(v -> {
            if (currentPosition < danhSachMon.size() - 1) {
                currentPosition++;
                notifyDataSetChanged();
            }
        });

        // Cập nhật các circle indicator
        updateCircleIndicators(holder, position);
    }

    private void updateCircleIndicators(FoodViewHolder holder, int position) {
        ImageView[] circles = {holder.circle1, holder.circle2, holder.circle3,
                holder.circle4, holder.circle5};

        for (int i = 0; i < circles.length; i++) {
            if (i < danhSachMon.size()) {
                circles[i].setVisibility(View.VISIBLE);
                if (i == position) {
                    circles[i].setColorFilter(0xFFBA5632); // Màu active
                } else {
                    circles[i].setColorFilter(0xFFDCA790); // Màu inactive
                }
            } else {
                circles[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return danhSachMon.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView txtFood, txtGioiThieu, txtNguyenLieu;
        ImageView imgMonAn, btnPre, btnNext;
        ImageView circle1, circle2, circle3, circle4, circle5;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtFood);
            txtGioiThieu = itemView.findViewById(R.id.txtGioiThieu);
            txtNguyenLieu = itemView.findViewById(R.id.txtNguyenLieu);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            btnPre = itemView.findViewById(R.id.btnPre);
            btnNext = itemView.findViewById(R.id.btnNext);
            circle1 = itemView.findViewById(R.id.circle1);
            circle2 = itemView.findViewById(R.id.circle2);
            circle3 = itemView.findViewById(R.id.circle3);
            circle4 = itemView.findViewById(R.id.circle4);
            circle5 = itemView.findViewById(R.id.circle5);
        }
    }
}