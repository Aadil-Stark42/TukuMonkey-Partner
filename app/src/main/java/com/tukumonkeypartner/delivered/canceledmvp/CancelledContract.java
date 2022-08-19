package com.tukumonkeypartner.delivered.canceledmvp;

import com.tukumonkeypartner.delivered.model.DeliveredListResponse;

public interface CancelledContract {

    void cancelled_success(DeliveredListResponse deliveredListResponse);

    void cancelled_failure(String msg);

    void dashboard_logout();

    interface GetCancelledIntractor {

        interface OnFinishedListener {
            void onFinished(DeliveredListResponse deliveredListResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void cancelled_APICall(GetCancelledIntractor.OnFinishedListener onFinishedListener);
    }
}
