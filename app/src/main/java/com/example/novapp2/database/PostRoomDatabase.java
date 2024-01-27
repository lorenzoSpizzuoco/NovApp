package com.example.novapp2.database;

import static com.example.novapp2.utils.Constants.POST_DATABASE_NAME;
import static com.example.novapp2.utils.Constants.POST_DATABASE_VERSION;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.novapp2.R;
import com.example.novapp2.ui.post.Post;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Post.class}, version = POST_DATABASE_VERSION)
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
                            PostRoomDatabase.class, POST_DATABASE_NAME).addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration().build();
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

                postDao.insert(new Post("innaugurazione anno accademico", "Rettore Bicocca", R.drawable.analisi, "Innaugurazionea anno accademico", 2, "today", "Milano", 0));
                postDao.insert(new Post("something crazy2", "spiz404", R.drawable.analisi, "earn 1M a month", 3, "today", "Milano", 0));
                postDao.insert(new Post("something crazy", "antonello venditti", R.drawable.a, "earn 1M a month", 3, "today", "Milano", 0));
                postDao.insert(new Post("Hackaton", "mario giordano", R.drawable.a, "ai hackaton!", 1, "13/02/2024", "Milano", 0));
                postDao.insert(new Post("Riapertura bar u3", "Staff Bicocca", R.drawable.analisi, "riapertura bar u3 alle 11", 2, "22/01/2024", "Milano", 0));
                postDao.insert(new Post("Gruppo studio", "Lorenzo", R.drawable.analisi, "Gruppo studio analisi 2", 4, "today", "Milano", 0));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", 4, "today", "Milano", 0));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.a, "earn 1M a month", 4, "today", "Milano", 0));
                postDao.insert(new Post("something crazy", "Lorenzo", R.drawable.analisi, "earn 1M a month", 4, "today", "Milano", 0));


            });
        }
    };
}