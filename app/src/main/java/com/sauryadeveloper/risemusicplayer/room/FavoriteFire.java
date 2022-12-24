package com.sauryadeveloper.risemusicplayer.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomFavouriteModel.class}, version = 1)
public abstract class FavoriteFire extends RoomDatabase {
    public abstract FavouriteQuery roomQuery();

}

