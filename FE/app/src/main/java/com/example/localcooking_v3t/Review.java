package com.example.localcooking_v3t;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DanhGiaDTO;
import com.example.localcooking_v3t.model.HinhAnhDanhGiaDTO;
import com.example.localcooking_v3t.model.KiemTraDanhGiaResponse;
import com.example.localcooking_v3t.model.TaoDanhGiaRequest;
import com.example.localcooking_v3t.model.TaoDanhGiaResponse;
import com.example.localcooking_v3t.model.UploadResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Review extends AppCompatActivity {

    private static final String TAG = "Review";
    private static final int MAX_IMAGES = 5;

    // Views
    private ImageView btnBack;
    private Button btnGuiDanhGia;
    private ImageView imgFood;
    private TextView txtTenKhoaHoc, txtThoiGian, txtDiaDiem, txtMucDoHaiLong;
    private ImageView[] starViews;
    private EditText edtBinhLuan;
    private LinearLayout layoutThemHinhAnh, layoutSelectedImages;
    private HorizontalScrollView scrollViewImages;
    
    // Dữ liệu
    private Integer maDatLich, maKhoaHoc;
    private String orderTitle, hinhAnhUrl, lich, diaDiem;
    private boolean isViewMode = false;
    private int selectedRating = 5;
    
    // Danh sách ảnh đã chọn (URI local)
    private List<Uri> selectedImageUris = new ArrayList<>();
    
    private ApiService apiService;
    
    // Launcher chọn ảnh
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleImageSelection(result.getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        
        // Set màu status bar
        setStatusBarColor();

        apiService = RetrofitClient.getApiService();
        initViews();
        getIntentData();
        displayData();
        setupStarRating();
        setupClickListeners();
        checkReviewStatus();
    }
    
    private void setStatusBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(0xFFFFC59D); // Màu cam của Review
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Ẩn bàn phím khi click ra ngoài EditText
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                if (ev.getRawX() < location[0] || ev.getRawX() > location[0] + v.getWidth() ||
                    ev.getRawY() < location[1] || ev.getRawY() > location[1] + v.getHeight()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initViews() {
        btnBack = findViewById(R.id.imageView6);
        btnGuiDanhGia = findViewById(R.id.button);
        imgFood = findViewById(R.id.imgFood4);
        txtTenKhoaHoc = findViewById(R.id.textView18);
        txtThoiGian = findViewById(R.id.textView14);
        txtDiaDiem = findViewById(R.id.textView9);
        txtMucDoHaiLong = findViewById(R.id.textView27);
        edtBinhLuan = findViewById(R.id.edtBinhLuan);
        layoutThemHinhAnh = findViewById(R.id.linearLayout2);
        layoutSelectedImages = findViewById(R.id.layoutSelectedImages);
        scrollViewImages = findViewById(R.id.scrollViewImages);
        
        starViews = new ImageView[]{
            findViewById(R.id.imageView10),
            findViewById(R.id.imageView13),
            findViewById(R.id.imageView16),
            findViewById(R.id.imageView17),
            findViewById(R.id.imageView18)
        };
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            orderTitle = intent.getStringExtra("orderTitle");
            hinhAnhUrl = intent.getStringExtra("hinhAnhUrl");
            maDatLich = intent.getIntExtra("maDatLich", -1);
            maKhoaHoc = intent.getIntExtra("maKhoaHoc", -1);
            isViewMode = intent.getBooleanExtra("isViewMode", false);
            
            // Debug log để kiểm tra dữ liệu nhận được
            Log.d(TAG, "=== INTENT DATA ===");
            Log.d(TAG, "orderTitle: " + orderTitle);
            Log.d(TAG, "hinhAnhUrl: " + hinhAnhUrl);
            Log.d(TAG, "maDatLich: " + maDatLich);
            
            // Nhận thời gian và địa điểm từ các key khác nhau
            String orderTime = intent.getStringExtra("orderTime");
            String orderDate = intent.getStringExtra("orderDate");
            String orderLocation = intent.getStringExtra("orderLocation");
            
            Log.d(TAG, "orderTime: " + orderTime);
            Log.d(TAG, "orderDate: " + orderDate);
            Log.d(TAG, "orderLocation: " + orderLocation);
            
            // Ghép thời gian và ngày nếu có
            if (orderTime != null && orderDate != null) {
                lich = orderTime + ", " + orderDate;
            } else if (intent.getStringExtra("lich") != null) {
                lich = intent.getStringExtra("lich");
            }
            
            // Lấy địa điểm
            if (orderLocation != null) {
                diaDiem = orderLocation;
            } else if (intent.getStringExtra("diaDiem") != null) {
                diaDiem = intent.getStringExtra("diaDiem");
            }
        }
    }
    
    private void displayData() {
        Log.d(TAG, "=== DISPLAY DATA ===");
        Log.d(TAG, "hinhAnhUrl value: " + hinhAnhUrl);
        
        if (hinhAnhUrl != null && !hinhAnhUrl.isEmpty()) {
            String fullUrl = RetrofitClient.getFullImageUrl(hinhAnhUrl);
            Log.d(TAG, "Full image URL: " + fullUrl);
            
            Glide.with(this)
                 .load(fullUrl)
                 .placeholder(R.drawable.hue)
                 .error(R.drawable.hue)
                 .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                     @Override
                     public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                         Log.e(TAG, "Glide load FAILED: " + (e != null ? e.getMessage() : "unknown error"));
                         if (e != null) e.logRootCauses(TAG);
                         return false;
                     }

                     @Override
                     public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                         Log.d(TAG, "Glide load SUCCESS from: " + dataSource);
                         return false;
                     }
                 })
                 .into(imgFood);
        } else {
            Log.w(TAG, "hinhAnhUrl is NULL or EMPTY - using default image");
        }
        
        if (orderTitle != null) txtTenKhoaHoc.setText(orderTitle);
        if (lich != null) txtThoiGian.setText(lich);
        if (diaDiem != null) txtDiaDiem.setText("Địa điểm: " + diaDiem);
    }
    
    private void setupStarRating() {
        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            starViews[i].setOnClickListener(v -> { if (!isViewMode) setRating(rating); });
        }
        setRating(5);
    }
    
    private void setRating(int rating) {
        selectedRating = rating;
        String[] texts = {"Rất tệ", "Tệ", "Bình thường", "Tốt", "Tuyệt vời!"};
        txtMucDoHaiLong.setText(texts[rating - 1]);
        for (int i = 0; i < 5; i++) {
            starViews[i].setImageResource(i < rating ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnGuiDanhGia.setOnClickListener(v -> { if (isViewMode) finish(); else submitReview(); });
        layoutThemHinhAnh.setOnClickListener(v -> {
            if (!isViewMode) {
                if (selectedImageUris.size() >= MAX_IMAGES) {
                    Toast.makeText(this, "Đã đạt tối đa " + MAX_IMAGES + " ảnh/video", Toast.LENGTH_SHORT).show();
                } else {
                    launchImagePicker();
                }
            }
        });
    }
    
    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(intent);
    }

    
    private void handleImageSelection(Intent data) {
        int addedCount = 0, skippedCount = 0;
        
        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                if (selectedImageUris.size() < MAX_IMAGES) {
                    selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                    addedCount++;
                } else {
                    skippedCount++;
                }
            }
        } else if (data.getData() != null) {
            if (selectedImageUris.size() < MAX_IMAGES) {
                selectedImageUris.add(data.getData());
                addedCount++;
            } else {
                skippedCount++;
            }
        }
        
        refreshImageList();
        
        if (skippedCount > 0) {
            Toast.makeText(this, "Chỉ được chọn tối đa " + MAX_IMAGES + " ảnh/video. Đã bỏ qua " + skippedCount + " file.", Toast.LENGTH_LONG).show();
        }
    }
    
    // ========== HIỂN THỊ ẢNH ĐƠN GIẢN (không dùng Adapter) ==========
    
    private void refreshImageList() {
        layoutSelectedImages.removeAllViews();
        
        for (int i = 0; i < selectedImageUris.size(); i++) {
            addImageView(selectedImageUris.get(i), i, false, false);
        }
        
        scrollViewImages.setVisibility(selectedImageUris.isEmpty() ? View.GONE : View.VISIBLE);
    }
    
    private void addImageView(Uri uri, int index, boolean isUrl, boolean isVideo) {
        // Tạo container cho mỗi ảnh
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_selected_image, layoutSelectedImages, false);
        ImageView imgSelected = itemView.findViewById(R.id.imgSelected);
        ImageView btnRemove = itemView.findViewById(R.id.btnRemove);
        ImageView iconVideo = itemView.findViewById(R.id.iconVideo);
        
        // Load ảnh
        if (isUrl) {
            Glide.with(this).load(uri.toString()).centerCrop().placeholder(R.drawable.hue).into(imgSelected);
        } else {
            Glide.with(this).load(uri).centerCrop().placeholder(R.drawable.hue).into(imgSelected);
        }
        
        // Hiển thị icon video nếu là video
        String mimeType = isUrl ? null : getContentResolver().getType(uri);
        boolean isVideoFile = isVideo || (mimeType != null && mimeType.startsWith("video"));
        if (iconVideo != null) {
            iconVideo.setVisibility(isVideoFile ? View.VISIBLE : View.GONE);
        }
        
        // Xử lý nút xóa
        if (isViewMode) {
            btnRemove.setVisibility(View.GONE);
            // Click để xem fullscreen
            imgSelected.setOnClickListener(v -> openMediaViewer(uri.toString(), isVideoFile));
        } else {
            btnRemove.setVisibility(View.VISIBLE);
            final int pos = index;
            btnRemove.setOnClickListener(v -> {
                selectedImageUris.remove(pos);
                refreshImageList();
            });
        }
        
        layoutSelectedImages.addView(itemView);
    }
    
    private void openMediaViewer(String url, boolean isVideo) {
        Intent intent = new Intent(this, MediaViewerActivity.class);
        intent.putExtra(MediaViewerActivity.EXTRA_MEDIA_URL, url);
        intent.putExtra(MediaViewerActivity.EXTRA_IS_VIDEO, isVideo);
        startActivity(intent);
    }
    
    // ========== KIỂM TRA VÀ HIỂN THỊ ĐÁNH GIÁ ĐÃ CÓ ==========
    
    private void checkReviewStatus() {
        if (maDatLich == null || maDatLich == -1) return;
        
        apiService.kiemTraDaDanhGia(maDatLich).enqueue(new Callback<KiemTraDanhGiaResponse>() {
            @Override
            public void onResponse(Call<KiemTraDanhGiaResponse> call, Response<KiemTraDanhGiaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KiemTraDanhGiaResponse result = response.body();
                    if (result.daDanhGiaRoi()) {
                        isViewMode = true;
                        displayExistingReview(result.getDanhGia());
                    } else if (!result.coTheDanhGia()) {
                        Toast.makeText(Review.this, "Đơn này không thể đánh giá", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<KiemTraDanhGiaResponse> call, Throwable t) {
                Log.e(TAG, "Error checking review", t);
            }
        });
    }
    
    private void displayExistingReview(DanhGiaDTO danhGia) {
        if (danhGia == null) return;
        
        btnGuiDanhGia.setText("Đóng");
        if (danhGia.getDiemDanhGia() != null) setRating(danhGia.getDiemDanhGia());
        if (danhGia.getBinhLuan() != null) edtBinhLuan.setText(danhGia.getBinhLuan());
        
        edtBinhLuan.setEnabled(false);
        edtBinhLuan.setFocusable(false);
        layoutThemHinhAnh.setVisibility(View.GONE);
        for (ImageView star : starViews) star.setClickable(false);
        
        // Hiển thị ảnh đã đánh giá
        if (danhGia.getHinhAnhList() != null && !danhGia.getHinhAnhList().isEmpty()) {
            layoutSelectedImages.removeAllViews();
            for (int i = 0; i < danhGia.getHinhAnhList().size(); i++) {
                HinhAnhDanhGiaDTO img = danhGia.getHinhAnhList().get(i);
                String url = RetrofitClient.getFullImageUrl(img.getDuongDan());
                addImageView(Uri.parse(url), i, true, img.isVideo());
            }
            scrollViewImages.setVisibility(View.VISIBLE);
        }
    }
    
    // ========== GỬI ĐÁNH GIÁ ==========
    
    private void submitReview() {
        if (maDatLich == null || maDatLich == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy đơn đặt lịch", Toast.LENGTH_SHORT).show();
            return;
        }
        
        btnGuiDanhGia.setEnabled(false);
        btnGuiDanhGia.setText("Đang gửi...");
        
        if (selectedImageUris.isEmpty()) {
            sendReviewToServer(new ArrayList<>());
        } else {
            uploadImagesAndSubmit();
        }
    }
    
    private void uploadImagesAndSubmit() {
        List<String> uploadedUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0);
        int total = selectedImageUris.size();
        
        for (Uri uri : selectedImageUris) {
            try {
                File file = createTempFile(uri);
                if (file == null) continue;
                
                String mimeType = getContentResolver().getType(uri);
                if (mimeType == null) mimeType = "image/jpeg";
                
                RequestBody body = RequestBody.create(MediaType.parse(mimeType), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
                
                apiService.uploadImage(part).enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        file.delete();
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            synchronized (uploadedUrls) { uploadedUrls.add(response.body().getFileUrl()); }
                        }
                        if (uploadCount.incrementAndGet() == total) sendReviewToServer(uploadedUrls);
                    }
                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        file.delete();
                        if (uploadCount.incrementAndGet() == total) sendReviewToServer(uploadedUrls);
                    }
                });
            } catch (Exception e) {
                if (uploadCount.incrementAndGet() == total) sendReviewToServer(uploadedUrls);
            }
        }
    }
    
    private File createTempFile(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            if (is == null) return null;
            
            String mimeType = getContentResolver().getType(uri);
            String ext = ".jpg";
            if (mimeType != null) {
                if (mimeType.startsWith("video")) ext = ".mp4";
                else if (mimeType.contains("png")) ext = ".png";
                else if (mimeType.contains("gif")) ext = ".gif";
            }
            
            File file = new File(getCacheDir(), "upload_" + System.currentTimeMillis() + ext);
            FileOutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) os.write(buffer, 0, len);
            os.close();
            is.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void sendReviewToServer(List<String> imageUrls) {
        String binhLuan = edtBinhLuan.getText().toString().trim();
        TaoDanhGiaRequest request = new TaoDanhGiaRequest(maDatLich, selectedRating, binhLuan, 
                imageUrls.isEmpty() ? null : imageUrls);
        
        apiService.taoDanhGia(request).enqueue(new Callback<TaoDanhGiaResponse>() {
            @Override
            public void onResponse(Call<TaoDanhGiaResponse> call, Response<TaoDanhGiaResponse> response) {
                btnGuiDanhGia.setEnabled(true);
                btnGuiDanhGia.setText("Gửi đánh giá");
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(Review.this, "Cảm ơn bạn đã đánh giá! ⭐", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Lỗi khi gửi đánh giá";
                    Toast.makeText(Review.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<TaoDanhGiaResponse> call, Throwable t) {
                btnGuiDanhGia.setEnabled(true);
                btnGuiDanhGia.setText("Gửi đánh giá");
                Toast.makeText(Review.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
