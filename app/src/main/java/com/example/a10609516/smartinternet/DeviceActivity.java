package com.example.a10609516.smartinternet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceActivity extends WQPToolsActivity {

    private LinearLayout device_llt, device_llt1, device_llt2, device_llt3, device_llt4,
                         device_llt5, device_llt6;
    private ImageView device_imv1, device_imv2, device_imv3, device_imv4, device_imv5,
                      device_imv6;
    private TextView device_txt1, device_txt2, device_txt3, device_txt4, device_txt5,
                     device_txt6, mac_txt1, mac_txt2, mac_txt3, mac_txt4,
                     mac_txt5, mac_txt6, name_txt1, name_txt2;

    private String LOG = "DeviceActivity";
    private String COUNT, device_md5, device_mac, device_name;

    //配合多解析度畫面，會調整Menu的高度
    private int WIDTH, HEIGHT;
    //讀取手機裝置資訊元件
    private DisplayMetrics mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        // 動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
        //獲取手機裝置資訊
        getWindowSize();
        //與OKHttp連線(查詢是否有連線之設備數量)
        sendRequestWithOkHttpForDeviceCount();
        //與OKHttp連線(已連線設備之資料)
        //sendRequestWithOkHttpForDeviceConnect();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction(){
        device_llt = (LinearLayout) findViewById(R.id.device_llt);
        device_llt1 = (LinearLayout) findViewById(R.id.device_llt1);
        device_llt2 = (LinearLayout) findViewById(R.id.device_llt2);
        device_llt3 = (LinearLayout) findViewById(R.id.device_llt3);
        device_llt4 = (LinearLayout) findViewById(R.id.device_llt4);
        device_llt5 = (LinearLayout) findViewById(R.id.device_llt5);
        device_llt6 = (LinearLayout) findViewById(R.id.device_llt6);
        device_imv1 = (ImageView) findViewById(R.id.device_imv1);
        device_imv2 = (ImageView) findViewById(R.id.device_imv2);
        device_imv3 = (ImageView) findViewById(R.id.device_imv3);
        device_imv4 = (ImageView) findViewById(R.id.device_imv4);
        device_imv5 = (ImageView) findViewById(R.id.device_imv5);
        device_imv6 = (ImageView) findViewById(R.id.device_imv6);
        device_txt1 = (TextView) findViewById(R.id.device_txt1);
        device_txt2 = (TextView) findViewById(R.id.device_txt2);
        device_txt3 = (TextView) findViewById(R.id.device_txt3);
        device_txt4 = (TextView) findViewById(R.id.device_txt4);
        device_txt5 = (TextView) findViewById(R.id.device_txt5);
        device_txt6 = (TextView) findViewById(R.id.device_txt6);
        mac_txt1 = (TextView) findViewById(R.id.mac_txt1);
        mac_txt2 = (TextView) findViewById(R.id.mac_txt2);
        mac_txt3 = (TextView) findViewById(R.id.mac_txt3);
        mac_txt4 = (TextView) findViewById(R.id.mac_txt4);
        mac_txt5 = (TextView) findViewById(R.id.mac_txt5);
        mac_txt6 = (TextView) findViewById(R.id.mac_txt6);
        name_txt1 = (TextView) findViewById(R.id.name_txt1);
        name_txt2 = (TextView) findViewById(R.id.name_txt2);
    }

    /**
     *按壓事件監聽器
     */
    private void OnClickListener() {
        device_imv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("0")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt1.getText().toString();
                    String d_mac = mac_txt1.getText().toString();
                    String d_name = name_txt1.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    bundle.putString("d_name", d_name);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });

        device_imv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("1")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt2.getText().toString();
                    String d_mac = mac_txt2.getText().toString();
                    String d_name = name_txt2.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    bundle.putString("d_name", d_name);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });

        device_imv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("2")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt3.getText().toString();
                    String d_mac = mac_txt3.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });

        device_imv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("3")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt4.getText().toString();
                    String d_mac = mac_txt4.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });

        device_imv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("4")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt5.getText().toString();
                    String d_mac = mac_txt5.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });

        device_imv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT.equals("5")) {
                    Intent match_intent = new Intent(DeviceActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                } else{
                    Intent status_intent = new Intent(DeviceActivity.this, StatusActivity.class);
                    //將設備的資料傳到StatusActivity
                    Bundle bundle = new Bundle();
                    String d_md5 = device_txt6.getText().toString();
                    String d_mac = mac_txt6.getText().toString();
                    bundle.putString("d_md5", d_md5);
                    bundle.putString("d_mac", d_mac);
                    status_intent.putExtras(bundle);//可放所有基本類別
                    startActivity(status_intent);
                }
            }
        });
    }

    /**
     * 獲取手機裝置資訊
     */
    private void getWindowSize() {
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        //圖片的寬度
        WIDTH = (int) ((float) mPhone.widthPixels);
        //圖片的高度
        HEIGHT = (int) ((float) mPhone.heightPixels);
    }

    /**
     * 確認設備數量顯示圖片
     */
    private void CheckDeviceCount() {
        if (COUNT.equals("0")){
            Log.e(LOG, "COUNT0:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //device_imv1.setImageResource(R.drawable.home_newdevice_pink);
                }
            });
        } else if (COUNT.equals("1")) {
            Log.e(LOG, "COUNT1:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    device_llt2.setVisibility(View.VISIBLE);
                }
            });
        } else if (COUNT.equals("2")) {
            Log.e(LOG, "COUNT2:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    device_llt2.setVisibility(View.VISIBLE);
                    device_llt3.setVisibility(View.VISIBLE);
                }
            });
        } else if (COUNT.equals("3")) {
            Log.e(LOG, "COUNT3:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    device_llt2.setVisibility(View.VISIBLE);
                    device_llt3.setVisibility(View.VISIBLE);
                    device_llt4.setVisibility(View.VISIBLE);
                }
            });
        } else if (COUNT.equals("4")) {
            Log.e(LOG, "COUNT4:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    device_llt2.setVisibility(View.VISIBLE);
                    device_llt3.setVisibility(View.VISIBLE);
                    device_llt4.setVisibility(View.VISIBLE);
                    device_llt5.setVisibility(View.VISIBLE);
                }
            });
        } else if (COUNT.equals("5")) {
            Log.e(LOG, "COUNT5:" + COUNT);
            DeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    device_llt2.setVisibility(View.VISIBLE);
                    device_llt3.setVisibility(View.VISIBLE);
                    device_llt4.setVisibility(View.VISIBLE);
                    device_llt5.setVisibility(View.VISIBLE);
                    device_llt5.setVisibility(View.VISIBLE);
                }
            });
        } else {

        }


    }

    /**
     * 與OKHttp連線(查詢是否有連線之設備數量)
     */
    private void sendRequestWithOkHttpForDeviceCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences user_email = getSharedPreferences("user_email", MODE_PRIVATE);
                String U_ACC = user_email.getString("ID", "");
                Log.e(LOG, U_ACC);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .build();
                    Log.e(LOG, U_ACC);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DeviceCount.php")
                            //.url("http://192.168.0.172/BWT/DeviceCount.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceCount(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得有連線之設備數量
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDeviceCount(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COUNT = jsonObject.getString("COUNT");
                Log.e(LOG, "DEVICE COUNT:" + COUNT);
                //確認設備數量顯示圖片
                CheckDeviceCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OKHttp連線(已連線設備之資料)
     */
    private void sendRequestWithOkHttpForDeviceConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences user_email = getSharedPreferences("user_email", MODE_PRIVATE);
                String U_ACC = user_email.getString("ID", "");
                Log.e(LOG, U_ACC);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .build();
                    Log.e(LOG, U_ACC);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DeviceConnect.php")
                            //.url("http://192.168.0.172/BWT/DeviceConnect.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceConnect(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得已連線設備之資料
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDeviceConnect(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                device_md5 = jsonObject.getString("device_md5");
                device_mac = jsonObject.getString("device_mac");
                device_name = jsonObject.getString("device_name");
                Log.e(LOG, "DEVICE:" + device_md5 + " - " + device_mac + " - " + device_name);
                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(device_md5);
                JArrayList.add(device_mac);
                JArrayList.add(device_name);

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
                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");


                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 與OKHttp連線(查詢是否已建立帳號)
     */
    /*private void sendRequestWithOkHttpForDeviceData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String device_md5 = "e7e89959d8c052668a3948e8b44c4fe7";
                Log.e(LOG, device_md5);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("device_md5", device_md5)
                            .build();
                    Log.e(LOG, device_md5);
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
                    parseJSONWithJSONObjectForCheckAccount(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    /**
     * 取得是否已建立帳號
     *
     * @param jsonData
     */
   /*private void parseJSONWithJSONObjectForCheckAccount(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.getString("name");
                description = jsonObject.getString("description");
                Log.e(LOG, name.trim() + description.trim());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
