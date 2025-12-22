package com.example.localcooking_v3t;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DanhGiaDTO;
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

    private ImageView btnBack;
    private Button btnGuiDanhGia;
    
    // Views để hiển thị thông tin đơn hàng
    private ImageView imgFood;
    private TextView txtTenKhoaHoc;
    private TextView txtThoiGian;
    private TextView txtDiaDiem;
    
    // Views đánh giá sao
    private ImageView[] starViews;
    private TextView txtMucDoHaiLong;
    
    // Views nhập đánh giá
    private EditText edtBinhLuan;
    private LinearLayout layoutThemHinhAnh;
    private RecyclerView recyclerViewImages;
    private SelectedImageAdapter imageAdapter;
    
    // Dữ liệu nhận từ Intent
    private String orderTitle;
    private String hinhAnhUrl;
    private Integer maDatLich;
    private Integer maKhoaHoc;
    private String lich;
    private String diaDiem;
    private boolean isViewMode = false;
    
    // Dữ liệu đánh giá
    private int selectedRating = 5;
    private List<String> selectedImageUrls = new ArrayList<>();
    
    private ApiService apiService;
    
    // Launcher để chọn ảnh
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getApiService();
        
        initViews();
        setupImageRecyclerView();
        getIntentData();
        displayData();
        setupStarRating();
        setupClickListeners();
        
        checkReviewStatus();
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = ev.getRawX();
                float y = ev.getRawY();
                if (x < location[0] || x > location[0] + v.getWidth() ||
                        y < location[1] || y > location[1] + v.getHeight()) {
                    hideKeyboard();
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.imageView6);
        btnGuiDanhGia = findViewById(R.id.button);
        
        imgFood = findViewById(R.id.imgFood4);
        txtTenKhoaHoc = findViewById(R.id.textView18);
        txtThoiGian = findViewById(R.id.textView14);
        txtDiaDiem = findViewById(R.id.textView9);
        
        starViews = new ImageView[5];
        starViews[0] = findViewById(R.id.imageView10);
        starViews[1] = findViewById(R.id.imageView13);
        starViews[2] = findViewById(R.id.imageView16);
        starViews[3] = findViewById(R.id.imageView17);
        starViews[4] = findViewById(R.id.imageView18);
        
        txtMucDoHaiLong = findViewById(R.id.textView27);
        edtBinhLuan = findViewById(R.id.edtBinhLuan);
        layoutThemHinhAnh = findViewById(R.id.linearLayout2);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
    }
    
    private void setupImageRecyclerView() {
        imageAdapter = new SelectedImageAdapter();
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);
        
        imageAdapter.setOnImageRemoveListener(position -> {
            imageAdapter.removeImage(position);
            updateImageVisibility();
        });
    }
    
    private void updateImageVisibility() {
        if (imageAdapter.getImageCount() > 0) {
            recyclerViewImages.setVisibility(View.VISIBLE);
        } else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            orderTitle = intent.getStringExtra("orderTitle");
            hinhAnhUrl = intent.getStringExtra("hinhAnhUrl");
            maDatLich = intent.getIntExtra("maDatLich", -1);
            maKhoaHoc = intent.getIntExtra("maKhoaHoc", -1);
            lich = intent.getStringExtra("lich");
            diaDiem = intent.getStringExtra("diaDiem");
            isViewMode = intent.getBooleanExtra("isViewMode", false);
            
            Log.d(TAG, "maDatLich: " + maDatLich + ", isViewMode: " + isViewMode);
        }
    }
    
    private void displayData() {
        if (hinhAnhUrl != null && !hinhAnhUrl.isEmpty()) {
            String fullUrl = RetrofitClient.getFullImageUrl(hinhAnhUrl);
            Glide.with(this).load(fullUrl).placeholder(R.drawable.hue).error(R.drawable.hue).into(imgFood);
        }
        
        if (orderTitle != null) txtTenKhoaHoc.setText(orderTitle);
        if (lich != null) txtThoiGian.setText(lich);
        if (diaDiem != null) txtDiaDiem.setText("Địa điểm: " + diaDiem);
    }
    
    private void setupStarRating() {
        for (int i = 0; i < starViews.length; i++) {
            final int rating = i + 1;
            starViews[i].setOnClickListener(v -> {
                if (!isViewMode) setRating(rating);
            });
        }
        setRating(5);
    }
    
    private void setRating(int rating) {
        selectedRating = rating;
        for (int i = 0; i < starViews.length; i++) {
            starViews[i].setImageResource(i < rating ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        }
        String[] mucDoText = {"Rất tệ", "Tệ", "Bình thường", "Tốt", "Tuyệt vời!"};
        txtMucDoHaiLong.setText(mucDoText[rating - 1]);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnGuiDanhGia.setOnClickListener(v -> {
            if (isViewMode) finish();
            else submitReview();
        });
        
        layoutThemHinhAnh.setOnClickListener(v -> {
            if (!isViewMode) {
                if (imageAdapter.getImageCount() >= 5) {
                    Toast.makeText(this, "Đã đạt tối đa 5 ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                launchImagePicker();
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
        if (data.getClipData() != null) {
            // Chọn nhiều ảnh
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count && imageAdapter.getImageCount() < 5; i++) {
                Uri uri = data.getClipData().getItemAt(i).getUri();
                imageAdapter.addImage(uri);
            }
        } else if (data.getData() != null) {
            // Chọn 1 ảnh
            imageAdapter.addImage(data.getData());
        }
        updateImageVisibility();
    }
    
    private void checkReviewStatus() {
        if (maDatLich == null || maDatLich == -1) return;
        
        apiService.kiemTraDaDanhGia(maDatLich).enqueue(new Callback<KiemTraDanhGiaResponse>() {
            @Override
            public void onResponse(Call<KiemTraDanhGiaResponse> call, Response<KiemTraDanhGiaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KiemTraDanhGiaResponse result = response.body();
                    Log.d(TAG, "trangThaiDanhGia: " + result.getTrangThaiDanhGia());
                    
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
                Log.e(TAG, "Error checking review status", t);
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
            Log.d(TAG, "Số ảnh đánh giá: " + danhGia.getHinhAnhList().size());
            // Tạo adapter mới cho chế độ xem (không có nút xóa)
            imageAdapter.setViewMode(true);
            for (com.example.localcooking_v3t.model.HinhAnhDanhGiaDTO img : danhGia.getHinhAnhList()) {
                String duongDan = img.getDuongDan();
                String url = RetrofitClient.getFullImageUrl(duongDan);
                Log.d(TAG, "Đường dẫn gốc: " + duongDan + " -> URL đầy đủ: " + url);
                imageAdapter.addImageUrl(url);
            }
            updateImageVisibility();
        } else {
            Log.d(TAG, "Không có ảnh đánh giá hoặc list null");
        }
    }
    
    private void submitReview() {
        if (maDatLich == null || maDatLich == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy đơn đặt lịch", Toast.LENGTH_SHORT).show();
            return;
        }
        
        btnGuiDanhGia.setEnabled(false);
        btnGuiDanhGia.setText("Đang gửi...");
        
        List<Uri> imageUris = imageAdapter.getImageUris();
        
        if (imageUris.isEmpty()) {
            // Không có ảnh, gửi đánh giá luôn
            sendReviewToServer(new ArrayList<>());
        } else {
            // Có ảnh, upload trước rồi gửi đánh giá
            uploadImagesAndSubmit(imageUris);
        }
    }
    
    private void uploadImagesAndSubmit(List<Uri> imageUris) {
        List<String> uploadedUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0);
        int totalImages = imageUris.size();
        
        Log.d(TAG, "=== BẮT ĐẦU UPLOAD " + totalImages + " ẢNH ===");
        
        for (Uri uri : imageUris) {
            try {
                File file = createTempFileFromUri(uri);
                if (file == null) {
                    handleUploadError("Không thể đọc file ảnh");
                    return;
                }
                
                Log.d(TAG, "Đang upload file: " + file.getName() + ", size: " + file.length());
                
                String mimeType = getContentResolver().getType(uri);
                if (mimeType == null) mimeType = "image/jpeg";
                
                RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                
                File finalFile = file;
                apiService.uploadImage(part).enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            String fileUrl = response.body().getFileUrl();
                            Log.d(TAG, "Upload thành công! URL: " + fileUrl);
                            synchronized (uploadedUrls) {
                                uploadedUrls.add(fileUrl);
                            }
                        } else {
                            Log.e(TAG, "Upload thất bại: " + (response.body() != null ? response.body().getMessage() : "Unknown error"));
                        }
                        
                        // Xóa file tạm
                        finalFile.delete();
                        
                        // Kiểm tra đã upload xong tất cả chưa
                        if (uploadCount.incrementAndGet() == totalImages) {
                            Log.d(TAG, "=== UPLOAD XONG, số URL: " + uploadedUrls.size() + " ===");
                            // Gửi đánh giá với danh sách URL đã upload
                            sendReviewToServer(uploadedUrls);
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        Log.e(TAG, "Upload failed", t);
                        finalFile.delete();
                        if (uploadCount.incrementAndGet() == totalImages) {
                            sendReviewToServer(uploadedUrls);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error preparing upload", e);
                if (uploadCount.incrementAndGet() == totalImages) {
                    sendReviewToServer(uploadedUrls);
                }
            }
        }
    }
    
    private File createTempFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;
            
            String fileName = "upload_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(getCacheDir(), fileName);
            
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            
            return tempFile;
        } catch (Exception e) {
            Log.e(TAG, "Error creating temp file", e);
            return null;
        }
    }
    
    private void handleUploadError(String message) {
        runOnUiThread(() -> {
            btnGuiDanhGia.setEnabled(true);
            btnGuiDanhGia.setText("Gửi đánh giá");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }
    
    private void sendReviewToServer(List<String> imageUrls) {
        String binhLuan = edtBinhLuan != null ? edtBinhLuan.getText().toString().trim() : "";
        
        Log.d(TAG, "=== GỬI ĐÁNH GIÁ ===");
        Log.d(TAG, "maDatLich: " + maDatLich);
        Log.d(TAG, "selectedRating: " + selectedRating);
        Log.d(TAG, "binhLuan: " + binhLuan);
        Log.d(TAG, "imageUrls: " + imageUrls);
        
        TaoDanhGiaRequest request = new TaoDanhGiaRequest(
                maDatLich, selectedRating, binhLuan,
                imageUrls.isEmpty() ? null : imageUrls
        );
        
        apiService.taoDanhGia(request).enqueue(new Callback<TaoDanhGiaResponse>() {
            @Override
            public void onResponse(Call<TaoDanhGiaResponse> call, Response<TaoDanhGiaResponse> response) {
                btnGuiDanhGia.setEnabled(true);
                btnGuiDanhGia.setText("Gửi đánh giá");
                
                Log.d(TAG, "Response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d(TAG, "Đánh giá thành công!");
                    Toast.makeText(Review.this, "Cảm ơn bạn đã đánh giá! ⭐", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Lỗi khi gửi đánh giá";
                    Log.e(TAG, "Đánh giá thất bại: " + msg);
                    Toast.makeText(Review.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaoDanhGiaResponse> call, Throwable t) {
                btnGuiDanhGia.setEnabled(true);
                btnGuiDanhGia.setText("Gửi đánh giá");
                Toast.makeText(Review.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error submitting review", t);
            }
        });
    }
}
