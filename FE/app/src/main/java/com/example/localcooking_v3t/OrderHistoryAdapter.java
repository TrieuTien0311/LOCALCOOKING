package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderHistory> danhSachLichSuDatLich;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onHuyDatClick(OrderHistory lichSuDatLich);
        void onDatLaiClick(OrderHistory lichSuDatLich);
        void onDanhGiaClick(OrderHistory lichSuDatLich);
        void onThanhToanClick(OrderHistory lichSuDatLich);
    }

    public OrderHistoryAdapter(List<OrderHistory> danhSachLichSuDatLich) {
        this.danhSachLichSuDatLich = danhSachLichSuDatLich;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<OrderHistory> newData) {
        this.danhSachLichSuDatLich = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistory order = danhSachLichSuDatLich.get(position);

        // Bind dữ liệu cơ bản
        holder.imgOrder.setImageResource(order.getHinhAnh());
        holder.txtTieuDe.setText(order.getTieuDe());
        holder.txtSoLuong.setText(order.getSoLuongNguoi());
        holder.txtLich.setText(order.getLich());
        holder.txtDiaDiem.setText(order.getDiaDiem());
        holder.txtGia.setText(order.getGia());

        switch (order.getTrangThai()) {
            case "Đặt trước":
                holder.btnHuy.setVisibility(View.VISIBLE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia2.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                break;

            case "Đã hoàn thành":
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.VISIBLE);
                holder.txtDanhGia2.setVisibility(View.VISIBLE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                
                // Kiểm tra đã đánh giá chưa để hiển thị text phù hợp
                if (order.isDaDanhGia()) {
                    holder.txtDanhGia2.setText("Xem đánh giá");
                } else {
                    holder.txtDanhGia2.setText("Đánh giá");
                }
                break;

            case "Đã hủy":
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia2.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.VISIBLE);
                break;

            default:
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia2.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                break;

        // Xử lý theo trạng thái
        String trangThai = order.getTrangThai();
        
        if ("Đặt trước".equals(trangThai)) {
            setupPreOrderItem(holder, order);
        } else if ("Đã hoàn thành".equals(trangThai)) {
            setupCompletedOrderItem(holder, order);
        } else if ("Đã hủy".equals(trangThai) || "Đã huỷ".equals(trangThai)) {
            setupCancelledOrderItem(holder, order);
        } else {
            // Mặc định ẩn hết
            holder.txtTrangThaiThanhToan.setVisibility(View.GONE);
            holder.btnHuy.setVisibility(View.GONE);
            holder.btnThanhToan.setVisibility(View.GONE);
            holder.txtDatLai.setVisibility(View.GONE);
            holder.txtDanhGia2.setVisibility(View.GONE);
            holder.txtThoiGianHuy.setVisibility(View.GONE);
        }

        // Click events
        holder.btnHuy.setOnClickListener(v -> {
            if (listener != null) listener.onHuyDatClick(order);
        });

        holder.btnThanhToan.setOnClickListener(v -> {
            if (listener != null) listener.onThanhToanClick(order);
        });

        holder.txtDatLai.setOnClickListener(v -> {
            if (listener != null) listener.onDatLaiClick(order);
        });

        holder.txtDanhGia2.setOnClickListener(v -> {
            if (listener != null) listener.onDanhGiaClick(order);
        });
    }
    
    /**
     * Thiết lập item cho tab "Đặt trước"
     */
    private void setupPreOrderItem(OrderHistoryViewHolder holder, OrderHistory order) {
        // Hiển thị badge trạng thái thanh toán
        holder.txtTrangThaiThanhToan.setVisibility(View.VISIBLE);
        
        // Ẩn các nút không cần thiết
        holder.txtThoiGianHuy.setVisibility(View.GONE);
        holder.txtDatLai.setVisibility(View.GONE);
        holder.txtDanhGia2.setVisibility(View.GONE);
        
        if (order.getDaThanhToan() != null && order.getDaThanhToan()) {
            // === ĐÃ THANH TOÁN ===
            holder.txtTrangThaiThanhToan.setText("✅ Đã thanh toán");
            holder.txtTrangThaiThanhToan.setBackgroundResource(R.drawable.badge_da_thanh_toan);
            
            // Chỉ hiển thị nút Hủy đặt
            holder.btnHuy.setVisibility(View.VISIBLE);
            holder.btnThanhToan.setVisibility(View.GONE);
        } else {
            // === CHƯA THANH TOÁN ===
            holder.txtTrangThaiThanhToan.setText("⏳ Chưa thanh toán");
            holder.txtTrangThaiThanhToan.setBackgroundResource(R.drawable.badge_chua_thanh_toan);
            
            // Hiển thị cả 2 nút: Hủy đặt + Thanh toán
            holder.btnHuy.setVisibility(View.VISIBLE);
            holder.btnThanhToan.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Thiết lập item cho tab "Đã hoàn thành"
     */
    private void setupCompletedOrderItem(OrderHistoryViewHolder holder, OrderHistory order) {
        // Ẩn badge và các nút action chính
        holder.txtTrangThaiThanhToan.setVisibility(View.GONE);
        holder.txtThoiGianHuy.setVisibility(View.GONE);
        holder.btnHuy.setVisibility(View.GONE);
        holder.btnThanhToan.setVisibility(View.GONE);
        
        // Hiển thị nút Đặt lại và Đánh giá
        holder.txtDatLai.setVisibility(View.VISIBLE);
        holder.txtDanhGia2.setVisibility(View.VISIBLE);
        
        // Cập nhật text nút đánh giá
        if (order.isDaDanhGia()) {
            holder.txtDanhGia2.setText("Xem đánh giá");
        } else {
            holder.txtDanhGia2.setText("Đánh giá");
        }
    }
    
    /**
     * Thiết lập item cho tab "Đã hủy"
     */
    private void setupCancelledOrderItem(OrderHistoryViewHolder holder, OrderHistory order) {
        // Ẩn badge và tất cả các nút
        holder.txtTrangThaiThanhToan.setVisibility(View.GONE);
        holder.btnHuy.setVisibility(View.GONE);
        holder.btnThanhToan.setVisibility(View.GONE);
        holder.txtDatLai.setVisibility(View.GONE);
        holder.txtDanhGia2.setVisibility(View.GONE);
        
        // Hiển thị thời gian hủy
        if (order.getThoiGianHuy() != null && !order.getThoiGianHuy().isEmpty()) {
            holder.txtThoiGianHuy.setVisibility(View.VISIBLE);
            holder.txtThoiGianHuy.setText("Đã huỷ lúc " + order.getThoiGianHuy());
        } else {
            holder.txtThoiGianHuy.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return danhSachLichSuDatLich != null ? danhSachLichSuDatLich.size() : 0;
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrder;
        TextView txtTrangThaiThanhToan;
        TextView txtTieuDe, txtSoLuong, txtLich, txtDiaDiem, txtGia, txtThoiGianHuy, txtDatLai, txtDanhGia2;
        Button btnHuy, btnThanhToan;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.imgMonAnOrder);
            txtTrangThaiThanhToan = itemView.findViewById(R.id.txtTrangThaiThanhToan);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongNguoi);
            txtLich = itemView.findViewById(R.id.txtLich);
            txtDiaDiem = itemView.findViewById(R.id.txtDiaDiem);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtThoiGianHuy = itemView.findViewById(R.id.txtThoiGianHuy);
            btnHuy = itemView.findViewById(R.id.btnHuyDat);
            btnThanhToan = itemView.findViewById(R.id.btnThanhToan);
            txtDatLai = itemView.findViewById(R.id.txtDatLai);
            txtDanhGia2 = itemView.findViewById(R.id.txtDanhGia2);
        }
    }
}
