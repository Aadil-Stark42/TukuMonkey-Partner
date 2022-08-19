package com.tukumonkeypartner.delivered.canceledmvp;

import android.os.Handler;
import org.jetbrains.annotations.NotNull;
import com.tukumonkeypartner.delivered.model.DeliveredListResponse;
import com.tukumonkeypartner.retrofit.VdeliverzApi;
import com.tukumonkeypartner.utils.MnxPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancelledIntract implements CancelledContract.GetCancelledIntractor {

    String TAG= CancelledIntract.class.getSimpleName();

    interface OnCancelledListener {
        void onSuccess();
        void onError(String msg);
    }

    public void directValidation(final OnCancelledListener listener) {

        new Handler().postDelayed(() -> {
            listener.onSuccess();
        }, 500);
    }

    @Override
    public void cancelled_APICall(OnFinishedListener onFinishedListener) {
        VdeliverzApi.getClient().getcancelledorderlist().enqueue(new Callback<DeliveredListResponse>(){
            @Override
            public void onResponse(@NotNull Call<DeliveredListResponse> call, @NotNull Response<DeliveredListResponse> response) {
                if(response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            if(response.body().getStatus()){
                                onFinishedListener.onFinished(response.body());
                            }else {
                                onFinishedListener.onFailure(response.body().getMessage());
                            }
                        }else{
                            onFinishedListener.onFailure(response.message());
                        }
                    }else {
                        onFinishedListener.onFailure(response.message());
                    }
                }else {
                    if(response.code()==401){
                        MnxPreferenceManager.clearAllPreferences();
                        onFinishedListener.do_logout();
                    }else{
                        onFinishedListener.onFailure("Server Error");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<DeliveredListResponse> call, @NotNull Throwable t) {
                if(t !=null) {
                    onFinishedListener.onFailure(t.getMessage());
                }
            }
        });
    }
}
