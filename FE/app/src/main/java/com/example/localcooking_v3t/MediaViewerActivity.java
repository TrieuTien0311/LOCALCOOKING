package com.example.localcooking_v3t;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.HinhAnhDanhGiaDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity xem ảnh/video đánh giá fullscreen với ExoPlayer và PiP
 */
public class MediaViewerActivity extends AppCompatActivity {

    public static final String EXTRA_MEDIA_URL = "media_url";
    public static final String EXTRA_IS_VIDEO = "is_video";
    public static final String EXTRA_MEDIA_LIST = "mediaList";
    public static final String EXTRA_POSITION = "position";

    private ViewPager2 viewPager;
    private ImageView btnClose;
    private TextView txtCounter;
    private View headerLayout;
    
    private List<HinhAnhDanhGiaDTO> mediaList;
    private int currentPosition;
    
    private String singleMediaUrl;
    private boolean isSingleVideo;
    private boolean isSingleMode = false;
    

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private boolean isInPipMode = false;
    private boolean shouldKeepPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_viewer);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        initViews();
        getIntentData();
        setupViewPager();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        btnClose = findViewById(R.id.btnClose);
        txtCounter = findViewById(R.id.txtCounter);
        headerLayout = findViewById(R.id.headerLayout);
        btnClose.setOnClickListener(v -> finish());
    }

    private void getIntentData() {
        singleMediaUrl = getIntent().getStringExtra(EXTRA_MEDIA_URL);
        isSingleVideo = getIntent().getBooleanExtra(EXTRA_IS_VIDEO, false);
        
        if (singleMediaUrl != null && !singleMediaUrl.isEmpty()) {
            isSingleMode = true;
            mediaList = new ArrayList<>();
        } else {
            mediaList = (ArrayList<HinhAnhDanhGiaDTO>) getIntent().getSerializableExtra(EXTRA_MEDIA_LIST);
            if (mediaList == null) {
                mediaList = (ArrayList<HinhAnhDanhGiaDTO>) getIntent().getSerializableExtra("mediaList");
            }
            currentPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
            if (currentPosition == 0) {
                currentPosition = getIntent().getIntExtra("position", 0);
            }
            if (mediaList == null) {
                mediaList = new ArrayList<>();
            }
        }
    }

    private void setupViewPager() {
        if (isSingleMode) {
            viewPager.setVisibility(View.GONE);
            txtCounter.setVisibility(View.GONE);
            
            View singleView = getLayoutInflater().inflate(R.layout.item_media_viewer, null);
            ImageView imageView = singleView.findViewById(R.id.imageView);
            playerView = singleView.findViewById(R.id.playerView);
            ProgressBar progressLoading = singleView.findViewById(R.id.progressLoading);
            
            ((android.widget.FrameLayout) findViewById(R.id.viewPager).getParent()).addView(singleView);
            
            String fullUrl = RetrofitClient.getFullImageUrl(singleMediaUrl);
            
            if (isSingleVideo) {
                imageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                progressLoading.setVisibility(View.VISIBLE);
                
                initExoPlayer(fullUrl, progressLoading);
            } else {
                imageView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                
                Glide.with(this)
                        .load(fullUrl)
                        .placeholder(R.drawable.hue)
                        .error(R.drawable.hue)
                        .into(imageView);
            }
        } else {
            if (mediaList.isEmpty()) {
                finish();
                return;
            }
            
            MediaPagerAdapter adapter = new MediaPagerAdapter(mediaList);
            pagerAdapter = adapter; // Lưu reference
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(currentPosition, false);
            updateCounter(currentPosition);

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    updateCounter(position);
                    // Pause tất cả video khi swipe
                    if (pagerAdapter != null) {
                        pagerAdapter.pauseAllPlayers();
                    }
                }
            });
        }
    }

    private void initExoPlayer(String videoUrl, ProgressBar progressLoading) {
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        exoPlayer.setMediaItem(mediaItem);
        
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    progressLoading.setVisibility(View.GONE);
                    updatePipParams(); // Enable auto PiP when video ready
                } else if (state == Player.STATE_BUFFERING) {
                    progressLoading.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                updatePipParams(); // Update PiP state when play/pause
            }
        });
        
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    private void updateCounter(int position) {
        txtCounter.setText((position + 1) + "/" + mediaList.size());
    }

    // ========== PiP Support ==========
    
    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        // Enter PiP when user presses home while video is playing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check single mode
            if (isSingleVideo && exoPlayer != null && exoPlayer.isPlaying()) {
                enterPipMode();
                return;
            }
            // Check list mode - nếu đang xem video trong list
            if (!isSingleMode && !mediaList.isEmpty()) {
                int currentPos = viewPager.getCurrentItem();
                if (currentPos < mediaList.size() && mediaList.get(currentPos).isVideo()) {
                    enterPipMode();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Nếu đang xem video, vào PiP thay vì đóng
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isSingleVideo && exoPlayer != null && exoPlayer.isPlaying()) {
                enterPipMode();
                return;
            }
        }
        super.onBackPressed();
    }

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Rational aspectRatio = new Rational(16, 9);
                PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder()
                        .setAspectRatio(aspectRatio);
                
                // Auto enter PiP khi minimize (Android 12+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    builder.setAutoEnterEnabled(true);
                    builder.setSeamlessResizeEnabled(true);
                }
                
                enterPictureInPictureMode(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Thiết lập auto PiP cho Android 12+
    private void updatePipParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                boolean isVideoPlaying = false;

                // Check Single Mode
                if (isSingleVideo && exoPlayer != null && exoPlayer.isPlaying()) {
                    isVideoPlaying = true;
                }

                // Check List Mode
                if (!isSingleMode && !mediaList.isEmpty()) {
                    int currentPos = viewPager.getCurrentItem();
                    if (currentPos < mediaList.size() && mediaList.get(currentPos).isVideo()) {
                        isVideoPlaying = true;
                    }
                }

                PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder()
                        .setAspectRatio(new Rational(16, 9))
                        .setAutoEnterEnabled(isVideoPlaying)
                        .setSeamlessResizeEnabled(true);

                setPictureInPictureParams(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, @NonNull Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        
        if (isInPictureInPictureMode) {
            // Entering PiP mode
            isInPipMode = true;
            shouldKeepPlaying = false;
            if (headerLayout != null) {
                headerLayout.setVisibility(View.GONE);
            }
            if (playerView != null) {
                playerView.setUseController(false);
            }
        } else {
            // Exiting PiP mode
            isInPipMode = false;
            
            // Đánh dấu là đang expand (không phải đóng bằng nút X)
            // Nếu activity vẫn còn visible thì là expand
            shouldKeepPlaying = !isFinishing();
            
            // Hiện lại controls
            if (headerLayout != null) {
                headerLayout.setVisibility(View.VISIBLE);
            }
            if (playerView != null) {
                playerView.setUseController(true);
            }
        }
    }
    
    private void stopAndReleasePlayer() {
        // Release single mode player
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        // Release all players in list mode
        if (pagerAdapter != null) {
            pagerAdapter.releaseAllPlayers();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Chỉ dừng video nếu KHÔNG phải đang expand từ PiP
        if (!shouldKeepPlaying) {
            stopAndReleasePlayer();
        }
        // Reset flag
        shouldKeepPlaying = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isInPipMode && exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (exoPlayer != null && !isInPipMode) {
            exoPlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAndReleasePlayer();
    }

    // ========== Adapter ==========
    
    private MediaPagerAdapter pagerAdapter; // Lưu reference để release players
    
    private class MediaPagerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<MediaPagerAdapter.MediaViewHolder> {
        private List<HinhAnhDanhGiaDTO> items;
        private List<ExoPlayer> activePlayers = new ArrayList<>(); // Track all active players

        MediaPagerAdapter(List<HinhAnhDanhGiaDTO> items) {
            this.items = items;
        }
        
        // Release tất cả players
        void releaseAllPlayers() {
            for (ExoPlayer player : activePlayers) {
                if (player != null) {
                    player.stop();
                    player.release();
                }
            }
            activePlayers.clear();
        }
        
        // Pause tất cả players
        void pauseAllPlayers() {
            for (ExoPlayer player : activePlayers) {
                if (player != null && player.isPlaying()) {
                    player.pause();
                }
            }
        }

        @Override
        public MediaViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_media_viewer, parent, false);
            return new MediaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MediaViewHolder holder, int position) {
            HinhAnhDanhGiaDTO media = items.get(position);
            String fullUrl = RetrofitClient.getFullImageUrl(media.getDuongDan());

            if (media.isVideo()) {
                holder.imageView.setVisibility(View.GONE);
                holder.playerView.setVisibility(View.VISIBLE);
                holder.progressLoading.setVisibility(View.VISIBLE);
                
                // Create ExoPlayer for this video
                ExoPlayer player = new ExoPlayer.Builder(MediaViewerActivity.this).build();
                holder.playerView.setPlayer(player);
                
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(fullUrl));
                player.setMediaItem(mediaItem);
                
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == Player.STATE_READY) {
                            holder.progressLoading.setVisibility(View.GONE);
                        } else if (state == Player.STATE_BUFFERING) {
                            holder.progressLoading.setVisibility(View.VISIBLE);
                        }
                    }
                });
                
                player.prepare();
                holder.player = player;
                activePlayers.add(player); // Track this player
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.playerView.setVisibility(View.GONE);
                holder.progressLoading.setVisibility(View.GONE);

                Glide.with(MediaViewerActivity.this)
                        .load(fullUrl)
                        .placeholder(R.drawable.hue)
                        .error(R.drawable.hue)
                        .into(holder.imageView);
            }
        }

        @Override
        public void onViewRecycled(@NonNull MediaViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder.player != null) {
                activePlayers.remove(holder.player); // Remove from tracking
                holder.player.stop();
                holder.player.release();
                holder.player = null;
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MediaViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            ImageView imageView;
            PlayerView playerView;
            ProgressBar progressLoading;
            ExoPlayer player;

            MediaViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                playerView = itemView.findViewById(R.id.playerView);
                progressLoading = itemView.findViewById(R.id.progressLoading);
            }
        }
    }
}
