package com.tukumonkeypartner.notification.notificationmvp;


import com.tukumonkeypartner.notification.model.NotificationListResponse;

public interface NotificationContract {

    void notification_success(NotificationListResponse notificationListResponse);

    void notification_failure(String msg);

    void dashboard_logout();

    interface GetNotificationIntractor {

        interface OnFinishedListener {
            void onFinished(NotificationListResponse notificationListResponse);
            void onFailure(String error_msg);
            void do_logout();
        }
        void notification_APICall(GetNotificationIntractor.OnFinishedListener onFinishedListener);
    }
}
