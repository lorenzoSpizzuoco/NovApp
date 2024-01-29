package com.example.novapp2.entity.post;

import static com.example.novapp2.utils.Constants.PROFANITY_API_BASE_URL;

import android.app.Application;
import android.util.Log;

import com.example.novapp2.entity.ad.AdViewModel;
import com.example.novapp2.repository.PostRepository;
import com.example.novapp2.service.ProfanityApiService;

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

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = AdViewModel.class.getSimpleName();
    private Retrofit retrofit;
    private PostRepository postRepository;

    private MutableLiveData<Integer> isFavorite = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final LiveData<List<Post>> allPost;


    public PostViewModel (Application application) {
        super(application);
        postRepository = new PostRepository(application);
        allPost = postRepository.getAllPost();
        isFavorite.setValue(0);
    }

    public MutableLiveData<Integer> getIsFavorite() {
        return isFavorite;
    }

    public  void setFavorite(long id, int fav) {
        isFavorite.setValue(fav);
        postRepository.setFavorite(id, fav);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Post>> getAllPost() {return allPost; }



    public void insert(Post post) {

        //isLoading.setValue(true);
        // check for profanity
        retrofit = new Retrofit.Builder()
                .baseUrl(PROFANITY_API_BASE_URL)
                .build();

        ProfanityApiService service = retrofit.create(ProfanityApiService.class);
        service.checkForPronfanity(post.getTitle() + post.getContent()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String resp = response.body().string();
                        Log.d(TAG, "response: " + resp + " " + resp.getClass().getSimpleName());
                        if (resp.equals("false")) {
                            postRepository.insert(post);
                            isLoading.setValue(true);
                            Log.d(TAG, "all good");
                        }
                        // profanity found (no cussing in my application!)
                        else {
                            Log.d(TAG, "rejected");
                            isLoading.setValue(true);
                        }

                    } catch (IOException e) {
                        Log.e("AdViewModel", "errore");
                    }
                } else {
                    Log.d("AdViewModel", "NESSUNA RISPOSTA");
                }

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO Gestisci il fallimento della chiamata API
                isLoading.setValue(false);
                Log.e("AdViewModel", "fail message: " + t.getMessage());

            }
        });

    }
}