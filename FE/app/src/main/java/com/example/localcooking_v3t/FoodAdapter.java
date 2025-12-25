package com.example.localcooking_v3t;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.HinhAnhMonAn;
import com.example.localcooking_v3t.model.MonAn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private static final String TAG = "FoodAdapter";
    private List<MonAn> danhSachMon;
    private Map<Integer, List<HinhAnhMonAn>> hinhAnhMap = new HashMap<>();
    private Map<Integer, Integer> currentImageIndexMap = new HashMap<>();
    private ApiService apiService;

    public FoodAdapter(List<MonAn> danhSachMon) {
        this.danhSachMon = danhSachMon;
        this.apiService = RetrofitClient.getApiService();
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
        Integer maMonAn = monAn.getMaMonAn();

        // DEBUG: Log thông tin món ăn
        Log.d(TAG, "=== onBindViewHolder position=" + position + " ===");
        Log.d(TAG, "maMonAn: " + maMonAn);
        Log.d(TAG, "tenMon: " + monAn.getTenMon());
        Log.d(TAG, "hinhAnh: " + monAn.getHinhAnh());

        holder.txtFood.setText(monAn.getTenMon());
        holder.txtGioiThieu.setText(monAn.getGioiThieu() != null ? monAn.getGioiThieu() : "");
        holder.txtNguyenLieu.setText(monAn.getNguyenLieu() != null ? monAn.getNguyenLieu() : "");

        // Khởi tạo index cho món ăn này nếu chưa có
        if (!currentImageIndexMap.containsKey(maMonAn)) {
            currentImageIndexMap.put(maMonAn, 0);
        }

        // Load hình ảnh từ API nếu chưa có trong cache
        if (!hinhAnhMap.containsKey(maMonAn)) {
            loadHinhAnhMonAn(holder, monAn);
        } else {
            // Hiển thị hình ảnh từ cache
            displayCurrentImage(holder, monAn);
        }

        // Xử lý nút Previous - chuyển ảnh trước
        holder.btnPre.setOnClickListener(v -> {
            List<HinhAnhMonAn> danhSachHinh = hinhAnhMap.get(maMonAn);
            if (danhSachHinh != null && !danhSachHinh.isEmpty()) {
                int currentIndex = currentImageIndexMap.get(maMonAn);
                if (currentIndex > 0) {
                    currentImageIndexMap.put(maMonAn, currentIndex - 1);
                    displayCurrentImage(holder, monAn);
                }
            }
        });

        // Xử lý nút Next - chuyển ảnh tiếp theo
        holder.btnNext.setOnClickListener(v -> {
            List<HinhAnhMonAn> danhSachHinh = hinhAnhMap.get(maMonAn);
            if (danhSachHinh != null && !danhSachHinh.isEmpty()) {
                int currentIndex = currentImageIndexMap.get(maMonAn);
                if (currentIndex < danhSachHinh.size() - 1) {
                    currentImageIndexMap.put(maMonAn, currentIndex + 1);
                    displayCurrentImage(holder, monAn);
                }
            }
        });
    }

    private void loadHinhAnhMonAn(FoodViewHolder holder, MonAn monAn) {
        Integer maMonAn = monAn.getMaMonAn();
        
        Log.d(TAG, "=== loadHinhAnhMonAn maMonAn=" + maMonAn + " ===");
        Log.d(TAG, "API URL: " + RetrofitClient.BASE_URL + "api/hinhanh-monan/monan/" + maMonAn);
        
        apiService.getHinhAnhMonAn(maMonAn).enqueue(new Callback<List<HinhAnhMonAn>>() {
            @Override
            public void onResponse(Call<List<HinhAnhMonAn>> call, Response<List<HinhAnhMonAn>> response) {
                Log.d(TAG, "API Response code: " + response.code());
                Log.d(TAG, "API Response body: " + (response.body() != null ? response.body().size() + " items" : "null"));
                
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<HinhAnhMonAn> danhSachHinh = response.body();
                    
                    // Log chi tiết từng hình ảnh
                    for (int i = 0; i < danhSachHinh.size(); i++) {
                        HinhAnhMonAn img = danhSachHinh.get(i);
                        Log.d(TAG, "  Image[" + i + "]: maHinhAnh=" + img.getMaHinhAnh() + ", duongDan=" + img.getDuongDan());
                    }
                    
                    hinhAnhMap.put(maMonAn, danhSachHinh);
                    currentImageIndexMap.put(maMonAn, 0);
                    displayCurrentImage(holder, monAn);
                } else {
                    Log.d(TAG, "Không có hình ảnh từ API (empty or null), fallback về hinhAnh của món ăn");
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            Log.e(TAG, "Cannot read error body");
                        }
                    }
                    // Fallback: sử dụng hinhAnh của món ăn
                    displayFallbackImage(holder, monAn);
                }
            }

            @Override
            public void onFailure(Call<List<HinhAnhMonAn>> call, Throwable t) {
                Log.e(TAG, "=== API FAILURE ===");
                Log.e(TAG, "Error type: " + t.getClass().getSimpleName());
                Log.e(TAG, "Error message: " + t.getMessage());
                t.printStackTrace();
                // Fallback: sử dụng hinhAnh của món ăn
                displayFallbackImage(holder, monAn);
            }
        });
    }
    
    /**
     * Hiển thị ảnh fallback từ MonAn.hinhAnh (server)
     */
    private void displayFallbackImage(FoodViewHolder holder, MonAn monAn) {
        String hinhAnh = monAn.getHinhAnh();
        
        Log.d(TAG, "=== displayFallbackImage ===");
        Log.d(TAG, "monAn.hinhAnh: " + hinhAnh);
        
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            // Load ảnh từ server
            String imageUrl = RetrofitClient.BASE_URL + "uploads/dishes/" + hinhAnh;
            Log.d(TAG, "Loading fallback image from SERVER: " + imageUrl);
            
            Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_main_dish_tt)
                .error(R.drawable.ic_main_dish_tt)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Glide FAILED to load: " + imageUrl);
                        if (e != null) {
                            Log.e(TAG, "Glide error: " + e.getMessage());
                            e.logRootCauses(TAG);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "Glide SUCCESS loaded: " + imageUrl);
                        return false;
                    }
                })
                .into(holder.imgMonAn);
        } else {
            Log.d(TAG, "hinhAnh is null or empty, using default image");
            holder.imgMonAn.setImageResource(R.drawable.ic_main_dish_tt);
        }
        
        // Ẩn circle indicators vì chỉ có 1 ảnh
        updateCircleIndicators(holder, 0, 1);
    }

    private void displayCurrentImage(FoodViewHolder holder, MonAn monAn) {
        Integer maMonAn = monAn.getMaMonAn();
        List<HinhAnhMonAn> danhSachHinh = hinhAnhMap.get(maMonAn);
        
        Log.d(TAG, "=== displayCurrentImage maMonAn=" + maMonAn + " ===");
        
        if (danhSachHinh != null && !danhSachHinh.isEmpty()) {
            int currentIndex = currentImageIndexMap.get(maMonAn);
            HinhAnhMonAn hinhAnh = danhSachHinh.get(currentIndex);
            
            // Load ảnh từ server URL
            String fileName = hinhAnh.getDuongDan();
            String imageUrl = RetrofitClient.BASE_URL + "uploads/dishes/" + fileName;
            
            Log.d(TAG, "Loading image[" + currentIndex + "] from URL: " + imageUrl);
            
            // Sử dụng Glide để load ảnh từ URL
            Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_main_dish_tt)
                .error(R.drawable.ic_main_dish_tt)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Glide FAILED to load: " + imageUrl);
                        if (e != null) {
                            Log.e(TAG, "Glide error: " + e.getMessage());
                            e.logRootCauses(TAG);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "Glide SUCCESS loaded: " + imageUrl);
                        return false;
                    }
                })
                .into(holder.imgMonAn);
            
            // Cập nhật circle indicators
            updateCircleIndicators(holder, currentIndex, danhSachHinh.size());
        } else {
            Log.d(TAG, "danhSachHinh is null or empty, using default");
            holder.imgMonAn.setImageResource(R.drawable.ic_main_dish_tt);
            updateCircleIndicators(holder, 0, 0);
        }
    }

    private void updateCircleIndicators(FoodViewHolder holder, int currentIndex, int totalImages) {
        ImageView[] circles = {holder.circle1, holder.circle2, holder.circle3,
                holder.circle4, holder.circle5};

        // Vì mỗi món ăn có 2 hình ảnh, chỉ hiển thị 2 chấm tròn đầu tiên
        for (int i = 0; i < circles.length; i++) {
            if (i < totalImages) {
                circles[i].setVisibility(View.VISIBLE);
                if (i == currentIndex) {
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