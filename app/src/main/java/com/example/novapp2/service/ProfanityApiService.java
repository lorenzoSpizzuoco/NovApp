package com.example.novapp2.service;

import static com.example.novapp2.utils.Constants.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfanityApiService {
    @POST(CHECK_PROFANITY_ENDPOINT)
    Call<ResponseBody> checkForPronfanity(
            @Query("key") String key,
            @Body String body
    );
}