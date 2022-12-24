package com.sauryadeveloper.risemusicplayer.model;

public class FavouriteModel {

    int pos;
    String title,path, album,art,duration;


    public FavouriteModel(int pos, String title, String path, String album, String art, String duration) {
        this.pos = pos;
        this.title = title;
        this.path = path;
        this.album = album;
        this.art = art;
        this.duration = duration;
    }

    public int getPos() {
        return pos;
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
