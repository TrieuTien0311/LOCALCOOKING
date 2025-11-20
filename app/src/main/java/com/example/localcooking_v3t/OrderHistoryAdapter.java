package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderHistory> danhSachLichSuDatLich;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onHuyDatClick(OrderHistory lichSuDatLich);
        void onDatLaiClick(OrderHistory lichSuDatLich);
        void onDanhGiaClick(OrderHistory lichSuDatLich);
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

        holder.imgOrder.setImageResource(order.getHinhAnh());
        holder.txtTieuDe.setText(order.getTieuDe());
        holder.txtSoLuong.setText(order.getSoLuongNguoi() + " người");
        holder.txtLich.setText(order.getLich());
        holder.txtDiaDiem.setText(order.getDiaDiem());
        holder.txtGia.setText(order.getGia());
        holder.txtThoiGianHuy.setText("Đã hủy lúc: " + order.getThoiGianHuy());
        holder.txtTrangThai.setText(order.getTrangThai());

        switch (order.getTrangThai()) {
            case "Đặt trước":
                holder.btnHuy.setVisibility(View.VISIBLE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                break;

            case "Đã hoàn thành":
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.VISIBLE);
                holder.txtDanhGia.setVisibility(View.VISIBLE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                break;

            case "Đã hủy":
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.VISIBLE);
                break;

            default:
                holder.btnHuy.setVisibility(View.GONE);
                holder.txtDatLai.setVisibility(View.GONE);
                holder.txtDanhGia.setVisibility(View.GONE);
                holder.txtThoiGianHuy.setVisibility(View.GONE);
                break;
        }

        holder.btnHuy.setOnClickListener(v -> {
            if (listener != null) listener.onHuyDatClick(order);
        });

        holder.txtDatLai.setOnClickListener(v -> {
            if (listener != null) listener.onDatLaiClick(order);
        });

        holder.txtDanhGia.setOnClickListener(v -> {
            if (listener != null) listener.onDanhGiaClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachLichSuDatLich != null ? danhSachLichSuDatLich.size() : 0;
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrder;
        TextView txtTieuDe, txtSoLuong, txtLich, txtDiaDiem, txtGia, txtThoiGianHuy, txtTrangThai, txtDatLai, txtDanhGia;
        Button btnHuy;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.imgMonAnOrder);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongNguoi);
            txtLich = itemView.findViewById(R.id.txtLich);
            txtDiaDiem = itemView.findViewById(R.id.txtDiaDiem);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtThoiGianHuy = itemView.findViewById(R.id.txtThoiGianHuy);
            btnHuy = itemView.findViewById(R.id.btnHuyDat);
            txtDatLai = itemView.findViewById(R.id.txtDatLai);
            txtDanhGia = itemView.findViewById(R.id.txtDanhGia);
        }
    }
}
