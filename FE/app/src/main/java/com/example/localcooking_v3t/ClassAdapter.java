package com.example.localcooking_v3t;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.model.LopHoc;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    
    private List<LopHoc> lopHocList;
    private OnItemClickListener listener;
    private String selectedDate; // Ngày được chọn từ calendar
    
    public interface OnItemClickListener {
        void onDatLichClick(LopHoc lopHoc);
        void onChiTietClick(LopHoc lopHoc);
        void onFavoriteClick(LopHoc lopHoc);
    }
    
    public ClassAdapter(List<LopHoc> lopHocList, String selectedDate) {
        this.lopHocList = lopHocList;
        this.selectedDate = selectedDate;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    public void updateData(List<LopHoc> newData) {
        this.lopHocList = newData;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }
    // Điều chỉnh marginTop của cardView dựa vào có ưu đãi hay không


    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        LopHoc lopHoc = lopHocList.get(position);
        
        // Hủy timer cũ nếu có
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        
        // Hiển thị banner ưu đãi và điều chỉnh marginTop của cardView
        if (lopHoc.getCoUuDai() != null && lopHoc.getCoUuDai()) {
            // Có ưu đãi: hiển thị banner và set marginTop = -15dp
            holder.layoutBanner.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cardView.getLayoutParams();
            params.topMargin = (int) (-15 * holder.itemView.getContext().getResources().getDisplayMetrics().density);
            holder.cardView.setLayoutParams(params);
            
            // Bắt đầu countdown timer
            startCountdownTimer(holder);
        } else {
            // Không có ưu đãi: ẩn banner và set marginTop = 0dp
            holder.layoutBanner.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cardView.getLayoutParams();
            params.topMargin = 0;
            holder.cardView.setLayoutParams(params);
        }
        
        // Hiển thị thông tin lớp học
        holder.txtTenLop.setText(lopHoc.getTenLop());
        holder.txtMoTa.setText(lopHoc.getMoTa());
        holder.txtThoiGian.setText("Thời gian: " + lopHoc.getThoiGian());
        
        // Hiển thị ngày được chọn từ calendar (nếu có), nếu không thì hiển thị ngày bắt đầu
        String ngayHienThi;
        if (selectedDate != null && !selectedDate.isEmpty()) {
            // Lấy phần ngày từ selectedDate (format: "T4, 19/12/2024")
            String[] parts = selectedDate.split(", ");
            ngayHienThi = parts.length > 1 ? parts[1] : formatDate(lopHoc.getNgayBatDau());
        } else {
            ngayHienThi = formatDate(lopHoc.getNgayBatDau());
        }
        holder.txtNgay.setText("Ngày: " + ngayHienThi);
        
        holder.txtDiaDiem.setText("Địa điểm: " + lopHoc.getDiaDiem());
        
        // Xử lý hiển thị giá với ưu đãi
        if (lopHoc.getCoUuDai() != null && lopHoc.getCoUuDai()) {
            // Có ưu đãi: hiển thị giá gốc bị gạch và giá sau giảm
            holder.txtGiaGoc.setVisibility(View.VISIBLE);
            holder.txtGiaGoc.setText(lopHoc.getGia());
            holder.txtGiaGoc.setPaintFlags(holder.txtGiaGoc.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            
            // Tính giá sau giảm 10%
            String giaSauGiam = calculateDiscountPrice(lopHoc.getGia(), 10);
            holder.txtGia.setText(giaSauGiam);
        } else {
            // Không có ưu đãi: chỉ hiển thị giá gốc
            holder.txtGiaGoc.setVisibility(View.GONE);
            holder.txtGia.setText(lopHoc.getGia());
        }
        
        // Hiển thị đánh giá
        if (lopHoc.getDanhGia() != null && lopHoc.getSoDanhGia() != null) {
            holder.txtDanhGia.setText(String.format("%.1f ⭐", lopHoc.getDanhGia()));
            holder.txtSoDanhGia.setText(String.format("(%d đánh giá)", lopHoc.getSoDanhGia()));
        } else {
            holder.txtDanhGia.setText("Chưa có đánh giá");
            holder.txtSoDanhGia.setText("");
        }
        
        // Hiển thị số suất còn lại
        if (lopHoc.getSuat() != null) {
            holder.txtSuat.setText("Còn " + lopHoc.getSuat() + " suất");
        }
        
        // Xử lý trạng thái yêu thích
        if (lopHoc.getIsFavorite() != null && lopHoc.getIsFavorite()) {
            holder.imgFavorite.setImageResource(R.drawable.ic_heartredfilled);
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_heart);
        }
        
        // Xử lý trạng thái đã diễn ra
        if (lopHoc.getDaDienRa() != null && lopHoc.getDaDienRa()) {
            holder.overlayDim.setVisibility(View.VISIBLE);
            holder.txtDaDienRa.setVisibility(View.VISIBLE);
        } else {
            holder.overlayDim.setVisibility(View.GONE);
            holder.txtDaDienRa.setVisibility(View.GONE);
        }

        // Xử lý sự kiện click
        holder.btnDatLich.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDatLichClick(lopHoc);
            }
        });
        
        holder.footer.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChiTietClick(lopHoc);
            }
        });
        
        holder.imgFavorite.setOnClickListener(v -> {
            if (listener != null) {
                // Toggle favorite
                lopHoc.setIsFavorite(!(lopHoc.getIsFavorite() != null && lopHoc.getIsFavorite()));
                notifyItemChanged(position);
                listener.onFavoriteClick(lopHoc);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return lopHocList.size();
    }
    
    @Override
    public void onViewRecycled(@NonNull ClassViewHolder holder) {
        super.onViewRecycled(holder);
        // Hủy timer khi view bị recycle
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
    }
    
    /**
     * Format ngày từ "2025-01-15" sang "15/01/2025"
     */
    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }
        
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) {
            // Nếu lỗi, trả về ngày gốc
        }
        
        return dateStr;
    }
    
    /**
     * Tính giá sau khi giảm phần trăm
     */
    private String calculateDiscountPrice(String originalPrice, int discountPercent) {
        if (originalPrice == null || originalPrice.isEmpty()) {
            return originalPrice;
        }
        
        try {
            // Loại bỏ ký tự không phải số (đ, ₫, dấu chấm, dấu phẩy)
            String priceStr = originalPrice.replaceAll("[^0-9]", "");
            double price = Double.parseDouble(priceStr);
            
            // Tính giá sau giảm
            double discountedPrice = price * (100 - discountPercent) / 100;
            
            // Format lại với dấu chấm phân cách hàng nghìn
            java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
            return formatter.format(discountedPrice).replace(",", ".") + "đ";
        } catch (Exception e) {
            return originalPrice;
        }
    }
    
    /**
     * Bắt đầu countdown timer đếm ngược về 00:00
     */
    private void startCountdownTimer(ClassViewHolder holder) {
        // Tính thời gian còn lại đến 00:00
        Calendar now = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DAY_OF_MONTH, 1); // Nửa đêm ngày mai
        
        long timeUntilMidnight = midnight.getTimeInMillis() - now.getTimeInMillis();
        
        holder.countDownTimer = new CountDownTimer(timeUntilMidnight, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / (1000 * 60 * 60);
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                
                String timeText = String.format("Kết thúc sau %02d:%02d:%02d", hours, minutes, seconds);
                holder.txtKetThuc.setText(timeText);
            }
            
            @Override
            public void onFinish() {
                holder.txtKetThuc.setText("Đã hết hạn");
            }
        };
        
        holder.countDownTimer.start();
    }
    
    static class ClassViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutBanner, footer;
        ImageView imgMonAn, imgFavorite;
        TextView txtTenLop, txtMoTa, txtThoiGian, txtNgay, txtDiaDiem;
        TextView txtGia, txtGiaGoc, txtDanhGia, txtSoDanhGia, txtSuat, txtKetThuc;
        MaterialButton btnDatLich;
        View overlayDim;
        TextView txtDaDienRa;
        androidx.cardview.widget.CardView cardView;
        CountDownTimer countDownTimer;
        
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutBanner = itemView.findViewById(R.id.layoutBanner);
            footer = itemView.findViewById(R.id.footer);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            txtTenLop = itemView.findViewById(R.id.txtTenLop);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtNgay = itemView.findViewById(R.id.txtNgay);
            txtDiaDiem = itemView.findViewById(R.id.txtDiaDiem);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtGiaGoc = itemView.findViewById(R.id.txtGiaGoc);
            txtDanhGia = itemView.findViewById(R.id.txtDanhGia);
            txtSoDanhGia = itemView.findViewById(R.id.txtSoDanhGia);
            txtSuat = itemView.findViewById(R.id.txtSuat);
            btnDatLich = itemView.findViewById(R.id.btnDatLich);
            overlayDim = itemView.findViewById(R.id.overlayDim);
            txtDaDienRa = itemView.findViewById(R.id.txtDaDienRa);
            cardView = itemView.findViewById(R.id.cardView);
            txtKetThuc = itemView.findViewById(R.id.txtKetThuc);
        }
    }
}
