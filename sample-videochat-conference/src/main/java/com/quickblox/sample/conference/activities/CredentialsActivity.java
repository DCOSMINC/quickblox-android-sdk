package com.quickblox.sample.conference.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.conference.ConferenceConfig;
import com.quickblox.core.ServiceZone;
import com.quickblox.sample.core.ui.activity.CoreSplashActivity;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.conference.R;

public class CredentialsActivity extends CoreSplashActivity implements View.OnClickListener{

    private String TAG = CredentialsActivity.class.getSimpleName();

    private static final String BLANK_ERROR = "This field can not be blank";
    private static final String APP_ID = "appId";
    private static final String ACCOUNT_KEY = "accountKey";
    private static final String AUTH_KEY = "authKey";
    private static final String AUTH_SECRET = "authSecret";
    private static final String API_SERVER = "apiServer";
    private static final String CHAT_SERVER = "chatServer";
    private static final String JANUS_SERVER = "janusServer";

    private EditText et_appId;
    private EditText et_accountKey;
    private EditText et_authKey;
    private EditText et_authSecret;
    private EditText et_apiServer;
    private EditText et_chatServer;
    private EditText et_janusServer;
    private Button btn_connect;
    private ImageView iv_logo;

    public static void start(Context context) {
        Intent intent = new Intent(context, CredentialsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        initUI();
        fillBySavedCredentials();
    }

    @Override
    protected String getAppName() {
        return getString(R.string.splash_app_title);
    }

    @Override
    protected void proceedToTheNextActivity() {
        LoginActivity.start(this);
        finish();
    }

    private void initUI() {
        et_appId = findViewById(R.id.et_appId);
        et_accountKey = findViewById(R.id.et_accountKey);
        et_authKey = findViewById(R.id.et_authKey);
        et_authSecret = findViewById(R.id.et_authSecret);
        et_apiServer = findViewById(R.id.et_apiServer);
        et_chatServer = findViewById(R.id.et_chatServer);
        et_janusServer = findViewById(R.id.et_janusServer);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);
        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(this);
    }

    private boolean checkTextFields() {
        return checkValueFromTextField(et_appId) && checkValueFromTextField(et_accountKey)
                && checkValueFromTextField(et_authKey) && checkValueFromTextField(et_authSecret)
                && checkValueFromTextField(et_apiServer) && checkValueFromTextField(et_chatServer)
                && checkValueFromTextField(et_janusServer);
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
            fillByCustomCredentials();
        }
        if ((v.getId() == R.id.btn_connect) && checkTextFields()) {
            signInQBbyCredentials();
        }
    }

    private void fillByCustomCredentials() {
        String appID = "73803";
        String authKey = "qp4zDcV8mk29Qp9";
        String authSecret = "Hm2KgDE6eeMZHu5";
        String accountKey = "uK_8uinNyz8-npTNB6tx";
        String apiServer = "https://api.quickblox.com";
        String chatServer = "chat.quickblox.com";
        String janusServer = "wss://janus.quickblox.com:8989";

        et_appId.setText(appID);
        et_authKey.setText(authKey);
        et_authSecret.setText(authSecret);
        et_accountKey.setText(accountKey);
        et_apiServer.setText(apiServer);
        et_chatServer.setText(chatServer);
        et_janusServer.setText(janusServer);
    }

    private void fillBySavedCredentials() {
        String appID = getSharedPreferencesValue(APP_ID);
        String authKey = getSharedPreferencesValue(AUTH_KEY);
        String authSecret = getSharedPreferencesValue(AUTH_SECRET);
        String accountKey = getSharedPreferencesValue(ACCOUNT_KEY);
        String apiServer = getSharedPreferencesValue(API_SERVER);
        String chatServer = getSharedPreferencesValue(CHAT_SERVER);
        String janusServer = getSharedPreferencesValue(JANUS_SERVER);

        et_appId.setText(appID);
        et_authKey.setText(authKey);
        et_authSecret.setText(authSecret);
        et_accountKey.setText(accountKey);
        et_apiServer.setText(apiServer);
        et_chatServer.setText(chatServer);
        et_janusServer.setText(janusServer);
    }

    private String getSharedPreferencesValue(String key) {
        String value = "";
        if (SharedPrefsHelper.getInstance().has(key)) {
            value = SharedPrefsHelper.getInstance().get(key);
        }
        return value;
    }

    private void saveCredentials() {
        String appID = et_appId.getText().toString();
        String authKey = et_authKey.getText().toString();
        String authSecret = et_authSecret.getText().toString();
        String accountKey = et_accountKey.getText().toString();
        String apiServer = et_apiServer.getText().toString();
        String chatServer = et_chatServer.getText().toString();
        String janusServer = et_janusServer.getText().toString();

        SharedPrefsHelper.getInstance().save(APP_ID, appID);
        SharedPrefsHelper.getInstance().save(AUTH_KEY, authKey);
        SharedPrefsHelper.getInstance().save(AUTH_SECRET, authSecret);
        SharedPrefsHelper.getInstance().save(ACCOUNT_KEY, accountKey);
        SharedPrefsHelper.getInstance().save(API_SERVER, apiServer);
        SharedPrefsHelper.getInstance().save(CHAT_SERVER, chatServer);
        SharedPrefsHelper.getInstance().save(JANUS_SERVER, janusServer);
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

        ConferenceConfig.setUrl(et_janusServer.getText().toString());
        startLoginActivity();
    }

    private void startLoginActivity() {
        LoginActivity.start(CredentialsActivity.this);
        finish();
    }
}