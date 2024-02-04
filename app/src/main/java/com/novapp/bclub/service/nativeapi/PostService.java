package com.novapp.bclub.service.nativeapi;

import static com.novapp.bclub.utils.Constants.API_KEY;
import static com.novapp.bclub.utils.Constants.PROFANITY_API_BASE_URL;
import static com.novapp.bclub.utils.Utils.checkResponse;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.repository.post.IPostRepository;
import com.novapp.bclub.repository.post.PostRepositoryImpl;
import com.novapp.bclub.service.api.ProfanityApiService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostService {

    public static final String TAG = PostService.class.getSimpleName();

    private static final IPostRepository postRepository = new PostRepositoryImpl();

    public Task<Void> insert(Post post, Uri image) {

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PROFANITY_API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ProfanityApiService service = retrofit.create(ProfanityApiService.class);
        service.checkForPronfanity(API_KEY,
                        "{comment: {text: \"" + post.getTitle() + " " + post.getContent() + "\" }, requestedAttributes: {PROFANITY:{}, TOXICITY:{}} }")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

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

                            } catch (IOException e) {
                                Log.e(TAG, "Errore");
                            }
                        } else {
                            Log.d(TAG, Objects.requireNonNull(response.errorBody()).toString());
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
        return postRepository.getAllPost();
    }


}
