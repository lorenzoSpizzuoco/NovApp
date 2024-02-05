package com.novapp.bclub.database;



import static com.novapp.bclub.utils.Constants.POST_DATABASE_NAME;
import static com.novapp.bclub.utils.Constants.POST_DATABASE_VERSION;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.novapp.bclub.database.PostDao;
import com.novapp.bclub.entity.post.Post;

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

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {

                PostDao postDao = INSTANCE.postDao();

                postDao.deleteAll();

            });
        }
    };
}