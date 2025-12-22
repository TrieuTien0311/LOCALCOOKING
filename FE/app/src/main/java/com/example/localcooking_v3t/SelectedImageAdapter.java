package com.example.localcooking_v3t;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ImageViewHolder> {

    private List<Uri> imageUris = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>(); // Cho chế độ xem
    private OnImageRemoveListener removeListener;
    private boolean isViewMode = false;

    public interface OnImageRemoveListener {
        void onRemove(int position);
    }

    public void setOnImageRemoveListener(OnImageRemoveListener listener) {
        this.removeListener = listener;
    }
    
    public void setViewMode(boolean viewMode) {
        this.isViewMode = viewMode;
    }

    public void addImage(Uri uri) {
        if (imageUris.size() + imageUrls.size() < 5) {
            imageUris.add(uri);
            notifyItemInserted(getItemCount() - 1);
        }
    }
    
    public void addImageUrl(String url) {
        if (imageUris.size() + imageUrls.size() < 5) {
            imageUrls.add(url);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void removeImage(int position) {
        if (position >= 0 && position < imageUris.size()) {
            imageUris.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public List<Uri> getImageUris() {
        return imageUris;
    }

    public int getImageCount() {
        return imageUris.size() + imageUrls.size();
    }

    public void clear() {
        imageUris.clear();
        imageUrls.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Ẩn/hiện nút xóa dựa trên chế độ
        holder.btnRemove.setVisibility(isViewMode ? View.GONE : View.VISIBLE);
        
        if (position < imageUris.size()) {
            // Hiển thị từ URI (ảnh mới chọn)
            Uri uri = imageUris.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .centerCrop()
                    .into(holder.imgSelected);
        } else {
            // Hiển thị từ URL (ảnh đã lưu)
            int urlIndex = position - imageUris.size();
            if (urlIndex < imageUrls.size()) {
                String url = imageUrls.get(urlIndex);
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.hue)
                        .error(R.drawable.hue)
                        .into(holder.imgSelected);
            }
        }

        holder.btnRemove.setOnClickListener(v -> {
            if (!isViewMode && removeListener != null) {
                removeListener.onRemove(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size() + imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgSelected;
        ImageView btnRemove;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
