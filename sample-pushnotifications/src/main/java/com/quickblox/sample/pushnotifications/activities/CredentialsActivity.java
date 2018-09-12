package com.quickblox.sample.pushnotifications.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.ServiceZone;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.ui.activity.CoreSplashActivity;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.sample.pushnotifications.R;
import com.quickblox.sample.pushnotifications.utils.Consts;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class CredentialsActivity extends CoreSplashActivity implements View.OnClickListener, View.OnLongClickListener{

    private final String BLANK_ERROR = "This field can not be blank";

    private EditText et_appId;
    private EditText et_accountKey;
    private EditText et_authKey;
    private EditText et_authSecret;
    private EditText et_apiServer;
    private EditText et_chatServer;
    private EditText et_login;
    private EditText et_password;
    private Button btn_connect;
    private String message;
    private ImageView iv_logo;


    public static void start(Context context, String message) {
        Intent intent = new Intent(context, CredentialsActivity.class);
        intent.putExtra(GcmConsts.EXTRA_GCM_MESSAGE, message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        message = getIntent().getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE); // to set in Message Activity
        initUI();
    }

    @Override
    protected String getAppName() {
        return getString(R.string.app_title);
    }

    @Override
    protected void proceedToTheNextActivity() {
        MessagesActivity.start(this, message);
        //finish();
    }

    private void initUI() {
        et_appId = findViewById(R.id.et_appId);
        et_accountKey = findViewById(R.id.et_accountKey);
        et_authKey = findViewById(R.id.et_authKey);
        et_authSecret = findViewById(R.id.et_authSecret);
        et_apiServer = findViewById(R.id.et_apiServer);
        et_chatServer = findViewById(R.id.et_chatServer);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);
        btn_connect.setOnLongClickListener(this);

        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(this);
        iv_logo.setOnLongClickListener(this);
    }

    private boolean checkTextFields() {
        return checkValueFromTextField(et_appId) && checkValueFromTextField(et_accountKey)
                && checkValueFromTextField(et_authKey) && checkValueFromTextField(et_authSecret)
                && checkValueFromTextField(et_apiServer) && checkValueFromTextField(et_chatServer)
                && checkValueFromTextField(et_login) && checkValueFromTextField(et_password);
    }

    private boolean checkValueFromTextField(EditText textField) {
        if(TextUtils.isEmpty(textField.getText().toString())) {
            textField.setError(BLANK_ERROR);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_logo) {
            fillByCustomCredentials(1);
        }
        if ((v.getId() == R.id.btn_connect) && checkTextFields()) {
            //((CoreApp) getApplication()).setCustomCredentials();
            signInQBbyCredentials();
            // go to next activity
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.iv_logo) {
            fillByCustomCredentials(2);
        }
        if (v.getId() == R.id.btn_connect) {
            signInQBbyDefaults();
        }
        return true;

    }

    private void fillByCustomCredentials(int code) {
        String appID = "";
        String authKey = "";
        String authSecret = "";
        String accountKey = "";
        String apiServer = "";
        String chatServer = "";
        String login = "";
        String password = "";

        if (code == 1) {
            appID = "72779";
            authKey = "T8r8-3dnfmejHds";
            authSecret = "N5RejRVHShtVhtk";
            accountKey = "J2wpVskzYqsyRHyxrA9w";
            apiServer = "https://api.quickblox.com";
            chatServer = "chat.quickblox.com";
            login = "pushtest01";
            password = "pushtest01";
        }

        if (code == 2) {
            appID = "72448";
            authKey = "f4HYBYdeqTZ7KNbb";
            authSecret = "ZC7dK39bOjVc-Z8";
            accountKey = "C4_z7nuaANnBYmsG_k98";
            apiServer = "https://api.quickblox.com";
            chatServer = "chat.quickblox.com";
            login = "test_user_id2";
            password = "test_user_id2";
        }

        et_appId.setText(appID);
        et_authKey.setText(authKey);
        et_authSecret.setText(authSecret);
        et_accountKey.setText(accountKey);
        et_apiServer.setText(apiServer);
        et_chatServer.setText(chatServer);
        et_login.setText(login);
        et_password.setText(password);
    }

    private void signInQBbyCredentials() {
        String appID = et_appId.getText().toString();
        String authKey = et_authKey.getText().toString();
        String authSecret = et_authSecret.getText().toString();
        String accountKey = et_accountKey.getText().toString();
        String apiServer = et_apiServer.getText().toString();
        String chatServer = et_chatServer.getText().toString();

        QBSettings.getInstance().init(getApplicationContext(), appID, authKey, authSecret);
        QBSettings.getInstance().setAccountKey(accountKey);
        QBSettings.getInstance().setEndpoints(apiServer, chatServer, ServiceZone.PRODUCTION);
        QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);

        String login = et_login.getText().toString();
        String password = et_password.getText().toString();
        QBUser qbUser = new QBUser(login, password);
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
                        signInQBbyCredentials();
                    }
                });
            }
        });
    }

    private void signInQBbyDefaults() {
        if (!checkSignIn()) {
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
                            signInQBbyDefaults();
                        }
                    });
                }
            });
        } else {
            proceedToTheNextActivityWithDelay();
        }
    }
}