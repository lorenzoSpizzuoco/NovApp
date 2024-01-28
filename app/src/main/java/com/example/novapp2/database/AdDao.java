package com.example.novapp2.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.novapp2.ui.ad.Ad;

import java.util.List;

@Dao
public interface AdDao {

    @Query("SELECT * FROM ad_table ORDER BY date DESC")
    LiveData<List<Ad>> getAllAds();

    @Query("SELECT * FROM ad_table WHERE id = :id")
    Ad getAd(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ad ad);

    // just for testing

    @Query("DELETE FROM ad_table")
    void deleteAll();
}