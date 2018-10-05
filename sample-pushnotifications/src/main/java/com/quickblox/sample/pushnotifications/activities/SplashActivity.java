package com.quickblox.sample.pushnotifications.activities;

import android.os.Bundle;
import android.view.View;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.ServiceZone;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.activity.CoreSplashActivity;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.sample.pushnotifications.R;
import com.quickblox.sample.pushnotifications.utils.Consts;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SplashActivity extends CoreSplashActivity {

    private static final String APP_ID = "appId";
    private static final String ACCOUNT_KEY = "accountKey";
    private static final String AUTH_KEY = "authKey";
    private static final String AUTH_SECRET = "authSecret";
    private static final String API_SERVER = "apiServer";
    private static final String CHAT_SERVER = "chatServer";
    private static final String USER_LOGIN = "login";
    private static final String USER_PASSWORD = "password";

    private String message;
    private String appID;
    private String authKey;
    private String authSecret;
    private String accountKey;
    private String apiServer;
    private String chatServer;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_MESSAGE);
        }

        if (checkConfigsWithSnackebarError()){
            signInQB();
        }
    }

    private void startMessageActivity () {
        MessagesActivity.start(this, message);
    }

    private void signInQB() {
        if (SharedPrefsHelper.getInstance().has(USER_LOGIN) && SharedPrefsHelper.getInstance().has(USER_PASSWORD)) {
            getSavedCredentials();
            initQBServices();

            QBUsers.signIn(login, password).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    startMessageActivity();
                    finish();
                }

                @Override
                public void onError(QBResponseException e) {
                    Toaster.shortToast("Unable to Sign In with saved User");
                }
            });
        } else {
            CredentialsActivity.start(this, message);
        }
        /*if (!checkSignIn()) {

            QBUser qbUser = CoreConfigUtils.getUserFromConfig(Consts.SAMPLE_CONFIG_FILE_NAME);

            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    proceedToTheNextActivity();
                }

                @Override
                public void onError(QBResponseException e) {
                    showSnackbarError(null, R.string.splash_create_session_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signInQB();
                        }
                    });
                }
            });
        } else {
            proceedToTheNextActivityWithDelay();
        }*/
    }

    private void initQBServices() {
        QBSettings.getInstance().init(getApplicationContext(), appID, authKey, authSecret);
        QBSettings.getInstance().setAccountKey(accountKey);
        QBSettings.getInstance().setEndpoints(apiServer, chatServer, ServiceZone.PRODUCTION);
        QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    private void getSavedCredentials() {
        appID = getSharedPreferencesValue(APP_ID);
        authKey = getSharedPreferencesValue(AUTH_KEY);
        authSecret = getSharedPreferencesValue(AUTH_SECRET);
        accountKey = getSharedPreferencesValue(ACCOUNT_KEY);
        apiServer = getSharedPreferencesValue(API_SERVER);
        chatServer = getSharedPreferencesValue(CHAT_SERVER);
        login = SharedPrefsHelper.getInstance().get(USER_LOGIN);
        password = SharedPrefsHelper.getInstance().get(USER_PASSWORD);
    }

    private String getSharedPreferencesValue(String key) {
        String value = "";
        if (SharedPrefsHelper.getInstance().has(key)) {
            value = SharedPrefsHelper.getInstance().get(key);
        }
        return value;
    }

    @Override
    protected String getAppName() {
        return getString(R.string.app_title);
    }

    @Override
    protected void proceedToTheNextActivity() {
        //MessagesActivity.start(this, message);
        //CredentialsActivity.start(this, message);
        finish();
    }

    @Override
    protected boolean sampleConfigIsCorrect() {
        boolean result = super.sampleConfigIsCorrect();
        result = result && CoreConfigUtils.getUserFromConfig(Consts.SAMPLE_CONFIG_FILE_NAME) != null;
        return result;
    }

}