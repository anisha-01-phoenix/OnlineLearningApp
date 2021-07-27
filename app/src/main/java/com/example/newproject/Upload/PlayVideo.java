package com.example.newproject.Upload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newproject.R;
import com.example.newproject.TrackSelectionDialog;
import com.example.newproject.databinding.ActivityPlayVideoBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class PlayVideo extends AppCompatActivity {

    ActivityPlayVideoBinding activityPlayVideoBinding;
    private DefaultTrackSelector trackSelector;
    private SimpleExoPlayer simpleExoPlayer;
    String[] speed={"0.25x","0.5x","Normal","1.5x","2x"};
    private MediaItem mediaItem;
    String url;
    boolean trackDialog;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayVideoBinding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(activityPlayVideoBinding.getRoot());
        url = getIntent().getStringExtra("VideoUrl");
        uri=Uri.parse(url);
        trackSelector=new DefaultTrackSelector(this);
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        activityPlayVideoBinding.exoplayerItem.setPlayer(simpleExoPlayer);
        mediaItem = MediaItem.fromUri(uri);
        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        ImageView farwordBtn = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.fwd);
        ImageView rewBtn = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.rew);
        ImageView setting = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.exo_track_selection_view);
        ImageView speedBtn = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.exo_playback_speed);
        TextView speedTxt = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.speed);



       speedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(PlayVideo.this);
                builder.setTitle("Set Speed");
                builder.setItems(speed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){

                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("0.25X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            simpleExoPlayer.setPlaybackParameters(param);


                        }  if (which==1){

                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("0.5X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            simpleExoPlayer.setPlaybackParameters(param);


                        }
                        if (which==2){

                            speedTxt.setVisibility(View.GONE);
                            PlaybackParameters param = new PlaybackParameters(1f);
                            simpleExoPlayer.setPlaybackParameters(param);


                        }
                        if (which==3){
                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("1.5X");
                            PlaybackParameters param = new PlaybackParameters(1.5f);
                            simpleExoPlayer.setPlaybackParameters(param);

                        }
                        if (which==4){
                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("2X");
                            PlaybackParameters param = new PlaybackParameters(2f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }

                    }
                });
                builder.show();
            }
        });


        farwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);

            }
        });
        rewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long num = simpleExoPlayer.getCurrentPosition() - 10000;
                if (num < 0) {

                    simpleExoPlayer.seekTo(0);


                } else {

                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);

                }


            }
        });

        ImageView fullscreenButton = activityPlayVideoBinding.exoplayerItem.getRootView().findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View view) {


                int orientation = PlayVideo.this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                } else {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }


            }
        });


        findViewById(R.id.exo_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.play();

            }
        });
        findViewById(R.id.exo_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.pause();

            }
        });


        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_ENDED) {

                }

            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trackDialog
                        && TrackSelectionDialog.willHaveContent(trackSelector)) {
                    trackDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    dismissedDialog -> trackDialog = false);
                    trackSelectionDialog.show(getSupportFragmentManager(),null);


                }


            }
        });
    }

    protected void releasePlayer()
    {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            trackSelector = null;
        }

    }


    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();
    }

}