package com.example.localcooking_v3t;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity xử lý deep link khi người dùng click vào link chia sẻ
 * Link format: https://localcooking.app/khoahoc/{maKhoaHoc}
 * hoặc: localcooking://khoahoc/{maKhoaHoc}
 */
public class DeepLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy data từ intent
        Intent intent = getIntent();
        Uri data = intent.getData();

        if (data != null) {
            // Lấy mã khóa học từ URL
            String path = data.getPath(); // /khoahoc/123
            String maKhoaHocStr = null;

            if (path != null && path.startsWith("/khoahoc/")) {
                maKhoaHocStr = path.replace("/khoahoc/", "");
            } else if (data.getHost() != null && data.getHost().equals("khoahoc")) {
                // Format: localcooking://khoahoc/123
                maKhoaHocStr = data.getLastPathSegment();
            }

            if (maKhoaHocStr != null && !maKhoaHocStr.isEmpty()) {
                try {
                    int maKhoaHoc = Integer.parseInt(maKhoaHocStr);
                    
                    // Chuyển đến MainActivity với thông tin khóa học
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    mainIntent.putExtra("openKhoaHoc", true);
                    mainIntent.putExtra("maKhoaHoc", maKhoaHoc);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Link không hợp lệ", Toast.LENGTH_SHORT).show();
                    openMainActivity();
                }
            } else {
                openMainActivity();
            }
        } else {
            openMainActivity();
        }

        finish();
    }

    private void openMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}