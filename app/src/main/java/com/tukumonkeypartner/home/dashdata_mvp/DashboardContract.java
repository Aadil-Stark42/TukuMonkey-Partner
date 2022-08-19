package com.tukumonkeypartner.home.dashdata_mvp;


import com.tukumonkeypartner.home.model.DashboardResponse;

public interface DashboardContract {

    void dash_success(DashboardResponse dashboardResponse);

    void dash_failure(String msg);

    void dashboard_logout();

    interface GetLoginIntractor {

        interface OnFinishedListener {
            void onFinished(DashboardResponse dashboardResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void dashAPICall(OnFinishedListener onFinishedListener);
    }

}
