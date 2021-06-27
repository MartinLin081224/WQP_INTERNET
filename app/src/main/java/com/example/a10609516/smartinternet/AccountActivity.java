package com.example.a10609516.smartinternet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class AccountActivity extends WQPToolsActivity {
    private RelativeLayout acc_rlt;
    private TextView login_txt, warning_txt, count_txt;
    private EditText email_edt, pwd_edt, pwd_confirm_edt;
    private CheckBox pwd_cbx;
    private ImageView register_imv;

    private String LOG = "AccountActivity";
    private String COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //取消ActionBar
        //getSupportActionBar().hide();
        //動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction() {
        acc_rlt = (RelativeLayout) findViewById(R.id.acc_rlt);
        login_txt = (TextView) findViewById(R.id.login_txt);
        warning_txt = (TextView) findViewById(R.id.warning_txt);
        count_txt = (TextView) findViewById(R.id.count_txt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        pwd_edt = (EditText) findViewById(R.id.pwd_edt);
        pwd_confirm_edt = (EditText) findViewById(R.id.pwd_confirm_edt);
        pwd_cbx = (CheckBox) findViewById(R.id.pwd_cbx);
        register_imv = (ImageView) findViewById(R.id.register_imv);
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
        drawable2.setBounds(0, 0, email_edt.getHeight(), email_edt.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        pwd_edt.setCompoundDrawables(drawable2, null, null, null);//只放左邊
        pwd_confirm_edt.setCompoundDrawables(drawable2, null, null, null);//只放左邊
    }

    /**
     * 按壓事件監聽器
     */
    private void OnClickListener() {

        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_edt.getText().toString()).matches()) {
                    //與OKHttp連線(查詢是否已建立帳號)
                    sendRequestWithOkHttpForCheckAccount();
                } else {

                }
            }
        });

        register_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //驗證使用者輸入的文字是否為 Email 格式
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_edt.getText().toString()).matches()) {
                    //判斷兩次密碼是否為空
                    if (pwd_edt.getText().toString().trim().equals("") || pwd_confirm_edt.getText().toString().trim().equals("")) {
                        Toast.makeText(AccountActivity.this, R.string.empty_password, Toast.LENGTH_SHORT).show();
                    } else if (pwd_edt.getText().toString().trim().equals(pwd_confirm_edt.getText().toString().trim())) {
                        if (count_txt.getText().toString().equals("0")) {
                            sendRequestWithOKHttpForRegisterAccount();
                            Toast.makeText(AccountActivity.this, R.string.registered_success, Toast.LENGTH_SHORT).show();
                            Intent register_intent = new Intent(AccountActivity.this, Sign_UpActivity.class);
                            startActivity(register_intent);
                            email_edt.setText("");
                            pwd_edt.setText("");
                            pwd_confirm_edt.setText("");
                            count_txt.setText("");
                            warning_txt.setText("");
                        } else {

                        }
                    } else {
                        //if(pwd_edt.equals(pwd_edt.getText().toString().trim()) != pwd_confirm_edt.equals(pwd_confirm_edt.getText().toString().trim()))
                        Toast.makeText(AccountActivity.this, R.string.match_password, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AccountActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                }
                /*if (Linkify.addLinks(email_edt.getText(), Linkify.EMAIL_ADDRESSES)) {
                    email_edt.setText(email_edt.getText());
                } else {
                    Toast.makeText(AccountActivity.this, "請完整輸入Mail", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        pwd_cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    pwd_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwd_confirm_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    pwd_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwd_confirm_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(login_intent);
            }
        });
    }

    /**
     * 與OKHttp連線(查詢是否已建立帳號)
     */
    private void sendRequestWithOkHttpForCheckAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String U_ACC = email_edt.getText().toString();
                Log.e(LOG, U_ACC);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .build();
                    Log.e(LOG, U_ACC);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/CheckAccount.php")
                            //.url("http://192.168.0.172/BWT/CheckAccount.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    parseJSONWithJSONObjectForCheckAccount(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得是否已建立帳號
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

                AccountActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (COUNT.equals("1")) {
                            warning_txt.setText(R.string.already_create);
                            count_txt.setText("1");
                        } else {
                            warning_txt.setText("");
                            count_txt.setText("0");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OKHttp連線(建立帳號)
     */
    private void sendRequestWithOKHttpForRegisterAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String U_ACC = email_edt.getText().toString();
                String U_PWD = pwd_edt.getText().toString();
                Log.e(LOG, U_ACC);
                Log.e(LOG, U_PWD);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .add("U_PWD", U_PWD)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/AccountRegister.php")
                            //.url("http://192.168.0.172/BWT/AccountRegister.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
