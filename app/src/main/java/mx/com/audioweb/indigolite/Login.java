package mx.com.audioweb.indigolite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import mx.com.audioweb.indigolite.TimeTracker.Shared_notifications;
import mx.com.audioweb.indigolite.TimeTracker.api.CONFIG;
import mx.com.audioweb.indigolite.TimeTracker.task.UserLoginTask;
import org.json.JSONObject;


public class Login extends Activity {

    EditText User, Pass;
    JSONObject jData,json;
    Button LogIn;
    String userName, password, webURL, getUserName, android_id;
    private Bundle id;
    SharedPreferences preferences;
    Context mContext ;
    boolean authVoiceID = false, isLogin, isAuth = false, callService = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = Login.this;
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final Shared_notifications session = new Shared_notifications(mContext);
        final SharedPreferences.Editor edit = preferences.edit();

        if (!CONFIG.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_LONG).show();
        }

        if (preferences.getBoolean("isUserLogin", false)) {

            if (!preferences.getBoolean("isVoiceAuthenticated", false)) {
                Intent mainIntent = new Intent(mContext, Home.class);
                startActivity(mainIntent);
                finish();
            } else {
                /*Intent mainIntent = new Intent(mContext, ActivityUserDetailScreen.class);
                startActivity(mainIntent);
                finish();*/
            }
        }

        User = (EditText) findViewById(R.id.userText);
        Pass = (EditText) findViewById(R.id.passwordText);

        LogIn = (Button) findViewById(R.id.loginButton);

        json = new JSONObject();
        jData = new JSONObject();

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            android_id = TelephonyMgr.getDeviceId();
        } else {
            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (wm.isWifiEnabled() == false) {
                // enable wifi if it is disabled
                wm.setWifiEnabled(true);
                Toast.makeText(Login.this, "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            }
            if (wm.isWifiEnabled() == true) {
                android_id = wm.getConnectionInfo().getMacAddress();
            }
        }
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CONFIG.isNetworkAvailable(mContext)) {
                    Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_LONG).show();
                } else {
                    userName = User.getText().toString();
                    password = Pass.getText().toString();
                    preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    edit.commit();

                    webURL = CONFIG.SERVER_URL + "salesmen/login";
                    try {
                        json.put("username", userName);
                        json.put("password", password);
                        json.put("device_id", "0");

                        Log.e("Logon json", json.toString());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    UserLoginTask login = new UserLoginTask(mContext, json);
                    try {
                        if (!CONFIG.isNetworkAvailable(mContext)) {
                            Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_LONG).show();
                        } else {
                            jData = login.execute(webURL).get();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    edit.putString("User Name", userName);
                    try {
                        if (jData.getString("status").matches("SUCCESS")) {
                            Log.e("USER ID--> ", jData.getString("smen_id").toString());
                            String user_id = jData.getString("smen_id");
                            id = new Bundle();
                            id.putSerializable("uid", user_id);
                            edit.putBoolean("isUserLogin", true);
                            edit.putBoolean("notification",false);
                            session.createNotification();
                            edit.putBoolean("Call_Service", callService);
                            edit.commit();
                            authVoiceID = jData.getString("smen_auth_voice_id").matches("0");
                            startActivity(new Intent(mContext, Home.class).putExtras(id));
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}


