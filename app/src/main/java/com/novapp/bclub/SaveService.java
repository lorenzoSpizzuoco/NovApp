package com.novapp.bclub;

import static com.novapp.bclub.utils.Constants.DB_SAVEDPOSTS;
import static com.novapp.bclub.utils.Constants.POST_DATABASE_NAME;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.novapp.bclub.database.PostRoomDatabase;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.service.nativeapi.UserService;

import java.util.List;

public class SaveService extends LifecycleService {

    public class MyService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // Avvia un nuovo thread per il salvataggio dei dati in remoto
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserService userService = new UserService();
                    // Ottieni un'istanza del tuo database Room
                    PostRoomDatabase db = Room.databaseBuilder(getApplicationContext(),
                            PostRoomDatabase.class, POST_DATABASE_NAME).build();

                    // Ottieni tutti i dati dal database
                    List<Post> dataList = db.postDao().getAllPosts();

                    // Ottieni un'istanza del tuo database Firebase
                    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
                    String user = userService.getCurrentUser().getID();
                    DatabaseReference myRef = firebaseDb.getReference("users").child(user).child(DB_SAVEDPOSTS);

                    // Carica tutti i dati su Firebase
                    for (Post data : dataList) {
                        myRef.child(data.getDbId()).setValue(data.getCategory());
                    }

                    // Arresta il servizio una volta terminato il caricamento dei dati
                    stopSelf();
                }
            }).start();

            return START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}
