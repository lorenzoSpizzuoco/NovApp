package com.example.novapp2.repository.post;

import static com.example.novapp2.utils.Constants.DB_EVENTS;
import static com.example.novapp2.utils.Constants.DB_GS;
import static com.example.novapp2.utils.Constants.DB_INFOS;
import static com.example.novapp2.utils.Constants.DB_POSTS;
import static com.example.novapp2.utils.Constants.DB_RIPET;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.novapp2.entity.post.GenericPost;
import com.example.novapp2.entity.post.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.List;

public class PostRepository implements IPostRepository{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    public Task<Void> insert(Post post, Uri image) {
        String id = mDatabase.child(DB_POSTS).push().getKey();
        StorageReference storageRef = mStorage.getReference();
        int category = post.getCategory();

        if(category == 1 || category == 3) {
            // postImage(String child, String fileName) filename -> nome.ext

            StorageReference imRef = storageRef.child("postImages").child(id + image.getLastPathSegment());

            Task uptask = imRef.putFile(image);
            uptask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        post.setPostImage(downloadUri.toString());

                        Date date = new Date();
                        long time = date.getTime();
                        mDatabase.child(DB_POSTS).child(id).setValue(new GenericPost(id, time, post.getCategory()));

                        switch (post.getCategory()) {
                            case 1:
                                mDatabase.child(DB_EVENTS).child(id).setValue(post);
                                break;
                            case 2:
                                mDatabase.child(DB_INFOS).child(id).setValue(post);
                                break;
                            case 3:
                                mDatabase.child(DB_RIPET).child(id).setValue(post);
                                break;
                            case 4:
                                mDatabase.child(DB_GS).child(id).setValue(post);
                                break;
                        }
                    } else {
                        Log.d(TAG, "fail");

                    }
                }
            });

        }
        else {
            Date date = new Date();
            long time = date.getTime();
            post.setPostImage(null);
            mDatabase.child(DB_POSTS).child(id).setValue(new GenericPost(id, time, post.getCategory()));

            switch (post.getCategory()) {
                case 1:
                    mDatabase.child(DB_EVENTS).child(id).setValue(post);
                    break;
                case 2:
                    mDatabase.child(DB_INFOS).child(id).setValue(post);
                    break;
                case 3:
                    mDatabase.child(DB_RIPET).child(id).setValue(post);
                    break;
                case 4:
                    mDatabase.child(DB_GS).child(id).setValue(post);
                    break;
            }
        }
    }

    @Override
    public Task<List<Post>> getAllPost() {
        return null;
    }
}
