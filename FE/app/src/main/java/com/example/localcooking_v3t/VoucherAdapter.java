package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.UuDaiViewHolder> {

    private List<Voucher> danhSachUuDai;
    private OnItemClickListener listener;
    private OnVoucherDeselectedListener deselectedListener;
    private boolean hienRadioButton = true;  // mặc định: có nút chọn
    private String preSelectedMaCode = null; // Mã voucher đã chọn trước đó

    public void setHienRadioButton(boolean hien) {
        this.hienRadioButton = hien;
    }
    
    /**
     * Set mã voucher đã chọn trước đó (để giữ trạng thái khi quay lại)
     */
    public void setPreSelectedMaCode(String maCode) {
        this.preSelectedMaCode = maCode;
    }

    public interface OnItemClickListener {
        void onDuocChonClick(Voucher uuDai);
    }
    
    public interface OnVoucherDeselectedListener {
        void onVoucherDeselected();
    }

    public VoucherAdapter(List<Voucher> danhSachUuDai) {
        this.danhSachUuDai = danhSachUuDai;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    public void setOnVoucherDeselectedListener(OnVoucherDeselectedListener listener) {
        this.deselectedListener = listener;
    }

    @NonNull
    @Override
    public UuDaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voucher, parent, false);
        return new UuDaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UuDaiViewHolder holder, int position) {
        Voucher uuDai = danhSachUuDai.get(position);

        holder.imgMonAn.setImageResource(uuDai.getHinhAnh());
        holder.txtTieuDe.setText(uuDai.getTieuDe());
        holder.txtMoTa.setText(uuDai.getMoTa());
        holder.txtHSD.setText("HSD: " + uuDai.getHSD());

        // Set trạng thái RadioButton
        holder.rdbChon.setChecked(uuDai.isDuocChon());

        // Điều khiển hiển thị radio
        if (!hienRadioButton) {
            holder.rdbChon.setVisibility(View.GONE);
        } else {
            holder.rdbChon.setVisibility(View.VISIBLE);
            holder.rdbChon.setChecked(uuDai.isDuocChon());
            
            // Xử lý click radio button - cho phép toggle (tick/untick)
            holder.rdbChon.setOnClickListener(v -> {
                boolean wasSelected = uuDai.isDuocChon();
                
                // Bỏ chọn tất cả voucher khác
                for (Voucher vc : danhSachUuDai) {
                    vc.setDuocChon(false);
                }
                
                if (wasSelected) {
                    // Nếu đang chọn -> bỏ chọn (untick)
                    uuDai.setDuocChon(false);
                    notifyDataSetChanged();
                    
                    // Thông báo đã bỏ chọn
                    if (deselectedListener != null) {
                        deselectedListener.onVoucherDeselected();
                    }
                } else {
                    // Nếu chưa chọn -> chọn (tick)
                    uuDai.setDuocChon(true);
                    notifyDataSetChanged();
                    
                    // Thông báo đã chọn
                    if (listener != null) {
                        listener.onDuocChonClick(uuDai);
                    }
                }
            });
            
            // Xử lý click cả item (không chỉ radio button)
            holder.rootView.setOnClickListener(v -> {
                holder.rdbChon.performClick();
            });
        }
    }

    @Override
    public int getItemCount() {
        return danhSachUuDai.size();
    }
    
    /**
     * Lấy voucher đang được chọn
     */
    public Voucher getSelectedVoucher() {
        for (Voucher vc : danhSachUuDai) {
            if (vc.isDuocChon()) {
                return vc;
            }
        }
        return null;
    }
    
    /**
     * Bỏ chọn tất cả voucher
     */
    public void clearSelection() {
        for (Voucher vc : danhSachUuDai) {
            vc.setDuocChon(false);
        }
        notifyDataSetChanged();
    }

    static class UuDaiViewHolder extends RecyclerView.ViewHolder {
        View rootView; // Root view (CardView)
        ImageView imgMonAn;
        TextView txtTieuDe, txtMoTa, txtHSD;
        RadioButton rdbChon;

        public UuDaiViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView; // itemView chính là CardView (root element)
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtHSD = itemView.findViewById(R.id.txtHSD);
            rdbChon = itemView.findViewById(R.id.rdbChon);
        }
    }
}
