package com.example.localcooking_v3t;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;

public class MediaViewerActivity extends AppCompatActivity {

    public static final String EXTRA_MEDIA_URL = "media_url";
    public static final String EXTRA_IS_VIDEO = "is_video";

    private PhotoView photoView;
    private VideoView videoView;
    private ImageView btnClose;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Fullscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        setContentView(R.layout.activity_media_viewer);

        initViews();
        loadMedia();
    }

    private void initViews() {
        photoView = findViewById(R.id.photoView);
        videoView = findViewById(R.id.videoView);
        btnClose = findViewById(R.id.btnClose);
        progressBar = findViewById(R.id.progressBar);

        btnClose.setOnClickListener(v -> finish());
    }

    private void loadMedia() {
        String mediaUrl = getIntent().getStringExtra(EXTRA_MEDIA_URL);
        boolean isVideo = getIntent().getBooleanExtra(EXTRA_IS_VIDEO, false);

        if (mediaUrl == null || mediaUrl.isEmpty()) {
            finish();
            return;
        }

        if (isVideo) {
            showVideo(mediaUrl);
        } else {
            showImage(mediaUrl);
        }
    }

    private void showImage(String url) {
        photoView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    @Override
                    public void onResourceReady(android.graphics.drawable.Drawable resource, 
                            com.bumptech.glide.request.transition.Transition<? super android.graphics.drawable.Drawable> transition) {
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(android.graphics.drawable.Drawable placeholder) {
                        progressBar.setVisibility(View.GONE);
                    }
                    
                    @Override
                    public void onLoadFailed(android.graphics.drawable.Drawable errorDrawable) {
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageResource(R.drawable.hue);
                    }
                });
    }

    private void showVideo(String url) {
        photoView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoURI(Uri.parse(url));
        
        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            videoView.start();
        });
        
        videoView.setOnErrorListener((mp, what, extra) -> {
            progressBar.setVisibility(View.GONE);
            return false;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
