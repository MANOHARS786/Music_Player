package com.sauryadeveloper.risemusicplayer.activity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.sauryadeveloper.risemusicplayer.R;
import com.sauryadeveloper.risemusicplayer.helper.Helper;
import com.sauryadeveloper.risemusicplayer.helper.Keys;
import com.sauryadeveloper.risemusicplayer.helper.SongHelper;
import com.sauryadeveloper.risemusicplayer.model.SongModel;
import com.sauryadeveloper.risemusicplayer.room.FavoriteFire;
import com.sauryadeveloper.risemusicplayer.room.FavouriteQuery;
import com.sauryadeveloper.risemusicplayer.room.RoomFavouriteModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    ImageView icon, fav, next, pre, play, back;
    TextView txtTitle, txtAlbum, txtPlayed, txtDuration;
    SeekBar seekBar;

    int pos;
    String title, path, album, art, duration;

    //player
    private MediaPlayer mPlayer = null;

    List<SongModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getViews();

        Bundle bundle = getIntent().getExtras();

        title = bundle.getString("title");
        path = bundle.getString("path");
        album = bundle.getString("album");
        art = bundle.getString("art");
        duration = bundle.getString("duration");
        pos = bundle.getInt("position");


        initPlayer();
        allSongs();


    }

//  ===========All Songs=======

    private void allSongs() {

        list.addAll(SongHelper.listNew);

        Log.d("posssssss", "allSongs: " + pos);


        next.setOnClickListener(v -> {
            int cPos = pos;
            if (cPos + 1 < list.size()) {
                pos += 1;
                getMusicData();
            } else {
                Toast.makeText(this, "No Next Songs", Toast.LENGTH_SHORT).show();
            }
        });
        pre.setOnClickListener(v -> {
            int cPos = pos;
            if (cPos - 1 >= 0) {
                pos -= 1;
                getMusicData();
            } else {
                Toast.makeText(this, "No Previous Songs", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getMusicData() {


        SongModel model = list.get(pos);

        title = model.getTitle();
        path = model.getPath();
        album = model.getAlbum();
        art = model.getArt();
        duration = model.getDuration();
        pos = model.getPos();


        mPlayer.stop();
        initPlayer();
    }


//    ===========Player==========

    private void initPlayer() {

        txtTitle.setText(title);
        txtAlbum.setText(album);
        txtDuration.setText(duration);

        Glide.with(MusicPlayerActivity.this)
                .load(SongHelper.getAlbumImage(path))
                .placeholder(R.drawable.ic_disc)
                .error(R.drawable.ic_disc)
                .transition(new DrawableTransitionOptions())
                .into(icon);
        Helper.startRotate(icon, MusicPlayerActivity.this);

        //Player

        play.setOnClickListener(v -> {
            startPlaying();
        });

        mPlayer = new MediaPlayer();
        mPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mPlayer.setOnPreparedListener(mediaPlayer -> {
            play.setImageResource(R.drawable.ic_pause);
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            updateSeekbar();
        });

        mPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            double ratio = percent / 100.0;
            int bufferingLevel = (int) (mp.getDuration() * ratio);
            seekBar.setSecondaryProgress(bufferingLevel);
        });

        mPlayer.setOnCompletionListener(mediaPlayer -> {
            play.setImageResource(R.drawable.ic_play);
            mPlayer.seekTo(0);
            updateSeekbar();
        });


        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        checkFavourite();

    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (!mPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.ic_pause);
                    mPlayer.start();
                }
                mPlayer.seekTo(progress);
                seekBar.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private void updateSeekbar() {
        try {
            if (mPlayer != null) {

                int currentPos = mPlayer.getCurrentPosition();
                seekBar.setProgress(currentPos);

                String seconds = String.valueOf((currentPos % 60000) / 1000);
                String minutes = String.valueOf(currentPos / 60000);

                if (minutes.length() == 1) {
                    minutes = "0" + minutes;
                }
                if (seconds.length() == 1) {
                    seconds = ":0" + seconds;
                } else {
                    seconds = ":" + seconds;
                }

                String time = minutes + seconds;

                txtPlayed.setText(time);

                new Handler().postDelayed(this::updateSeekbar, 1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlaying() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            play.setImageResource(R.drawable.ic_play);
        } else if (mPlayer != null) {
            mPlayer.start();
            play.setImageResource(R.drawable.ic_pause);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


//    ========Views======

    private void getViews() {

        icon = findViewById(R.id.player_icon);
        fav = findViewById(R.id.player_fav);
        next = findViewById(R.id.player_next);
        pre = findViewById(R.id.player_pre);
        play = findViewById(R.id.player_play);
        back = findViewById(R.id.player_back);
        txtTitle = findViewById(R.id.player_title);
        txtAlbum = findViewById(R.id.player_album);
        txtPlayed = findViewById(R.id.player_duration_played);
        txtDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.player_seekbar);


        txtTitle.setSelected(true);


        back.setOnClickListener(v -> {
            finish();
        });
    }

//    ==========DB============


    private void checkFavourite() {

        FavoriteFire db = Room.databaseBuilder(getApplicationContext(),
                FavoriteFire.class, Keys.favouriteDB).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.roomQuery();

        if (userDao.is_exist(title)) {
            fav.setImageResource(R.drawable.ic_favorite);
        } else {
            fav.setImageResource(R.drawable.ic_favorite_empty);
        }

        fav.setOnClickListener(v -> {
            if (userDao.is_exist(title)) {
                delete();
            } else {
                new AddFavourite(title, path, album, art, duration).start();
            }
        });

    }

    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this);
        builder.setTitle("Remove")
                .setIcon(R.drawable.ic_favorite)
                .setMessage("Are you sure want to Remove from Favourite List..?")
                .setPositiveButton("Remove", (dialogInterface, i) -> {

                    FavoriteFire db = Room.databaseBuilder(MusicPlayerActivity.this,
                            FavoriteFire.class, Keys.favouriteDB).allowMainThreadQueries().build();
                    FavouriteQuery userDao = db.roomQuery();
                    try {
                        userDao.deleteByTitle(title);
                        fav.setImageResource(R.drawable.ic_favorite_empty);
                        Toast.makeText(MusicPlayerActivity.this, "Removed..", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(MusicPlayerActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    public class AddFavourite extends Thread {

        FavoriteFire db = Room.databaseBuilder(MusicPlayerActivity.this,
                FavoriteFire.class, Keys.favouriteDB).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.roomQuery();

        String title, path, album, art, duration;


        public AddFavourite(String title, String path, String album, String art, String duration) {
            this.title = title;
            this.path = path;
            this.album = album;
            this.art = art;
            this.duration = duration;
        }

        @Override
        public void run() {
            super.run();

            try {
                if (userDao.is_exist(title)) {
                    runOnUiThread(() -> Toast.makeText(MusicPlayerActivity.this, "Already Added", Toast.LENGTH_SHORT).show());
                } else {

                    userDao.insertData(new RoomFavouriteModel(title, path, album, art, duration));

                    runOnUiThread(() -> {
                        fav.setImageResource(R.drawable.ic_favorite);
                        Toast.makeText(MusicPlayerActivity.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                    });

                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MusicPlayerActivity.this, "Error to add Favourite: ", Toast.LENGTH_SHORT).show());
            }
        }
    }

}