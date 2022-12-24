package com.sauryadeveloper.risemusicplayer.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomFavouriteModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "album")
    public String album;

    @ColumnInfo(name = "art")
    public String art;

    @ColumnInfo(name = "duration")
    public String duration;

    public RoomFavouriteModel(String title, String path, String album, String art, String duration) {
        this.title = title;
        this.path = path;
        this.album = album;
        this.art = art;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getArt() {
        return art;
    }

    public String getDuration() {
        return duration;
    }
}
