package com.novapp.bclub.entity.post;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.service.nativeapi.UserService;

public class PostDetailViewModel extends AndroidViewModel {

    public PostDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getAuthorImage(String email) {

        MutableLiveData<String> authorImage = new MutableLiveData<>();

        UserService.getUserByEmail(email).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        authorImage.postValue(task.getResult().getProfileImg());
                    }
                }
        );

        return authorImage;
    }

}
