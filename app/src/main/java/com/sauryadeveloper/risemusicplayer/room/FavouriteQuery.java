package com.sauryadeveloper.risemusicplayer.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteQuery {

    @Insert
    void insertData(RoomFavouriteModel quizDBModel);

    @Query("SELECT EXISTS(SELECT * FROM RoomFavouriteModel WHERE title=:title)")
    Boolean is_exist(String title);

    @Query("SELECT * FROM RoomFavouriteModel ORDER By id DESC")
    List<RoomFavouriteModel> allCarts();

    @Query("DELETE FROM RoomFavouriteModel WHERE title=:title")
    void deleteByTitle(String title);


}
