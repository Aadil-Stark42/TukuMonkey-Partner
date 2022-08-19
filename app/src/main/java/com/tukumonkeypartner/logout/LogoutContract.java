package com.tukumonkeypartner.logout;

import com.tukumonkeypartner.utils.GeneralResponse;

public interface LogoutContract{

    void logout_success(GeneralResponse generalResponse);

    void logout_failure(String msg);

    void dashboard_logout();

    interface GetLogoutIntractor {
        interface OnFinishedListener {
            void onFinished(GeneralResponse generalResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void logoutAPICall(OnFinishedListener onFinishedListener);
    }

}
