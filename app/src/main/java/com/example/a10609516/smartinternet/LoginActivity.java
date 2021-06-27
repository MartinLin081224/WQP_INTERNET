package com.example.a10609516.smartinternet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends WQPToolsActivity {

    private TextView warning_txt;
    private EditText email_edt, pwd_edt;
    private ImageView login_imv;
    private CheckBox remember_cbx;

    private String LOG = "LoginActivity";
    private String COUNT, account_str, password_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //取消ActionBar
        //getSupportActionBar().hide();
        //動態取得 View 物件
        InItFunction();
        //自動變更圖片適中大小
        //ResizeIcon();
        //按壓事件監聽器
        OnClickListener();
        //記住帳密功能
        SharedPreferencesWithLogin();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction(){
        warning_txt = (TextView) findViewById(R.id.warning_txt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        pwd_edt = (EditText) findViewById(R.id.pwd_edt);
        login_imv = (ImageView) findViewById(R.id.login_imv);
        remember_cbx = (CheckBox) findViewById(R.id.remember_cbx);
    }

    /**
     * 自動變更圖片適中大小
     */
    private void ResizeIcon() {
        /*final float density = getResources().getDisplayMetrics().density;
        final int width = Math.round(44 * density);
        final int height = Math.round(44 * density);*/

        Log.e(LOG, String.valueOf(email_edt.getMeasuredHeight()));

        Drawable drawable1 = getResources().getDrawable(R.drawable.icon_email);
        drawable1.setBounds(0, 0, email_edt.getHeight(), email_edt.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        email_edt.setCompoundDrawables(drawable1, null, null, null);//只放左邊
        Drawable drawable2 = getResources().getDrawable(R.drawable.icon_password);
        drawable2.setBounds(0, 0, pwd_edt.getHeight(), pwd_edt.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        pwd_edt.setCompoundDrawables(drawable2, null, null, null);//只放左邊
    }

    /**
     *按壓事件監聽器
     */
    private void OnClickListener() {
        login_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //驗證使用者輸入的文字是否為 Email 格式
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_edt.getText().toString()).matches()) {
                    //與OKHttp連線(查詢輸入的帳號密碼是否正確)
                    sendRequestWithOkHttpForCheckUserAccount();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 記住帳密功能
     */
    private void SharedPreferencesWithLogin() {
        SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
        //SharedPreferences將account 和 password 記錄起來 每次進去APP時 開始從中讀取資料 放入accountEdit，passwordEdit中
        account_str = remdname.getString("account", "");
        password_str = remdname.getString("password", "");
        email_edt.setText(account_str);
        pwd_edt.setText(password_str);
        //如果remember_checkBox勾選，記住帳密   remember_checkBox不勾選，不記住帳密
        remember_cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("account", email_edt.getText().toString());
                    edit.putString("password", pwd_edt.getText().toString());
                    edit.commit();
                }
                if (!isChecked) {
                    SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("account", "");
                    edit.putString("password", "");
                    edit.commit();
                }
            }
        });
    }

    /**
     * 與OKHttp連線(查詢輸入的帳號密碼是否正確)
     */
    private void sendRequestWithOkHttpForCheckUserAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String U_ACC = email_edt.getText().toString();
                String U_PWD = pwd_edt.getText().toString();
                Log.e(LOG, "U_ACC:" + U_ACC);
                Log.e(LOG, "U_PWD:" + U_PWD);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .add("U_PWD", U_PWD)
                            .build();
                    Log.e(LOG, U_ACC);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/CheckUserAccount.php")
                            //.url("http://192.168.0.172/BWT/CheckUserAccount.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForCheckAccount(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得帳號密碼是否正確
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForCheckAccount(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COUNT = jsonObject.getString("COUNT");
                Log.e(LOG, "COUNT123:" + COUNT);

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (COUNT.equals("1")) {
                            warning_txt.setText("");
                            SharedPreferences sharedPreferences = getSharedPreferences("user_email", MODE_PRIVATE);
                            sharedPreferences.edit().putString("ID", email_edt.getText().toString()).apply();
                            Intent login_intent = new Intent(LoginActivity.this, DeviceActivity.class);
                            startActivity(login_intent);
                        } else {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    warning_txt.setText(R.string.input_wrong);
                                }
                            });
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Activity窗口獲得或失去焦點時被调用,在onResume之後或onPause之後
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Log.i(LOG, "onWindowFocusChanged is called and" + "hasFocus is true");
            //自動變更圖片適中大小
            ResizeIcon();
        }else {
            Log.i(LOG, "onWindowFocusChanged is called and" + "hasFocus is false");
            //自動變更圖片適中大小
            ResizeIcon();
        }
    }
}
