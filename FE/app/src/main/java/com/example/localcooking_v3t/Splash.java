package com.example.localcooking_v3t;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView tvAppName, tvSubtitle;
    private View dot1, dot2, dot3;
    private View circle1, circle2, circle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        startAnimations();
    }

    private void initViews() {
        imgLogo = findViewById(R.id.imgLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);

        // Ẩn tất cả elements ban đầu
        imgLogo.setVisibility(View.INVISIBLE);
        tvAppName.setVisibility(View.INVISIBLE);
        tvSubtitle.setVisibility(View.INVISIBLE);
    }

    private void startAnimations() {
        // Animation cho background circles
        animateBackgroundCircles();

        // Bước 1: Logo bounce in (0-1000ms)
        new Handler().postDelayed(() -> {
            imgLogo.setVisibility(View.VISIBLE);
            Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_bounce_in);
            imgLogo.startAnimation(logoAnim);
        }, 300);

        // Bước 2: Text slide in (1600ms)
        new Handler().postDelayed(() -> {
            tvAppName.setVisibility(View.VISIBLE);
            Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_slide_in);
            tvAppName.startAnimation(textAnim);
        }, 1600);

        // Bước 3: Subtitle fade in (2200ms)
        new Handler().postDelayed(() -> {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setAlpha(0f);
            tvSubtitle.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 2200);

        // Bước 4: Logo pulse effect (2400ms)
        new Handler().postDelayed(() -> {
            Animation pulseAnim = AnimationUtils.loadAnimation(this, R.anim.logo_pulse);
            imgLogo.startAnimation(pulseAnim);
        }, 2400);

        // Bước 5: Loading dots animation (2800ms)
        new Handler().postDelayed(() -> {
            animateLoadingDots();
        }, 2800);

        // Bước 6: Fade out và chuyển màn hình (5000ms)
        new Handler().postDelayed(() -> {
            fadeOutAndNavigate();
        }, 5000);
    }

    private void animateBackgroundCircles() {
        // Circle 1 - xoay chậm
        ObjectAnimator rotate1 = ObjectAnimator.ofFloat(circle1, "rotation", 0f, 360f);
        rotate1.setDuration(8000);
        rotate1.setRepeatCount(ObjectAnimator.INFINITE);
        rotate1.start();

        // Circle 2 - xoay ngược chiều
        ObjectAnimator rotate2 = ObjectAnimator.ofFloat(circle2, "rotation", 360f, 0f);
        rotate2.setDuration(6000);
        rotate2.setRepeatCount(ObjectAnimator.INFINITE);
        rotate2.start();

        // Circle 3 - scale nhẹ
        ObjectAnimator scale3 = ObjectAnimator.ofFloat(circle3, "scaleX", 1f, 1.2f);
        scale3.setDuration(3000);
        scale3.setRepeatCount(ObjectAnimator.INFINITE);
        scale3.setRepeatMode(ObjectAnimator.REVERSE);
        scale3.start();

        ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(circle3, "scaleY", 1f, 1.2f);
        scaleY3.setDuration(3000);
        scaleY3.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY3.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY3.start();
    }

    private void animateLoadingDots() {
        // Dot 1 - nhảy lên rồi hạ xuống
        dot1.animate()
                .translationY(-20f)
                .scaleX(1.3f)
                .scaleY(1.3f)
                .alpha(1f)
                .setDuration(400)
                .withEndAction(() -> {
                    dot1.animate()
                            .translationY(0f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(0.6f)
                            .setDuration(400)
                            .start();
                })
                .start();

        // Dot 2 (delay 300ms)
        new Handler().postDelayed(() -> {
            dot2.animate()
                    .translationY(-20f)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .alpha(1f)
                    .setDuration(400)
                    .withEndAction(() -> {
                        dot2.animate()
                                .translationY(0f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(0.6f)
                                .setDuration(400)
                                .start();
                    })
                    .start();
        }, 300);

        // Dot 3 (delay 600ms)
        new Handler().postDelayed(() -> {
            dot3.animate()
                    .translationY(-20f)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .alpha(1f)
                    .setDuration(400)
                    .withEndAction(() -> {
                        dot3.animate()
                                .translationY(0f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(0.6f)
                                .setDuration(400)
                                .start();
                    })
                    .start();
        }, 600);

        // Lặp lại animation dots sau khi tất cả đã hoàn thành
        new Handler().postDelayed(() -> {
            animateLoadingDots();
        }, 1400);
    }

    private void fadeOutAndNavigate() {
        // Fade out toàn bộ màn hình
        findViewById(R.id.main1).animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Chuyển sang màn hình chính (Header - HomeFragment)
                        Intent intent = new Intent(Splash.this, Header.class);
                        startActivity(intent);
                        finish();
                        // Thêm transition effect
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                })
                .start();
    }
}