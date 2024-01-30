package com.example.novapp2.entity.post;

import static com.example.novapp2.utils.Constants.API_KEY;
import static com.example.novapp2.utils.Constants.PROFANITY_API_BASE_URL;
import static com.example.novapp2.utils.Utils.checkResponse;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.example.novapp2.repository.post.PostRepository2;
import com.example.novapp2.service.PostService;
import com.example.novapp2.service.ProfanityApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = PostViewModel.class.getSimpleName();

    //private PostRepository2 postRepository;
    private static PostService postService = new PostService();
    private long lastelement;

    private MutableLiveData<Integer> isFavorite = new MutableLiveData<>();
    private MutableLiveData<Boolean> doneLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> allPost = null;

    public PostViewModel (Application application) {
        super(application);

        isFavorite.setValue(0);
        lastelement = 0;
    }

    public MutableLiveData<Integer> getIsFavorite() {
        return isFavorite;
    }

    public  void setFavorite(long id, int fav) {
        isFavorite.setValue(fav);
        //postRepository.setFavorite(id, fav);
    }

    public LiveData<Boolean> getDoneLoading() {
        return doneLoading;
    }

    public MutableLiveData<List<Post>> getAllPost() {

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        postService.getAllPost().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                posts.postValue(task.getResult());
            }
        });

        return posts;
    }



    public void insert(Post post, Uri image) {
        postService.insert(post, image);
        doneLoading.setValue(true);
    }
}