package com.tukumonkeypartner.mark_as_canceled;


import com.tukumonkeypartner.utils.GeneralResponse;

public interface CancelContract {

    void cancel_success(GeneralResponse generalResponse);

    void cancel_failure(String msg);

    void dashboard_logout();

    interface GetCancelIntractor {

        interface OnFinishedListener {
            void onFinished(GeneralResponse generalResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void cancelAPICall(OnFinishedListener onFinishedListener);
    }

}
