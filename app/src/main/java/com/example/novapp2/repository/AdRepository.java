package com.example.novapp2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.novapp2.database.AdDao;
import com.example.novapp2.database.AdRoomDatabase;
import com.example.novapp2.ui.ad.Ad;

import java.util.List;

public class AdRepository {

    private String TAG = AdRepository.class.getSimpleName();
    private AdDao adDao;
    private LiveData<List<Ad>> allAd;

    public AdRepository(Application application) {
        //AdRoomDatabase db = AdRoomDatabase.getDatabase(application);
        //AdRoomDatabase.getDatabase(application).clearAllTables();
        AdRoomDatabase db = AdRoomDatabase.getDatabase(application);

        db = AdRoomDatabase.getDatabase(application);
        adDao = db.adDao();
        allAd = adDao.getAllAds();
    }

    public LiveData<List<Ad>> getAllAd() {
        return allAd;
    }

    public void insert(Ad ad) {
        AdRoomDatabase.databaseWriteExecutor.execute(() -> {
            adDao.insert(ad);
        });
    }



}