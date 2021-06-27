package com.example.a10609516.smartinternet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StatusActivity extends AppCompatActivity {

    private LinearLayout status_llt, status_all_llt, value_llt1, value_llt2, value_llt3,
            value_llt4, value_llt5, value_llt6;
    private TextView name_txt, id_txt, description_txt, filter_txt1, filter_txt2,
            filter_txt3, filter_txt4, filter_txt5, filter_txt6, value_txt1,
            value_txt2, value_txt3, value_txt4, value_txt5, value_txt6,
            value_total_txt1, value_total_txt2, value_total_txt3, value_total_txt4, value_total_txt5,
            value_total_txt6;
    private ProgressBar value_pgr1, value_pgr2, value_pgr3, value_pgr4, value_pgr5, value_pgr6;

    private String LOG = "StatusActivity";
    private String name, description, MAC, MD5, MODEL,
            D_NAME, FILTER01, DATA01, FILTER02, DATA02,
            FILTER03, DATA03, FILTER04, DATA04, FILTER05,
            DATA05, FILTER06, DATA06, DT, S_DATA01,
            S_DATA02, S_DATA03, S_DATA04, S_DATA05, S_DATA06,
            VALUE01, VALUE02, VALUE03, VALUE04, VALUE05, VALUE06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        //動態取得 View 物件
        InItFunction();
        //取得OrderSearchActivity傳遞過來的值
        GetResponseData();
        //與OKHttp連線(查詢該設備名稱)
        sendRequestWithOkHttpForDeviceModel();
        // 與OKHttp連線(查詢該設備使用的Log)
        sendRequestWithOkHttpForDeviceStatusLog();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction() {
        status_all_llt = (LinearLayout) findViewById(R.id.status_all_llt);
        status_llt = (LinearLayout) findViewById(R.id.status_llt);
        value_llt1 = (LinearLayout) findViewById(R.id.value_llt1);
        value_llt2 = (LinearLayout) findViewById(R.id.value_llt2);
        value_llt3 = (LinearLayout) findViewById(R.id.value_llt3);
        value_llt4 = (LinearLayout) findViewById(R.id.value_llt4);
        value_llt5 = (LinearLayout) findViewById(R.id.value_llt5);
        value_llt6 = (LinearLayout) findViewById(R.id.value_llt6);
        name_txt = (TextView) findViewById(R.id.name_txt);
        id_txt = (TextView) findViewById(R.id.id_txt);
        filter_txt1 = (TextView) findViewById(R.id.filter_txt1);
        filter_txt2 = (TextView) findViewById(R.id.filter_txt2);
        filter_txt3 = (TextView) findViewById(R.id.filter_txt3);
        filter_txt4 = (TextView) findViewById(R.id.filter_txt4);
        filter_txt5 = (TextView) findViewById(R.id.filter_txt5);
        filter_txt6 = (TextView) findViewById(R.id.filter_txt6);
        value_txt1 = (TextView) findViewById(R.id.value_txt1);
        value_txt2 = (TextView) findViewById(R.id.value_txt2);
        value_txt3 = (TextView) findViewById(R.id.value_txt3);
        value_txt4 = (TextView) findViewById(R.id.value_txt4);
        value_txt5 = (TextView) findViewById(R.id.value_txt5);
        value_txt6 = (TextView) findViewById(R.id.value_txt6);
        value_total_txt1 = (TextView) findViewById(R.id.value_total_txt1);
        value_total_txt2 = (TextView) findViewById(R.id.value_total_txt2);
        value_total_txt3 = (TextView) findViewById(R.id.value_total_txt3);
        value_total_txt4 = (TextView) findViewById(R.id.value_total_txt4);
        value_total_txt5 = (TextView) findViewById(R.id.value_total_txt5);
        value_total_txt6 = (TextView) findViewById(R.id.value_total_txt6);
        value_pgr1 = (ProgressBar) findViewById(R.id.value_pgr1);
        value_pgr2 = (ProgressBar) findViewById(R.id.value_pgr2);
        value_pgr3 = (ProgressBar) findViewById(R.id.value_pgr3);
        value_pgr4 = (ProgressBar) findViewById(R.id.value_pgr4);
        value_pgr5 = (ProgressBar) findViewById(R.id.value_pgr5);
        value_pgr6 = (ProgressBar) findViewById(R.id.value_pgr6);
        description_txt = (TextView) findViewById(R.id.description_txt);
    }

    /**
     * 取得SearchActivity傳遞過來的值
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        String d_md5 = bundle.getString("d_md5");
        String d_mac = bundle.getString("d_mac");
        String d_name = bundle.getString("d_name");
        name_txt.setText(d_name);
        Log.e(LOG, d_md5 + " - " + d_mac + " - " + d_name);
    }

    /**
     * 與OKHttp連線(查詢該設備名稱)
     */
    private void sendRequestWithOkHttpForDeviceModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = getIntent().getExtras();
                String d_md5 = bundle.getString("d_md5");
                Log.e(LOG, d_md5);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("device_md5", d_md5)
                            .build();
                    Log.e(LOG, "MD5:" + d_md5);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DeviceModel.php")
                            //.url("http://192.168.0.172/BWT/DeviceModel.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceModel(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得該設備名稱
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDeviceModel(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.getString("name");
                description = jsonObject.getString("description");
                Log.e(LOG, name + "--" + description);
                //id_txt.setText("");
                //description_txt.setText("");
                StatusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        id_txt.setText(name);
                        description_txt.setText(description);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OKHttp連線(查詢該設備使用的Log)
     */
    private void sendRequestWithOkHttpForDeviceStatusLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = getIntent().getExtras();
                String d_md5 = bundle.getString("d_md5");
                String d_mac = bundle.getString("d_mac");
                Log.e(LOG, d_md5);
                Log.e(LOG, d_mac);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("device_md5", d_md5)
                            .build();
                    Log.e(LOG, d_md5);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DeviceData.php")
                            //.url("http://192.168.0.172/BWT/DeviceData.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceStatusLog(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得該設備使用的Log
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDeviceStatusLog(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MAC = jsonObject.getString("MAC");
                //String GET_BACK = new String(jsonObject.getString("GET_BACK").getBytes(),"utf-8");
                MD5 = jsonObject.getString("MD5");
                MODEL = jsonObject.getString("MODEL");
                D_NAME = jsonObject.getString("D_NAME");
                FILTER01 = jsonObject.getString("FILTER01");
                DATA01 = jsonObject.getString("DATA01");
                FILTER02 = jsonObject.getString("FILTER02");
                DATA02 = jsonObject.getString("DATA02");
                FILTER03 = jsonObject.getString("FILTER03");
                DATA03 = jsonObject.getString("DATA03");
                FILTER04 = jsonObject.getString("FILTER04");
                DATA04 = jsonObject.getString("DATA04");
                FILTER05 = jsonObject.getString("FILTER05");
                DATA05 = jsonObject.getString("DATA05");
                FILTER06 = jsonObject.getString("FILTER06");
                DATA06 = jsonObject.getString("DATA06");
                DT = jsonObject.getString("DT");
                S_DATA01 = jsonObject.getString("S_DATA01");
                S_DATA02 = jsonObject.getString("S_DATA02");
                S_DATA03 = jsonObject.getString("S_DATA03");
                S_DATA04 = jsonObject.getString("S_DATA04");
                S_DATA05 = jsonObject.getString("S_DATA05");
                S_DATA06 = jsonObject.getString("S_DATA06");
                VALUE01 = jsonObject.getString("VALUE01");
                VALUE02 = jsonObject.getString("VALUE02");
                VALUE03 = jsonObject.getString("VALUE03");
                VALUE04 = jsonObject.getString("VALUE04");
                VALUE05 = jsonObject.getString("VALUE05");
                VALUE06 = jsonObject.getString("VALUE06");

                Log.e(LOG, FILTER01);

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(MAC);
                JArrayList.add(MD5);
                JArrayList.add(MODEL);
                JArrayList.add(D_NAME);
                JArrayList.add(FILTER01);
                JArrayList.add(DATA01);
                JArrayList.add(FILTER02);
                JArrayList.add(DATA02);
                JArrayList.add(FILTER03);
                JArrayList.add(DATA03);
                JArrayList.add(FILTER04);
                JArrayList.add(DATA04);
                JArrayList.add(FILTER05);
                JArrayList.add(DATA05);
                JArrayList.add(FILTER06);
                JArrayList.add(DATA06);
                JArrayList.add(DT);

                StatusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int I_DATA01 = Integer.parseInt(S_DATA01);
                        int I_DATA02 = Integer.parseInt(S_DATA02);
                        int I_DATA03 = Integer.parseInt(S_DATA03);
                        int I_DATA04 = Integer.parseInt(S_DATA04);
                        int I_DATA05 = Integer.parseInt(S_DATA05);
                        int I_DATA06 = Integer.parseInt(S_DATA06);
                        int I_VALUE01 = Integer.parseInt(VALUE01);
                        int I_VALUE02 = Integer.parseInt(VALUE02);
                        int I_VALUE03 = Integer.parseInt(VALUE03);
                        int I_VALUE04 = Integer.parseInt(VALUE04);
                        int I_VALUE05 = Integer.parseInt(VALUE05);
                        int I_VALUE06 = Integer.parseInt(VALUE06);

                        filter_txt1.setText(FILTER01);
                        filter_txt2.setText(FILTER02);
                        filter_txt3.setText(FILTER03);
                        filter_txt4.setText(FILTER04);
                        filter_txt5.setText(FILTER05);
                        filter_txt6.setText(FILTER06);
                        value_txt1.setText(S_DATA01 + " ml ");
                        value_txt2.setText(S_DATA02 + " ml ");
                        value_txt3.setText(S_DATA03 + " ml ");
                        value_txt4.setText(S_DATA04 + " ml ");
                        value_txt5.setText(S_DATA05 + " ml ");
                        value_txt6.setText(S_DATA06 + " ml ");
                        value_total_txt1.setText(VALUE01 + " ml ");
                        value_total_txt2.setText(VALUE02 + " ml ");
                        value_total_txt3.setText(VALUE03 + " ml ");
                        value_total_txt4.setText(VALUE04 + " ml ");
                        value_total_txt5.setText(VALUE05 + " ml ");
                        value_total_txt6.setText(VALUE06 + " ml ");
                        value_pgr1.setMax(I_VALUE01);
                        value_pgr2.setMax(I_VALUE02);
                        value_pgr3.setMax(I_VALUE03);
                        value_pgr4.setMax(I_VALUE04);
                        value_pgr5.setMax(I_VALUE05);
                        value_pgr6.setMax(I_VALUE06);
                        value_pgr1.setProgress(I_DATA01);
                        value_pgr2.setProgress(I_DATA02);
                        value_pgr3.setProgress(I_DATA03);
                        value_pgr4.setProgress(I_DATA04);
                        value_pgr5.setProgress(I_DATA05);
                        value_pgr6.setProgress(I_DATA06);

                        if (FILTER01.equals("")){
                            value_llt1.setVisibility(View.GONE);
                        }
                        if (FILTER02.equals("")){
                            value_llt2.setVisibility(View.GONE);
                        }
                        if (FILTER03.equals("")){
                            value_llt3.setVisibility(View.GONE);
                        }
                        if (FILTER04.equals("")){
                            value_llt4.setVisibility(View.GONE);
                        }
                        if (FILTER05.equals("")){
                            value_llt5.setVisibility(View.GONE);
                        }
                        if (FILTER06.equals("")){
                            value_llt6.setVisibility(View.GONE);
                        }
                    }
                });

                //HandlerMessage更新UI
                Bundle b = new Bundle();
                b.putStringArrayList("JSON_data", JArrayList);
                Message msg = mHandler.obtainMessage();
                msg.setData(b);
                msg.what = 1;
                msg.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新UI
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt0 = new LinearLayout(StatusActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(StatusActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(StatusActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(StatusActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt4 = new LinearLayout(StatusActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt5 = new LinearLayout(StatusActivity.this);
                    small_llt5.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt6 = new LinearLayout(StatusActivity.this);
                    small_llt6.setOrientation(LinearLayout.HORIZONTAL);
                    /*LinearLayout small_llt7 = new LinearLayout(StatusActivity.this);
                    small_llt7.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt8 = new LinearLayout(StatusActivity.this);
                    small_llt8.setOrientation(LinearLayout.HORIZONTAL);*/

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //顯示每筆LinearLayout的時間
                    TextView dynamically_txt0;
                    dynamically_txt0 = new TextView(StatusActivity.this);
                    dynamically_txt0.setText(JArrayList.get(16).trim());
                    dynamically_txt0.setGravity(Gravity.LEFT);
                    dynamically_txt0.setPadding(20, 10, 0, 10);
                    //dynamically_txt0.setWidth(50);
                    dynamically_txt0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第一道)
                    TextView dynamically_txt1;
                    dynamically_txt1 = new TextView(StatusActivity.this);
                    dynamically_txt1.setText(getString(R.string.filter) + "(" + JArrayList.get(4) + ") : " + JArrayList.get(5).trim() + " ml");
                    dynamically_txt1.setGravity(Gravity.LEFT);
                    dynamically_txt1.setPadding(20, 10, 0, 10);
                    //dynamically_number.setWidth(50);
                    dynamically_txt1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第二道)
                    TextView dynamically_txt2;
                    dynamically_txt2 = new TextView(StatusActivity.this);
                    dynamically_txt2.setText(getString(R.string.filter) + "("  + JArrayList.get(6) + ") : " + JArrayList.get(7).trim() + " ml");
                    dynamically_txt2.setGravity(Gravity.LEFT);
                    dynamically_txt2.setPadding(20, 10, 0, 10);
                    dynamically_txt2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第三道)
                    TextView dynamically_txt3;
                    dynamically_txt3 = new TextView(StatusActivity.this);
                    dynamically_txt3.setText(getString(R.string.filter) + "("  + JArrayList.get(8) + ") : " + JArrayList.get(9).trim() + " ml");
                    dynamically_txt3.setGravity(Gravity.LEFT);
                    dynamically_txt3.setPadding(20, 10, 0, 10);
                    dynamically_txt3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第四道)
                    TextView dynamically_txt4;
                    dynamically_txt4 = new TextView(StatusActivity.this);
                    dynamically_txt4.setText(getString(R.string.filter) + "("  + JArrayList.get(10) + ") : " + JArrayList.get(11).trim() + " ml");
                    dynamically_txt4.setGravity(Gravity.LEFT);
                    dynamically_txt4.setPadding(20, 10, 0, 10);
                    dynamically_txt4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第五道)
                    TextView dynamically_txt5;
                    dynamically_txt5 = new TextView(StatusActivity.this);
                    dynamically_txt5.setText(getString(R.string.filter) + "("  + JArrayList.get(12) + ") : " + JArrayList.get(13).trim() + " ml");
                    dynamically_txt5.setGravity(Gravity.LEFT);
                    dynamically_txt5.setPadding(20, 10, 0, 10);
                    dynamically_txt5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //顯示每筆LinearLayout的濾芯(第六道)
                    TextView dynamically_txt6;
                    dynamically_txt6 = new TextView(StatusActivity.this);
                    dynamically_txt6.setText(getString(R.string.filter) + "("  + JArrayList.get(14) + ") : " + JArrayList.get(15).trim() + " ml");
                    dynamically_txt6.setGravity(Gravity.LEFT);
                    dynamically_txt6.setPadding(20, 10, 0, 10);
                    dynamically_txt6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //設置每筆LinearLayout的分隔線
                    TextView dynamically_txt00 = new TextView(StatusActivity.this);
                    dynamically_txt00.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt00.setPadding(10, 0, 10, 0);

                    //設置每筆LinearLayout的分隔線
                    TextView dynamically_txt01 = new TextView(StatusActivity.this);
                    dynamically_txt01.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt01.setPadding(10, 0, 10, 0);

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt0.addView(dynamically_txt0, small_pm);
                    small_llt1.addView(dynamically_txt1, small_pm);
                    small_llt2.addView(dynamically_txt2, small_pm);
                    small_llt3.addView(dynamically_txt3, small_pm);
                    small_llt4.addView(dynamically_txt4, small_pm);
                    small_llt5.addView(dynamically_txt5, small_pm);
                    small_llt6.addView(dynamically_txt6, small_pm);

                    if (JArrayList.get(6).equals("")) {
                        small_llt2.setVisibility(View.GONE);
                    }
                    if (JArrayList.get(8).equals("")) {
                        small_llt3.setVisibility(View.GONE);
                    }
                    if (JArrayList.get(10).equals("")) {
                        small_llt4.setVisibility(View.GONE);
                    }
                    if (JArrayList.get(12).equals("")) {
                        small_llt5.setVisibility(View.GONE);
                    }
                    if (JArrayList.get(14).equals("")) {
                        small_llt6.setVisibility(View.GONE);
                    }
                    status_llt.addView(dynamically_txt00, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    status_llt.addView(small_llt0);
                    status_llt.addView(dynamically_txt01, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    status_llt.addView(small_llt1);
                    status_llt.addView(small_llt2);
                    status_llt.addView(small_llt3);
                    status_llt.addView(small_llt4);
                    status_llt.addView(small_llt5);
                    status_llt.addView(small_llt6);

                    status_llt.getChildAt(0).setVisibility(View.GONE);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
