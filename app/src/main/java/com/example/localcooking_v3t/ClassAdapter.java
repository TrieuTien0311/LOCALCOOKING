package com.example.localcooking_v3t;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.LopHocViewHolder> {

    private List<Class> danhSachLopHoc;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDatLichClick(Class lopHoc);
        void onChiTietClick(Class lopHoc);
        void onFavoriteClick(Class lopHoc);
    }

    public ClassAdapter(List<Class> danhSachLopHoc) {
        this.danhSachLopHoc = danhSachLopHoc;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Class> newData) {
        this.danhSachLopHoc = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LopHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new LopHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LopHocViewHolder holder, int position) {
        Class lopHoc = danhSachLopHoc.get(position);

        holder.txtTenLop.setText(lopHoc.getTenLop());
        holder.txtMoTa.setText(lopHoc.getMoTa());
        holder.txtThoiGian.setText("Thời gian: " + lopHoc.getThoiGian());
        holder.txtNgay.setText("Ngày: " + lopHoc.getNgay());
        holder.txtDiaDiem.setText("Địa điểm: " + lopHoc.getDiaDiem());
        holder.txtGia.setText(lopHoc.getGia());
        holder.txtDanhGia.setText(lopHoc.getDanhGia() + " ⭐");
        holder.txtSoDanhGia.setText("(" + lopHoc.getSoDanhGia() + " đánh giá)");
        holder.imgMonAn.setImageResource(lopHoc.getHinhAnh());

        // Điều chỉnh marginTop của cardView dựa vào có ưu đãi hay không
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cardView.getLayoutParams();

        if (lopHoc.isCoUuDai()) {
            // Có ưu đãi: hiển thị banner và set marginTop = -15dp
            holder.layoutBanner.setVisibility(View.VISIBLE);
            holder.txtKetThuc.setText("Kết thúc sau " + lopHoc.getThoiGianKetThuc());
            params.topMargin = dpToPx(holder.itemView, -15);
        } else {
            // Không có ưu đãi: ẩn banner và set marginTop = 0dp
            holder.layoutBanner.setVisibility(View.GONE);
            params.topMargin = 0;
        }

        holder.cardView.setLayoutParams(params);

        // Xử lý sự kiện
        holder.btnDatLich.setOnClickListener(v -> {
            if (listener != null) listener.onDatLichClick(lopHoc);
        });

        holder.txtChiTiet.setOnClickListener(v -> {
            if (listener != null) listener.onChiTietClick(lopHoc);
        });

        holder.imgFavorite.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(lopHoc);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachLopHoc.size();
    }

    // Chuyển đổi dp sang pixel
    private int dpToPx(View view, int dp) {
        float density = view.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    static class LopHocViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout layoutBanner;
        ImageView imgMonAn, imgFavorite;
        TextView txtTenLop, txtMoTa, txtThoiGian, txtNgay, txtDiaDiem;
        TextView txtGia, txtDanhGia, txtSoDanhGia, txtChiTiet, txtKetThuc;
        Button btnDatLich;

        public LopHocViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            layoutBanner = itemView.findViewById(R.id.layoutBanner);
            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            txtTenLop = itemView.findViewById(R.id.txtTenLop);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtNgay = itemView.findViewById(R.id.txtNgay);
            txtDiaDiem = itemView.findViewById(R.id.txtDiaDiem);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtDanhGia = itemView.findViewById(R.id.txtDanhGia);
            txtSoDanhGia = itemView.findViewById(R.id.txtSoDanhGia);
            txtChiTiet = itemView.findViewById(R.id.txtChiTiet);
            txtKetThuc = itemView.findViewById(R.id.txtKetThuc);
            btnDatLich = itemView.findViewById(R.id.btnDatLich);
        }
    }
}