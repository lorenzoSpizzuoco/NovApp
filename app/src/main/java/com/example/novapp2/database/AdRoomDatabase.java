package com.example.novapp2.database;

import static com.example.novapp2.utils.Constants.AD_DATABASE_NAME;
import static com.example.novapp2.utils.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.novapp2.R;

import com.example.novapp2.entity.ad.Ad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Ad.class}, version = DATABASE_VERSION)
public abstract class AdRoomDatabase extends RoomDatabase {

    public abstract AdDao adDao();

    private static volatile AdRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AdRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AdRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AdRoomDatabase.class, AD_DATABASE_NAME).addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                AdDao adDao = INSTANCE.adDao();

                adDao.deleteAll();

                adDao.insert(new Ad("Lorenzo", "offerta assurda", "offerta incredibile", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
                adDao.insert(new Ad("sup", "boh", "ho perso lo zaino", R.drawable.analisi, "oggi"));
            });
        }
    };
}