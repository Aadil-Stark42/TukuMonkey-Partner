package com.tukumonkeypartner.mark_as_delivered;


import com.tukumonkeypartner.utils.GeneralResponse;

public interface DeliveredContract {

    void delivered_success(GeneralResponse generalResponse);

    void delivered_failure(String msg);

    void dashboard_logout();

    interface GetDeliveredIntractor {

        interface OnFinishedListener {
            void onFinished(GeneralResponse generalResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void deliveredAPICall(OnFinishedListener onFinishedListener);
    }

}
