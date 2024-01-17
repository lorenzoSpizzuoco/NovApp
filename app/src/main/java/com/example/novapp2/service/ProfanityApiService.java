package com.example.novapp2.service;

import static com.example.novapp2.utils.Constants.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ProfanityApiService {
    @GET(CHECK_PROFANITY_ENDPOINT)
    Call<ResponseBody> checkForPronfanity(
            @Query("text") String text
    );
}