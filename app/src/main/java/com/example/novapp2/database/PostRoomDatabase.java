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

                postDao.insert(new Post("Conferenza sulla Fisica Quantistica", "Prof. Rossi", R.drawable.ask_customer_feedback , "Discussione sulla teoria quantistica", 2, "Oggi", "Aula Magna"));
                postDao.insert(new Post("Campionato di Calcetto", "Sport Bicocca", R.drawable.matematica, "Partecipa al torneo di calcetto universitario", 3, "14/02/2024", "Campo Sportivo"));
                postDao.insert(new Post("Avviso Importante - Iscrizioni Esami", "Segreteria Studenti", R.drawable.testing, "Scadenza iscrizioni agli esami del prossimo semestre", 1, "21/01/2024", "Online"));
                postDao.insert(new Post("Presentazione Tirocinio Internazionale", "Career Service", R.drawable.sign_up_form, "Opportunità di tirocinio all'estero", 2, "10/02/2024", "Aula 103"));
                postDao.insert(new Post("Incontro con l'Autore", "Biblioteca Universitaria", R.drawable.analisi, "Discussione sui nuovi libri in biblioteca", 2, "01/02/2024", "Biblioteca"));
                postDao.insert(new Post("Lezioni di Yoga Gratuite", "Associazione Studenti", R.drawable.ask_customer_feedback, "Rilassati con le lezioni di yoga offerte gratuitamente", 4, "Ogni Mercoledì", "Palestra Universitaria"));
                postDao.insert(new Post("Workshop di Programmazione", "Dipartimento di Informatica", R.drawable.website_development, "Apprendi nuove tecniche di programmazione", 3, "05/02/2024", "Laboratorio Informatico"));

            });
        }
    };
}