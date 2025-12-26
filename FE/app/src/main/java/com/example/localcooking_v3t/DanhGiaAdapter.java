package com.example.localcooking_v3t;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DanhGiaDTO;
import com.example.localcooking_v3t.model.HinhAnhDanhGiaDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.DanhGiaViewHolder> {

    private List<DanhGiaDTO> danhGiaList;
    private Context context;
    private OnMediaClickListener mediaClickListener;

    public interface OnMediaClickListener {
        void onMediaClick(List<HinhAnhDanhGiaDTO> mediaList, int position);
    }

    public DanhGiaAdapter(List<DanhGiaDTO> danhGiaList, Context context) {
        this.danhGiaList = danhGiaList;
        this.context = context;
    }

    public void setOnMediaClickListener(OnMediaClickListener listener) {
        this.mediaClickListener = listener;
    }

    public void updateData(List<DanhGiaDTO> newData) {
        this.danhGiaList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DanhGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_danh_gia, parent, false);
        return new DanhGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGiaViewHolder holder, int position) {
        DanhGiaDTO danhGia = danhGiaList.get(position);

        // Tên người đánh giá
        holder.txtTenDangNhap.setText(danhGia.getTenDangNhap() != null ? danhGia.getTenDangNhap() : "Ẩn danh");

        // Avatar
        holder.imgAvatar.setImageResource(R.drawable.avatar_evaluate);

        // Hiển thị sao đánh giá
        displayStars(holder.layoutSaoDanhGia, danhGia.getDiemDanhGia());

        // Ngày tham gia - hiển thị "Đã tham gia + ngày"
        String ngayThamGia = danhGia.getNgayThamGia() != null ? danhGia.getNgayThamGia() : "";
        holder.txtDaThamGia.setText("Đã tham gia " + ngayThamGia);

        // Ngày đánh giá
        if (holder.txtNgayDanhGia != null && danhGia.getNgayDanhGia() != null) {
            holder.txtNgayDanhGia.setText("Đăng lúc: " + danhGia.getNgayDanhGia());
            holder.txtNgayDanhGia.setVisibility(View.VISIBLE);
        }

        // Nội dung đánh giá
        String binhLuan = danhGia.getBinhLuan();
        if (binhLuan != null && !binhLuan.isEmpty()) {
            holder.txtNoiDungDG.setText(binhLuan);
            holder.txtNoiDungDG.setVisibility(View.VISIBLE);
        } else {
            holder.txtNoiDungDG.setVisibility(View.GONE);
        }

        // Hiển thị hình ảnh/video nếu có
        if (danhGia.hasMedia()) {
            holder.layoutMedia.setVisibility(View.VISIBLE);
            displayMedia(holder, danhGia.getHinhAnhList());
        } else {
            holder.layoutMedia.setVisibility(View.GONE);
        }

        // Phản hồi của trung tâm (mặc định hiển thị)
        holder.layoutPhanHoi.setVisibility(View.VISIBLE);
    }

    private void displayStars(LinearLayout layout, Integer diemDanhGia) {
        layout.removeAllViews();
        int stars = diemDanhGia != null ? diemDanhGia : 0;

        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(15), dpToPx(15));
            params.setMarginEnd(dpToPx(2));
            star.setLayoutParams(params);

            if (i < stars) {
                star.setImageResource(R.drawable.ic_star);
            } else {
                star.setImageResource(R.drawable.ic_star_outline);
            }
            layout.addView(star);
        }
    }

    private void displayMedia(DanhGiaViewHolder holder, List<HinhAnhDanhGiaDTO> mediaList) {
        holder.layoutMediaImages.removeAllViews();

        int maxDisplay = Math.min(mediaList.size(), 3);
        for (int i = 0; i < maxDisplay; i++) {
            HinhAnhDanhGiaDTO media = mediaList.get(i);
            
            // Tạo container cho ảnh/video
            android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(context);
            LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(
                    dpToPx(80), dpToPx(80));
            frameParams.setMarginEnd(dpToPx(8));
            frameLayout.setLayoutParams(frameParams);
            
            ImageView imageView = new ImageView(context);
            android.widget.FrameLayout.LayoutParams imgParams = new android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imgParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Load hình ảnh với full URL
            String fullUrl = RetrofitClient.getFullImageUrl(media.getDuongDan());
            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.hue)
                    .error(R.drawable.hue)
                    .into(imageView);

            frameLayout.addView(imageView);

            // Nếu là video, hiển thị icon play overlay
            if (media.isVideo()) {
                ImageView playIcon = new ImageView(context);
                android.widget.FrameLayout.LayoutParams playParams = new android.widget.FrameLayout.LayoutParams(
                        dpToPx(30), dpToPx(30));
                playParams.gravity = android.view.Gravity.CENTER;
                playIcon.setLayoutParams(playParams);
                playIcon.setImageResource(R.drawable.ic_play_circle);
                playIcon.setColorFilter(0xFFFFFFFF);
                frameLayout.addView(playIcon);
            }

            // Click để xem full
            final int pos = i;
            frameLayout.setOnClickListener(v -> {
                if (mediaClickListener != null) {
                    mediaClickListener.onMediaClick(mediaList, pos);
                }
            });

            holder.layoutMediaImages.addView(frameLayout);
        }

        // Hiển thị số lượng còn lại nếu > 3
        if (mediaList.size() > 3) {
            TextView txtMore = new TextView(context);
            txtMore.setText("+" + (mediaList.size() - 3));
            txtMore.setTextSize(14);
            txtMore.setTextColor(0xFF734322);
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txtParams.gravity = android.view.Gravity.CENTER_VERTICAL;
            txtMore.setLayoutParams(txtParams);
            holder.layoutMediaImages.addView(txtMore);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public int getItemCount() {
        return danhGiaList != null ? danhGiaList.size() : 0;
    }

    static class DanhGiaViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        TextView txtTenDangNhap;
        LinearLayout layoutSaoDanhGia;
        TextView txtDaThamGia;
        TextView txtNgayDanhGia;
        TextView txtNoiDungDG;
        LinearLayout layoutMedia;
        LinearLayout layoutMediaImages;
        LinearLayout layoutPhanHoi;

        public DanhGiaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTenDangNhap = itemView.findViewById(R.id.txtTenDangNhap);
            layoutSaoDanhGia = itemView.findViewById(R.id.SaoDanhGia);
            txtDaThamGia = itemView.findViewById(R.id.txtDaThamGia);
            txtNgayDanhGia = itemView.findViewById(R.id.txtNgayDanhGia);
            txtNoiDungDG = itemView.findViewById(R.id.txtNoiDungDG);
            layoutMedia = itemView.findViewById(R.id.layoutMedia);
            layoutMediaImages = itemView.findViewById(R.id.layoutMediaImages);
            layoutPhanHoi = itemView.findViewById(R.id.PhanHoi);
        }
    }
}
