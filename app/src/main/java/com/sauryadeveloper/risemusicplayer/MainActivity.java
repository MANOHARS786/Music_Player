package com.sauryadeveloper.risemusicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.sauryadeveloper.risemusicplayer.activity.MusicPlayerActivity;
import com.sauryadeveloper.risemusicplayer.adapter.FavouriteAdapter;
import com.sauryadeveloper.risemusicplayer.adapter.SongsAdapter;
import com.sauryadeveloper.risemusicplayer.ads.Admob;
import com.sauryadeveloper.risemusicplayer.helper.Keys;
import com.sauryadeveloper.risemusicplayer.helper.SongHelper;
import com.sauryadeveloper.risemusicplayer.model.FavouriteModel;
import com.sauryadeveloper.risemusicplayer.model.SongModel;
import com.sauryadeveloper.risemusicplayer.room.FavoriteFire;
import com.sauryadeveloper.risemusicplayer.room.FavouriteQuery;
import com.sauryadeveloper.risemusicplayer.room.RoomFavouriteModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    List<SongModel> songs = new ArrayList<>();
    List<FavouriteModel> favourite = new ArrayList<>();
    SongsAdapter songsAdapter;
    FavouriteAdapter favouriteAdapter;

    String loadType = "songs";

    public static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        recyclerView = findViewById(R.id.recycler_songs);

        loadSongs();

        sideBar();


        LinearLayout banner = findViewById(R.id.banner_main);

        Admob.setBanner(banner, MainActivity.this);

        loadAds();

    }

    private void loadAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainActivity.this, getString(R.string.interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private void loadSongs() {

        loadType = "songs";

        songsAdapter = new SongsAdapter(songs, songsDetails -> {

            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            intent.putExtra("title", songsDetails.getTitle());
            intent.putExtra("path", songsDetails.getPath());
            intent.putExtra("album", songsDetails.getAlbum());
            intent.putExtra("artist", songsDetails.getArt());
            intent.putExtra("duration", songsDetails.getDuration());
            intent.putExtra("position", songsDetails.getPos());
            startActivity(intent);


        });

        recyclerView.setAdapter(songsAdapter);

        songs.clear();
        songsAdapter.notifyDataSetChanged();

        SongHelper.listNew.clear();

        songs.addAll(SongHelper.list);

        for (int i = 0; i < SongHelper.list.size(); i++) {

            String title = songs.get(i).getTitle();
            String path = songs.get(i).getPath();
            String album = songs.get(i).getAlbum();
            String art = songs.get(i).getArt();
            String duration = songs.get(i).getDuration();

            SongHelper.listNew.add(new SongModel(i, title, path, album, art, duration));
        }

        songsAdapter.notifyDataSetChanged();
    }

    private void loadFavourite() {

        loadType = "fav";


        favouriteAdapter = new FavouriteAdapter(favourite, songsDetails -> {

            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            intent.putExtra("title", songsDetails.getTitle());
            intent.putExtra("path", songsDetails.getPath());
            intent.putExtra("album", songsDetails.getAlbum());
            intent.putExtra("artist", songsDetails.getArt());
            intent.putExtra("duration", songsDetails.getDuration());
            intent.putExtra("position", songsDetails.getPos());
            startActivity(intent);

        });

        recyclerView.setAdapter(favouriteAdapter);

        favourite.clear();
        favouriteAdapter.notifyDataSetChanged();

        List<RoomFavouriteModel> list2;
        FavoriteFire db = Room.databaseBuilder(MainActivity.this,
                FavoriteFire.class, Keys.favouriteDB).allowMainThreadQueries().build();
        FavouriteQuery userDao = db.roomQuery();
        list2 = userDao.allCarts();

        SongHelper.listNew.clear();

        for (int i = 0; i < list2.size(); i++) {

            String title = list2.get(i).getTitle();
            String path = list2.get(i).getPath();
            String album = list2.get(i).getAlbum();
            String artist = list2.get(i).getArt();
            String duration = list2.get(i).getDuration();

            favourite.add(new FavouriteModel(i, title, path, album, artist, duration));
            SongHelper.listNew.add(new SongModel(i, title, path, album, artist, duration));

            favouriteAdapter.notifyDataSetChanged();
        }
    }

    private void sideBar() {

        nav = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        nav.setItemIconTintList(null);

        nav.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.nav_all:

                    if (loadType.contains("fav")) {
                        loadSongs();
                    }
                    break;

                case R.id.nav_fav:
                    if (loadType.contains("songs")) {
                        loadFavourite();
                    }

                    break;

                case R.id.nav_share:

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey buddy! Download *" +
                            getString(R.string.app_name) + "App* .. :\n\n https://play.google.com/store/apps/details?id=" +
                            getPackageName());
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    startActivity(Intent.createChooser(sharingIntent, "ChikuAI Code Dev. Team"));

                    break;


                case R.id.nav_rate:

                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                    break;

                case R.id.nav_policy:

                    break;

            }

            if (drawerLayout.isDrawerOpen(nav)) {
                drawerLayout.closeDrawer(nav);

            } else {
                drawerLayout.openDrawer(nav);
            }
            return true;
        });

        ImageView navBtn = findViewById(R.id.openNav);
        navBtn.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(nav)) {
                drawerLayout.closeDrawer(nav);
            } else {
                drawerLayout.openDrawer(nav);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(nav)) {
            drawerLayout.closeDrawer(nav);
        } else {
            if (loadType.contains("fav")) {
                loadSongs();
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    loadAds();
                }
            });
        }
    }
}