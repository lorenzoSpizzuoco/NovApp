package com.novapp.bclub.service.api;

import static com.novapp.bclub.utils.Constants.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfanityApiService {
    @POST(CHECK_PROFANITY_ENDPOINT)
    Call<ResponseBody> checkForPronfanity(
            @Query("key") String key,
            @Body String body
    );
}