package com.example.novapp2.service;

import static com.example.novapp2.utils.Constants.API_KEY;
import static com.example.novapp2.utils.Constants.PROFANITY_API_BASE_URL;
import static com.example.novapp2.utils.Utils.checkResponse;

import android.net.Uri;
import android.util.Log;

import com.example.novapp2.entity.post.Post;
import com.example.novapp2.repository.post.IPostRepository;
import com.example.novapp2.repository.post.PostRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostService {

    public static final String TAG = PostService.class.getSimpleName();
    private Retrofit retrofit;

    private static IPostRepository repository = new PostRepository();

    public Task<Void> insert(Post post, Uri image) {

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(PROFANITY_API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ProfanityApiService service = retrofit.create(ProfanityApiService.class);
        service.checkForPronfanity(API_KEY,
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
                                    repository.insert(post, image).addOnCompleteListener(
                                            t -> {
                                                if (t.isSuccessful()) {
                                                    taskCompletionSource.setResult(t.getResult());
                                                }
                                            }
                                    );
                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Errore");
                            }
                        } else {
                            Log.d(TAG, response.errorBody().toString());
                            Log.d(TAG, "Nessuna risposta");
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // TODO Gestisci il fallimento della chiamata API
                        Log.e(TAG, "fail message: " + t.getMessage());

                    }
                });
        return taskCompletionSource.getTask();
    }

    public Task<List<Post>> getAllPost() {
        return repository.getAllPost();
    }

    public Task<Void> insertSavedPost(String user, String postId, int category) { return repository.insertSaved(user, postId, category); }

    public Task<Void> removeSavedPost(String user, String postId) { return repository.removeSaved(user, postId); }

    public Task<DataSnapshot> getSavedPosts(String user) { return repository.getFavoritePosts(user); }

    public Task<Integer> getIsSaved(String user, String id) {
        TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

        repository.getFavoritePosts(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isSaved = false;

                for (DataSnapshot ds : task.getResult().getChildren()) {
                    if (ds.getKey().equals(id)) {
                        isSaved = true;
                        break;
                    }
                }

                if (isSaved) {
                    taskCompletionSource.setResult(1);
                } else {
                    taskCompletionSource.setResult(0);
                }
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<List<Post>> getSavedPost(String user) {
        return repository.getSavedPosts(user);
    }

}
