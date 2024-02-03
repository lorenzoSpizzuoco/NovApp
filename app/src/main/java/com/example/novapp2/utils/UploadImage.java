package com.example.novapp2.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImage {

    private static StorageReference ref = FirebaseStorage.getInstance().getReference();
    private static final String TAG = UploadImage.class.getSimpleName();

    public static Task<Uri> uploadImage(Uri image, String child, String name) {

        StorageReference imRef = ref.child(child).child(name + image.getLastPathSegment());

        return imRef.putFile(image).continueWithTask(
                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Upload failed", task.getException());
                            throw task.getException();
                        }

                        Log.d(TAG, "returning");
                        // Continue with the task to get the download URL
                        return imRef.getDownloadUrl();
                    }
                });
    }

}
