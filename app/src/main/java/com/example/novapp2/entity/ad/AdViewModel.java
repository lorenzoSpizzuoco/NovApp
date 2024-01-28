package com.example.novapp2.entity.ad;

import static com.example.novapp2.utils.Constants.PROFANITY_API_BASE_URL;

import android.app.Application;
import android.util.Log;

import com.example.novapp2.service.ProfanityApiService;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.novapp2.repository.AdRepository;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdViewModel extends AndroidViewModel {

    static final private String TAG = AdViewModel.class.getSimpleName();
    private Retrofit retrofit;
    private AdRepository adRepository;

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final LiveData<List<Ad>> allAd;

    public AdViewModel (Application application) {
        super(application);
        adRepository = new AdRepository(application);
        allAd = adRepository.getAllAd();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Ad>> getAllAd() {return allAd; }

    public void insert(Ad ad) {

        //isLoading.setValue(true);
        // check for profanity
        retrofit = new Retrofit.Builder()
                .baseUrl(PROFANITY_API_BASE_URL)
                .build();

        ProfanityApiService service = retrofit.create(ProfanityApiService.class);
        service.checkForPronfanity(ad.getTitle() + ad.getContent()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String resp = response.body().string();
                        Log.d(TAG, "response: " + resp + " " + resp.getClass().getSimpleName());
                        if (resp.equals("false")) {
                            adRepository.insert(ad);
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