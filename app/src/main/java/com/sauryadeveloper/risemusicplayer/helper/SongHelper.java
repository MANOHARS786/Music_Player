package com.sauryadeveloper.risemusicplayer.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.sauryadeveloper.risemusicplayer.model.SongModel;

import java.util.ArrayList;
import java.util.List;

public class SongHelper {

    public static List<SongModel> list = new ArrayList<>();
    public static List<SongModel> listNew = new ArrayList<>();

    public static boolean isLoaded = false;

    @SuppressLint("Range")
    public static void getAllSongs(Context context) {

        int pos = 0;
        List<SongModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        if (c != null) {

            while (c.moveToNext()) {

                String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = path.substring(path.lastIndexOf("/") + 1);

                String art = "", album = "";

                try {
                    album = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                } catch (Exception e) {
                    album = "Unknown";
                }
                try {
                    art = c.getString(c.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART));
                } catch (Exception e) {
                    art = "em";
                }

                String time = "";

                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(path);

                String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                long dur = Long.parseLong(duration);
                String seconds = String.valueOf((dur % 60000) / 1000);
                String minutes = String.valueOf(dur / 60000);

                if (minutes.length() == 1) {
                    minutes = "0" + minutes;
                }
                if (seconds.length() == 1) {
                    seconds = ":0" + seconds;
                } else {
                    seconds = ":" + seconds;
                }

                time = minutes + seconds;

                metaRetriever.release();

                SongModel songModel = new SongModel(pos, title, path, album, art, time);
                tempAudioList.add(songModel);

                pos += 1;

            }
            c.close();
        }

        list.clear();
        list.addAll(tempAudioList);
        isLoaded = true;
    }

    public static Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }
}
