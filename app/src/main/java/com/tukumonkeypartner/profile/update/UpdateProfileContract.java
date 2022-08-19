package com.tukumonkeypartner.profile.update;


import com.tukumonkeypartner.utils.GeneralResponse;

public interface UpdateProfileContract {

    void updateProfile_success(GeneralResponse generalResponse);

    void updateProfile_failure(String msg);

    void dashboard_logout();

    interface UpdateProfileIntractor {

        interface OnFinishedListener {
            void onFinished(GeneralResponse generalResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void updateProfileAPICall(OnFinishedListener onFinishedListener);
    }

}
