package com.example.novapp2.database;

import static com.example.novapp2.utils.Constants.AD_DATABASE_NAME;
import static com.example.novapp2.utils.Constants.DATABASE_VERSION;
import static com.example.novapp2.utils.Constants.POST_DATABASE_NAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.novapp2.R;
import com.example.novapp2.ui.post.Post;
import com.example.novapp2.utils.Constants.*;

import com.example.novapp2.ui.ad.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Post.class}, version = DATABASE_VERSION)
public abstract class PostRoomDatabase extends RoomDatabase {



    public abstract PostDao postDao();

    private static volatile PostRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PostRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PostRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostRoomDatabase.class, POST_DATABASE_NAME).addCallback(sRoomDatabaseCallback).build();
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
                PostDao postDao = INSTANCE.postDao();

                postDao.deleteAll();

                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy2", "spiz404", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy", "antonello venditti", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("new song", "massimo troisi", R.drawable.analisi, "just dropped a hit", "event", "today"));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", "job offer", "today"));
            });
        }
    };
}