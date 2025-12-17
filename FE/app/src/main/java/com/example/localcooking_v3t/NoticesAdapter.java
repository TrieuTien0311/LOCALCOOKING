package com.example.localcooking_v3t;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.NoticeViewHolder> {

    private List<Notice> noticeList;
    private OnItemClickListener listener;

    // Interface cho sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Notice notice, int position);
    }

    public NoticesAdapter(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);

        holder.txtTitleTB.setText(notice.getTieuDeTB());
        holder.txtNoiDungTB.setText(notice.getNoiDungTB());
        holder.txtThoiGianTB.setText(notice.getThoiGianTB());
        holder.imgThongBao.setImageResource(notice.getAnhTB());

        // Thay đổi màu CardView dựa trên trạng thái
        if (!notice.isTrangThai()) {
            // Chưa đọc - màu đậm hơn (xám nhạt)
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E8E8E8"));
            // Có thể thêm độ đậm cho text
            holder.txtTitleTB.setTextColor(Color.parseColor("#000000"));
        } else {
            // Đã đọc - màu trắng
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.txtTitleTB.setTextColor(Color.parseColor("#666666"));
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Đánh dấu đã đọc khi click
                notice.setTrangThai(true);
                notifyItemChanged(position);
                listener.onItemClick(notice, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList != null ? noticeList.size() : 0;
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgThongBao;
        TextView txtTitleTB;
        TextView txtNoiDungTB;
        TextView txtThoiGianTB;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imgThongBao = itemView.findViewById(R.id.imgThongBao);
            txtTitleTB = itemView.findViewById(R.id.txtTitleTB);
            txtNoiDungTB = itemView.findViewById(R.id.txtNoiDungTB);
            txtThoiGianTB = itemView.findViewById(R.id.txtThoiGianTB);
        }
    }

    // Phương thức cập nhật danh sách
    public void updateNotices(List<Notice> newNotices) {
        this.noticeList = newNotices;
        notifyDataSetChanged();
    }
}
