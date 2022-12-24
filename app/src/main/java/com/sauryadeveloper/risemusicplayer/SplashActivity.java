package com.sauryadeveloper.risemusicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sauryadeveloper.risemusicplayer.helper.Helper;
import com.sauryadeveloper.risemusicplayer.helper.SongHelper;

public class SplashActivity extends AppCompatActivity {


    boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);
        Helper.startRotate(logo,SplashActivity.this);

        if (isPermission()) {
            loadMusic();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }

    }

    private boolean isPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }else {
            return true;
        }
    }

    private void loadMusic() {
        AsyncTask.execute(() -> SongHelper.getAllSongs(SplashActivity.this));
        check();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRunning = true;
        AsyncTask.execute(() -> SongHelper.getAllSongs(SplashActivity.this));
        check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    private void check() {
        if (isRunning) {
            new Handler().postDelayed(() -> {
                if (SongHelper.isLoaded) {
                    isRunning = false;
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    check();
                }
            }, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isRunning = true;
            loadMusic();
        }
    }
}