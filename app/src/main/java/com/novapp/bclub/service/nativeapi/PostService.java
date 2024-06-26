package com.novapp.bclub.service.nativeapi;
import static com.novapp.bclub.utils.Constants.PROFANITY_API_BASE_URL;
import static com.novapp.bclub.utils.Utils.checkResponse;

import android.app.Application;
import android.net.Uri;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.R;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.repository.post.IPostRepository;
import com.novapp.bclub.repository.post.PostRepositoryImpl;
import com.novapp.bclub.service.api.ProfanityApiService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostService {

    public static final String TAG = PostService.class.getSimpleName();

    private static IPostRepository postRepository;
    String api_key;
    public PostService(Application application) {
        api_key = application.getString(R.string.api_key);
        postRepository = new PostRepositoryImpl(application);
    }

    public Task<Void> insert(Post post, Uri image) {

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PROFANITY_API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ProfanityApiService service = retrofit.create(ProfanityApiService.class);
        service.checkForPronfanity(api_key,
                        "{comment: {text: \"" + post.getTitle() + " " + post.getContent() + "\" }, requestedAttributes: {PROFANITY:{}, TOXICITY:{}} }")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String resp = response.body().string();
                                Log.d(TAG, "response: " + resp + " " + resp.getClass().getSimpleName());
                                boolean res = checkResponse(resp);
                                if (res) {
                                    // insertion
                                    postRepository.insert(post, image).addOnCompleteListener(
                                            t -> {
                                                if (t.isSuccessful()) {
                                                    taskCompletionSource.setResult(t.getResult());
                                                }
                                            }
                                    );
                                }
                                else {
                                    taskCompletionSource.setException(new Exception("profanity"));
                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Errore");
                            }
                        } else {
                            taskCompletionSource.setException(new Exception("fail"));
                            Log.d(TAG, response.errorBody().toString());
                            Log.d(TAG, "Nessuna risposta");
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "fail message: " + t.getMessage());
                        taskCompletionSource.setException(new Exception("fail"));
                    }
                });

        return taskCompletionSource.getTask();
    }


    public LiveData<List<Post>> getAllPostRoom(boolean refresh) {
        return postRepository.getAllPostRoom(refresh);
    }

    public void deleteAll() {
        postRepository.deleteAll();
    }

    public void removeSaved(Post post) {
        postRepository.removeSaved(post);
    }

    public void insertSaved(Post post) {
        postRepository.insertLocal(post);
    }

    public LiveData<List<Post>> getUserPosts(String user) {
        return postRepository.getUserPost(user);
    }

}
