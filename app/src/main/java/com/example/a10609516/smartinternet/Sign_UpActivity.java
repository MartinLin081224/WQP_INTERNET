package com.example.a10609516.smartinternet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Sign_UpActivity extends WQPToolsActivity {

    private EditText last_name_edt, first_name_edt, phone_edt, address_edt;
    private TextView warning_txt;
    private Button birth_btn;
    private ImageView sign_up_btn;
    private Spinner sex_spinner, country_spinner;
    private ImageView gender_imv;

    private ArrayAdapter<String> sex_listAdapter, country_listAdapter;

    private String LOG = "Sign_UpActivity";
    private String U_SEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //取消ActionBar
        //getSupportActionBar().hide();
        //動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
        //性別的Spinner
        SexSpinner();
        //國家的Spinner
        CountrySpinner();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction(){
        last_name_edt = (EditText) findViewById(R.id.last_name_edt);
        first_name_edt = (EditText) findViewById(R.id.first_name_edt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        address_edt = (EditText) findViewById(R.id.address_edt);
        warning_txt = (TextView) findViewById(R.id.warning_txt);
        birth_btn = (Button) findViewById(R.id.birth_btn);
        sign_up_btn = (ImageView) findViewById(R.id.sign_up_btn);
        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        country_spinner = (Spinner) findViewById(R.id.country_spinner);
    }

    /**
     * 自動變更圖片適中大小
     */
    private void ResizeIcon() {
        /*final float density = getResources().getDisplayMetrics().density;
        final int width = Math.round(44 * density);
        final int height = Math.round(44 * density);*/

        Drawable drawable1 = getResources().getDrawable(R.drawable.icon_last);
        drawable1.setBounds(0, 0, last_name_edt.getHeight(), last_name_edt.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        last_name_edt.setCompoundDrawables(drawable1, null, null, null);//只放左邊
        Drawable drawable2 = getResources().getDrawable(R.drawable.icon_first);
        drawable2.setBounds(0, 0, first_name_edt.getHeight(), first_name_edt.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        first_name_edt.setCompoundDrawables(drawable2, null, null, null);//只放左邊
        Drawable drawable3 = getResources().getDrawable(R.drawable.icon_first);
        drawable3.setBounds(0, 0, phone_edt.getHeight(), phone_edt.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        phone_edt.setCompoundDrawables(drawable3, null, null, null);//只放左邊
        Drawable drawable4 = getResources().getDrawable(R.drawable.icon_first);
        drawable4.setBounds(0, 0, address_edt.getHeight(), address_edt.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        address_edt.setCompoundDrawables(drawable4, null, null, null);//只放左邊
        Drawable drawable5 = getResources().getDrawable(R.drawable.icon_birthday);
        drawable5.setBounds(0, 0, birth_btn.getHeight(), birth_btn.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        birth_btn.setCompoundDrawables(drawable5, null, null, null);//只放左邊
        /*Drawable drawable6 = getResources().getDrawable(R.drawable.icon_gender);
        drawable6.setBounds(0, 0, sex_spinner.getHeight(), sex_spinner.getHeight());//第一0是距左邊距離，第二0是距上邊距離，getHeight()分别是EditText長寬
        sex_spinner.setPadding(sex_spinner.getHeight(), 0, 0, 0);*/
    }


    /**
     *按壓事件監聽器
     */
    private void OnClickListener() {
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(last_name_edt.getText().toString().equals("") || first_name_edt.getText().toString().equals("")
                        || String.valueOf(sex_spinner.getSelectedItemId()).equals("0") || birth_btn.getText().toString().equals("")
                        || phone_edt.getText().toString().equals("") || String.valueOf(country_spinner.getSelectedItemId()).equals("0")
                        || address_edt.getText().toString().equals("")) {
                    warning_txt.setText(R.string.data_incomplete);
                } else {
                    warning_txt.setText("");
                    sendRequestWithOKHttpForSignUp();
                    Intent home_intent = new Intent(Sign_UpActivity.this, DeviceActivity.class);
                    startActivity(home_intent);
                }

            }
        });
    }

    /**
     * 性別的Spinner
     */
    private void SexSpinner() {
        sex_listAdapter = new ArrayAdapter<String>(this, R.layout.gender_spinner_row, R.id.gender, getResources().getStringArray(R.array.sex_array));
        //sex_listAdapter.setDropDownViewResource(R.layout.gender_spinner_row);
        sex_spinner.setAdapter(sex_listAdapter);
    }

    /**
     * 國家的Spinner
     */
    private void CountrySpinner() {
        country_listAdapter = new ArrayAdapter<String>(this, R.layout.country_spinner_row, R.id.country, getResources().getStringArray(R.array.country_array));
        //country_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(country_listAdapter);
    }

    /**
     * 與OKHttp連線(輸入帳號詳細基本資料)
     */
    private void sendRequestWithOKHttpForSignUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences user_email = getSharedPreferences("user_email", MODE_PRIVATE);
                String U_ACC = user_email.getString("ID", "");
                String LAST_NAME = last_name_edt.getText().toString();
                String FIRST_NAME = first_name_edt.getText().toString();
                String GENDER =  String.valueOf(sex_spinner.getSelectedItemId());
                if (GENDER.equals("1")){
                    U_SEX = "1";
                } else if (GENDER.equals("2")){
                    U_SEX = "0";
                }
                String U_BIRTH = birth_btn.getText().toString();
                String U_PHONE = phone_edt.getText().toString();
                String U_COUNTRY = String.valueOf(country_spinner.getSelectedItem());
                String U_ADDRESS = address_edt.getText().toString();

                Log.e(LOG, LAST_NAME + '-' + FIRST_NAME + '-' + GENDER + '-' + U_SEX + '-' + U_BIRTH + '-' + U_PHONE + '-' + U_COUNTRY + '-' + U_ADDRESS + '-' + U_ACC);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("LAST_NAME", LAST_NAME)
                            .add("FIRST_NAME", FIRST_NAME)
                            .add("U_SEX", U_SEX)
                            .add("U_BIRTH", U_BIRTH)
                            .add("U_PHONE", U_PHONE)
                            .add("U_COUNTRY", U_COUNTRY)
                            .add("U_ADDRESS", U_ADDRESS)
                            .add("U_ACC", U_ACC)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DataRegister.php")
                            //.url("http://192.168.0.172/BWT/DataRegister.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                }catch (Exception e) {
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
