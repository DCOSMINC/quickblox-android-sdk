package com.quickblox.sample.pushnotifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;

/**
 * Created by egor on 10/5/18.
 */

public class QBFcmPushInstanceIDService extends com.quickblox.messages.services.fcm.QBFcmPushInstanceIDService {

    private static final String USER_LOGIN = "login";
    private static final String USER_PASSWORD = "password";

    @Override
    public void onTokenRefresh() {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (isCredentialsExist()){
                SubscribeService.subscribeToPushes(this, true);
            }
    }

    private boolean isCredentialsExist () {
        return SharedPrefsHelper.getInstance().has(USER_LOGIN) &&
                SharedPrefsHelper.getInstance().has(USER_PASSWORD) &&
                !SharedPrefsHelper.getInstance().get(USER_LOGIN).toString().isEmpty();
    }
}
