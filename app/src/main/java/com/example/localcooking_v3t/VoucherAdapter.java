package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.UuDaiViewHolder> {

    private List<Voucher> danhSachUuDai;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDuocChonClick(Voucher uuDai);
    }

    public VoucherAdapter(List<Voucher> danhSachUuDai) {
        this.danhSachUuDai = danhSachUuDai;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UuDaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vouchers, parent, false);
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

        holder.rdbChon.setOnClickListener(v -> {

            // Bỏ chọn tất cả voucher khác
            for (Voucher vc : danhSachUuDai) {
                vc.setDuocChon(false);
            }

            // Chọn voucher hiện tại
            uuDai.setDuocChon(true);

            notifyDataSetChanged();

            if (listener != null) listener.onDuocChonClick(uuDai);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachUuDai.size();
    }

    static class UuDaiViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgMonAn;
        TextView txtTieuDe, txtMoTa, txtHSD;
        RadioButton rdbChon;

        public UuDaiViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDe);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtHSD = itemView.findViewById(R.id.txtHSD);
            rdbChon = itemView.findViewById(R.id.rdbChon);
        }
    }
}
