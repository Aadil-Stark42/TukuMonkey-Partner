package com.tukumonkeypartner.collect_cash;


import com.tukumonkeypartner.utils.GeneralResponse;

public interface CashContract {

    void cash_success(GeneralResponse generalResponse);

    void cash_failure(String msg);

    void dashboard_logout();

    interface GetCashIntractor {

        interface OnFinishedListener {
            void onFinished(GeneralResponse generalResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void cashAPICall(OnFinishedListener onFinishedListener);
    }

}
