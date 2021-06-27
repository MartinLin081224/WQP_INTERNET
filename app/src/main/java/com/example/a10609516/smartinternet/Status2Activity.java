package com.example.a10609516.smartinternet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;
import com.example.a10609516.smartinternet.Tools.ble;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class Status2Activity extends WQPToolsActivity {

    private LinearLayout status_llt, status_all_llt, value_llt1, value_llt2, value_llt3,
            value_llt4, value_llt5, value_llt6, value_llt;
    private TextView name_txt, id_txt, description_txt, filter_txt1, filter_txt2,
            filter_txt3, filter_txt4, filter_txt5, filter_txt6, value_txt1,
            value_txt2, value_txt3, value_txt4, value_txt5, value_txt6,
            value_total_txt1, value_total_txt2, value_total_txt3, value_total_txt4, value_total_txt5,
            value_total_txt6, zero;
    private ProgressBar value_pgr1, value_pgr2, value_pgr3, value_pgr4, value_pgr5, value_pgr6;

    private String LOG = "Status2Activity";
    private String name, description, MAC, MD5, MODEL,
            D_NAME, FILTER01, DATA01, FILTER02, DATA02,
            FILTER03, DATA03, FILTER04, DATA04, FILTER05,
            DATA05, FILTER06, DATA06, DT, S_DATA01,
            S_DATA02, S_DATA03, S_DATA04, S_DATA05, S_DATA06,
            VALUE01, VALUE02, VALUE03, VALUE04, VALUE05, VALUE06;

    private StringBuilder sb = new StringBuilder();
    private Button btn_01,btn_02,btn_03,btn_04,btn_05,btn_06;
    private TextView txt_sb;
    private Spinner spinner;
    private ScrollView scrollView;
    private String[] value = {"0A","01","02","03","04","05","06"};
    private SimpleDateFormat DateFormat_dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat DateFormat_time = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat DateFormat_cmd03 = new SimpleDateFormat("ssmmHH0uddMMyy");
    private SimpleDateFormat DateFormat_cmd04 = new SimpleDateFormat("ssmmHHddMMyy");
    /**********BLE**********/
    private UUID SERVICE_UUID = UUID.fromString("00000ffe0-0000-1000-8000-00805f9b34fb");
    private UUID WCHAR_UUID = UUID.fromString("00000ffe1-0000-1000-8000-00805f9b34fb");
    private UUID RCHAR_UUID = UUID.fromString("00000ffe1-0000-1000-8000-00805f9b34fb");
    private UUID Descriptor_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private LocationManager locationManager = null;
    private BluetoothManager mbluetoothManager = null;
    private BluetoothAdapter mbluetoothAdapter = null;
    private BluetoothLeScanner mbluetoothLeScanner = null;
    private BluetoothGatt bleGatt = null;
    private List<ScanFilter> filters;
    private ScanSettings setting;
    /********** MCU **********/
    private String MCU_ID=null;
    private String MCU_PWD=null;
    private boolean MCU_PAIR=false,set_char=false;
    private ProgressDialog dialog_pair;
    private Integer int_flow;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status2);

        //動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();

        /********** CHECK LOCATION BLE **********/
        Scansetting_init();
        check_location_coarse();

    }

    //動態取得 View 物件
    private void  InItFunction(){
        value_llt =  (LinearLayout) findViewById(R.id.value_llt);

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

        btn_01 = (Button) findViewById(R.id.btn_01);
        btn_02 = (Button) findViewById(R.id.btn_02);
        btn_03 = (Button) findViewById(R.id.btn_03);
        btn_04 = (Button) findViewById(R.id.btn_04);
        btn_05 = (Button) findViewById(R.id.btn_05);
        btn_06 = (Button) findViewById(R.id.btn_06);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        txt_sb = (TextView) findViewById(R.id.txt_sb);

        zero = (TextView) findViewById(R.id.zero);

        id_txt.setText("BWT 淨水器");

        SharedPreferences MCU = getSharedPreferences("MCU", MODE_PRIVATE);
        String P_MCU_ID = MCU.getString("P_MCU_ID", "");
        String P_MCU_PWD = MCU.getString("P_MCU_PWD", "");
        Log.e(LOG, "TEST" + P_MCU_ID + "    " + P_MCU_PWD);

        if (P_MCU_ID != "") {
            MCU_ID = MCU.getString("P_MCU_ID", "");
            MCU_PWD = MCU.getString("P_MCU_PWD", "");
        }
    }

    /**
     * 按壓事件監聽器
     */
    private void OnClickListener() {
        btn_01.setOnClickListener(btn_01_listener);
        btn_02.setOnClickListener(btn_02_listener);
        btn_03.setOnClickListener(btn_03_listener);
        btn_04.setOnClickListener(btn_04_listener);
        btn_05.setOnClickListener(btn_05_listener);
        btn_06.setOnClickListener(btn_06_listener);
    }

    /********** button listener **********/
    private Button.OnClickListener btn_01_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendtoBLE(ble.HexString2Bytes("E101E2D7"));
            addlog("CMD01 詢問Serial Number");
        }
    };

    private Button.OnClickListener btn_02_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog_pair();
        }
    };

    private Button.OnClickListener btn_03_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date date = new Date(System.currentTimeMillis());
            String DT = DateFormat_cmd03.format(date);
            String dt = DateFormat_dt.format(date);
            String senddata = "E103"+MCU_ID+MCU_PWD+DT;
            senddata = senddata+ble.crc(senddata)+"D7";
            SendtoBLE(ble.HexString2Bytes(senddata));
            addlog("CMD03 校正時間:"+dt);
        }
    };

    private Button.OnClickListener btn_04_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String senddata = "E104"+MCU_ID+MCU_PWD;
            senddata = senddata+ble.crc(senddata)+"D7";
            SendtoBLE(ble.HexString2Bytes(senddata));
            addlog("CMD04 讀取時間........");
        }
    };

    private Button.OnClickListener btn_05_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog_DataList5();
        }
    };

    private Button.OnClickListener btn_06_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog_DataList6();
        }
    };

    private void showDialog_pair() {
        MCU_PAIR=true;
        addlog("CMD02 等待配對........");
        dialog_pair = new ProgressDialog(Status2Activity.this);
        dialog_pair.setTitle("啟動配對模式");
        dialog_pair.setMessage("請按壓 配對按鈕 一下");
        dialog_pair.setIndeterminate(true);
        dialog_pair.setCancelable(true);
        dialog_pair.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MCU_PAIR=false;
            }
        });
        dialog_pair.show();
    }

    private void showDialog_DataList5() {
        addlog("CMD05 選擇要查詢的Data");
        final String[] items = {"DataALL","Data01","Data02","Data03","Data04","Data05","Data06"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(Status2Activity.this);
        listDialog.setTitle("選擇要查詢的Data");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(0==which){
                    addlog("CMD05 請用循環方式一個一個問");

                    for (int x = 1; x < 7; x++) {
                        String senddata = "E105"+MCU_ID+MCU_PWD+"0"+x;
                        senddata = senddata+ble.crc(senddata)+"D7";
                        SendtoBLE(ble.HexString2Bytes(senddata));
                        addlog("CMD05 查詢 "+"0"+x+" 的Data");

                        try{
                            // delay 1 second
                            Thread.sleep(800);

                        } catch(InterruptedException e){
                            e.printStackTrace();

                        }

                    }

                    value_llt.setVisibility(View.VISIBLE);

                    Status2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            filter_txt1.setText("濾芯1");
                            filter_txt2.setText("濾芯2");
                            filter_txt3.setText("濾芯3");
                            filter_txt4.setText("濾芯4");
                            filter_txt5.setText("濾芯5");
                            filter_txt6.setText("濾芯6");
                            value_txt1.setText(17 + " L ");
                            value_txt2.setText(34 + " L ");
                            value_txt3.setText(51 + " L ");
                            value_txt4.setText(68 + " L ");
                            value_txt5.setText(257 + " L ");
                            value_txt6.setText(4386 + " L ");
                            value_total_txt1.setText(800 + " L ");
                            value_total_txt2.setText(4000 + " L ");
                            value_total_txt3.setText(1200 + " L ");
                            value_total_txt4.setText(4000 + " L ");
                            value_total_txt5.setText(8000 + " L ");
                            value_total_txt6.setText(12000 + " L ");
                            value_pgr1.setMax(50);
                            value_pgr2.setMax(100);
                            value_pgr3.setMax(100);
                            value_pgr4.setMax(300);
                            value_pgr5.setMax(500);
                            value_pgr6.setMax(6000);
                            value_pgr1.setProgress(17);
                            value_pgr2.setProgress(34);
                            value_pgr3.setProgress(51);
                            value_pgr4.setProgress(68);
                            value_pgr5.setProgress(257);
                            value_pgr6.setProgress(4386);
                        }
                    });

                }else{
                    String data = value[which];
                    String senddata = "E105"+MCU_ID+MCU_PWD+data;
                    senddata = senddata+ble.crc(senddata)+"D7";
                    SendtoBLE(ble.HexString2Bytes(senddata));
                    addlog("CMD05 查詢 "+data+" 的Data");
                }
            }
        });
        listDialog.show();
    }

    private void showDialog_DataList6() {
        addlog("CMD06 選擇要重置的Data");
        final String[] items = {"DataALL","Data01","Data02","Data03","Data04","Data05","Data06"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(Status2Activity.this);
        listDialog.setTitle("選擇要重置的Data");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = value[which];
                String senddata = "E106"+MCU_ID+MCU_PWD+data;
                senddata = senddata+ble.crc(senddata)+"D7";
                SendtoBLE(ble.HexString2Bytes(senddata));
                addlog("CMD06 重置 "+data+" 的Data");

                Status2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        filter_txt1.setText("濾芯1");
                        filter_txt2.setText("濾芯2");
                        filter_txt3.setText("濾芯3");
                        filter_txt4.setText("濾芯4");
                        filter_txt5.setText("濾芯5");
                        filter_txt6.setText("濾芯6");
                        value_txt1.setText(0 + " L ");
                        value_txt2.setText(0 + " L ");
                        value_txt3.setText(0 + " L ");
                        value_txt4.setText(0 + " L ");
                        value_txt5.setText(0 + " L ");
                        value_txt6.setText(0 + " L ");
                        value_total_txt1.setText(800 + " L ");
                        value_total_txt2.setText(4000 + " L ");
                        value_total_txt3.setText(1200 + " L ");
                        value_total_txt4.setText(4000 + " L ");
                        value_total_txt5.setText(8000 + " L ");
                        value_total_txt6.setText(12000 + " L ");
                        value_pgr1.setMax(50);
                        value_pgr2.setMax(100);
                        value_pgr3.setMax(100);
                        value_pgr4.setMax(300);
                        value_pgr5.setMax(500);
                        value_pgr6.setMax(6000);
                        value_pgr1.setProgress(0);
                        value_pgr2.setProgress(0);
                        value_pgr3.setProgress(0);
                        value_pgr4.setProgress(0);
                        value_pgr5.setProgress(0);
                        value_pgr6.setProgress(0);
                    }
                });
            }
        });
        listDialog.show();
    }

    /********** BLE  **********/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private ScanCallback scanCallback=new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String deviceName = result.getScanRecord().getDeviceName();
            String deviceMac = result.getDevice().getAddress();
            addlog("掃描到："+deviceName+"("+deviceMac+")");
            mbluetoothLeScanner.stopScan(scanCallback);
            if("BWTBLE".equals(deviceName)){
                Log.e("Scan",deviceMac);
                if (null!=mbluetoothAdapter.getRemoteDevice(deviceMac)) {  //判断是否已经添加
                    result.getDevice().connectGatt(Status2Activity.this, true,bluetoothGattCallback);
                    addlog("準備連接："+deviceName+"("+deviceMac+")");
                }
            }else{
                mbluetoothLeScanner.startScan(filters,setting,scanCallback);
            }
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e("Scan","onBatchScanResults");
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("Scan","onScanFailed");
        }
    };

    private BluetoothGattCallback bluetoothGattCallback= new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            final String device = gatt.getDevice().getName()+"("+gatt.getDevice().getAddress()+")";
            if (newState == BluetoothProfile.STATE_CONNECTED && !mbluetoothAdapter.isDiscovering() && gatt!=bleGatt) {
                gatt.discoverServices();//連接成功，開始搜索服務
                Log.e("Discovered", "開始discoverServices："+device);
                //addlog("Discovered設定開始：" +device);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                gatt.close();//斷線
                bleGatt=null;set_char=false;
                Log.e("Discovered","段開連線："+device);
                addlog("Discovered段開連線：" +device);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            final String device = gatt.getDevice().getName()+"("+gatt.getDevice().getAddress()+")";
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("Discovered", "結束 discoverServices：" +device);
                Log.e("Rcharacteristic", "開始設定：" +device);
                addlog("Discovered設定結束：" +device);
                addlog("Rcharacteristic開始設定：" +device);
                BluetoothGattCharacteristic Rchar = gatt.getService(SERVICE_UUID).getCharacteristic(RCHAR_UUID);
                if (Rchar != null) {
                    Rchar.setWriteType(BluetoothGattCharacteristic.PROPERTY_NOTIFY);
                    BluetoothGattDescriptor clientConfig = Rchar.getDescriptor(Descriptor_UUID);
                    clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    if (gatt.setCharacteristicNotification(Rchar, true)) {
                        bleGatt=gatt;
                        if(set_char==false){
                            set_char=true;
                            gatt.writeDescriptor(clientConfig);
                            Log.e("Rcharacteristic", "設定成功：" +device);
                            addlog("Rcharacteristic設定成功：" +device);
                        }
                    }
                } else {
                    Log.e("Rcharacteristic", "設定失敗：" +device);
                    addlog("Rcharacteristic設定失敗：" +device);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String device = gatt.getDevice().getName()+"("+gatt.getDevice().getAddress()+")";
            Log.e("TAG",device + " recieved ");
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String device = gatt.getDevice().getName()+"("+gatt.getDevice().getAddress()+")";
            byte[] input  = characteristic.getValue();
            Log.e("WRITE",device + "：" + ble.Bytes2HexString(input));
            //addlog(device+" write：\n"+ble.Bytes2HexString(input));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String device = gatt.getDevice().getName()+"("+gatt.getDevice().getAddress()+")";
            byte[] input  = characteristic.getValue();
            Log.e("READ",device + "：" + ble.Bytes2HexString(input));
            //addlog(device+" read：\n"+ble.Bytes2HexString(input));

            if(input.length<=7){
                return;
            }
            final byte[] start = {input[0]};
            final byte[] cmd = {input[1]};
            final String CMD = ble.Bytes2HexString(cmd);
            byte[] ID = new byte[5];
            for(int i=0;i<5;i++){
                ID[i]=input[i+2];
            }
            byte[] data = {input[7]};
            if(start.equals("0xD3")){
                return;
            }
            String senddata="",crc="";
            /******************** 確認哪一事件********************/
            switch (CMD) {
                /******************** 0x01 詢問Serial Number ********************/
                case "01" : //成功
                    MCU_ID = ble.Bytes2HexString(ID);
                    Log.e("TAG","ID："+MCU_ID);
                    addlog("ID:"+MCU_ID);
                    break;
                /******************** 0x02 配對 ********************/
                case "02" : //通知APP配對
                    if(MCU_PAIR){
                        MCU_PWD = ble.randomPWD();
                        Log.e("TAG","ID ："+MCU_ID+"　　PWD:"+MCU_PWD);
                        senddata = "E102"+MCU_ID+MCU_PWD;
                        senddata = senddata+ble.crc(senddata)+"D7";
                        SendtoBLE(ble.HexString2Bytes(senddata));
                        addlog("開始配對\nID:"+MCU_ID+" PWD:"+MCU_PWD);

                        SharedPreferences pref = getSharedPreferences("MCU", MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("P_MCU_ID", MCU_ID);
                        edit.putString("P_MCU_PWD", MCU_PWD);
                        edit.commit();
                    }
                    break;
                case "A2" : //成功
                    addlog("CMDA2 配對 成功");
                    dialog_pair.dismiss();
                    break;
                case "B2" : //失敗
                    addlog("CMDB2 配對 失敗");
                    dialog_pair.dismiss();
                    break;
                /******************** 0x03 校正時間 ********************/
                case "A3" : //成功
                    addlog("CMDA3 校正時間 成功");
                    break;
                case "B3" : //失敗
                    addlog("校正時間 失敗");
                    break;
                /******************** 0x04 讀取時間 ********************/
                case "A4" : //成功
                    byte[] nowDT={input[7],input[8],input[9],input[11],input[12],input[13]};

                    String aaa= ble.Bytes2HexString(nowDT);
                    try {
                        Date kk = DateFormat_cmd04.parse(aaa);
                        aaa = DateFormat_dt.format(kk);
                        addlog("CMD04 讀取時間:"+aaa);
                    }catch (Exception e){
                        addlog("CMD04 讀取時間 失敗");
                    }
                    //addlog("CMD04 讀取時間:"+ble.Bytes2HexString(nowDT));
                    break;
                case "B4" : //失敗
                    addlog("CMD04 讀取時間 失敗");
                    break;
                /******************** 0x05 讀取DATA ********************/
                case "A5" : //成功
                    byte[] flow = {input[8],input[9]};
                    int_flow = ble.Bytes2Int(flow);
                    addlog("CMDA5 讀取DATA "+ble.Bytes2HexString(data)+" 成功 "+Integer.toString(int_flow)+" L");

                    Status2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    break;
                case "B5" : //失敗
                    addlog("CMDB5 讀取DATA "+ble.Bytes2HexString(data)+" 失敗");
                    break;
                /******************** 0x06 重置DATA ********************/
                case "A6" : //成功
                    addlog("CMDA6 重置DATA "+ble.Bytes2HexString(data)+" 成功");
                    break;
                case "B6" : //失敗
                    addlog("CMDB6 重置DATA "+ble.Bytes2HexString(data)+" 失敗");
                    break;
                default:
                    Log.e("READ",gatt.getDevice().getAddress() + " NO CASE");
            }
        }

    };

    public void SendtoBLE(byte[] arr){
        if(null==bleGatt){
            Log.e("SEND","還沒抓到設備");
            return;
        }
        try{
            BluetoothGattService service = bleGatt.getService(SERVICE_UUID);
            BluetoothGattCharacteristic Wchar = service.getCharacteristic(WCHAR_UUID);
            if (Wchar !=null) {
                Wchar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                Wchar.setValue(arr);
                bleGatt.writeCharacteristic(Wchar);//送出
                return;
            }
        }catch(Exception e){
            return;
        }
    }

    /********** init  **********/
    public void layout_init(View view){

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Scansetting_init(){
        filters = new ArrayList<>();
        ScanFilter filter1 = new ScanFilter.Builder()
                .setServiceUuid(ParcelUuid.fromString("00000ffe0-0000-1000-8000-00805f9b34fb"))
                .build();
        filters.add(filter1);

        setting = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    /********** CHECK LOCATION BLE  **********/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_location_coarse() {
        if(PackageManager.PERMISSION_GRANTED!= ContextCompat.checkSelfPermission(Status2Activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            addlog("請求 ACCESS_COARSE_LOCATION權限");
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }else{
            addlog("取得 ACCESS_COARSE_LOCATION權限");
            check_location_fine();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_location_fine() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(Status2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            addlog("請求 ACCESS_FINE_LOCATION權限");
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},102);
        }else{
            addlog("取得 ACCESS_FINE_LOCATION權限");
            check_ble();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_ble() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(Status2Activity.this, android.Manifest.permission.BLUETOOTH)){
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH},201);
        }else{
            addlog("取得 BLUETOOTH權限");
            check_ble_admin();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_ble_admin() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(Status2Activity.this, android.Manifest.permission.BLUETOOTH_ADMIN)){
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN},202);
        }else{
            addlog("取得 BLUETOOTH_ADMIN權限");
            check_openlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_openlocation() {
        locationManager = (LocationManager) Status2Activity.this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            addlog("請求 開啟定位");
            Toast.makeText(Status2Activity.this,"請開啟 定位功能!",Toast.LENGTH_SHORT);
            Intent enableLOCATIONIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLOCATIONIntent, 301);
        }else{
            addlog("開啟定位 成功");
            check_openble();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_openble() {
        mbluetoothManager = (BluetoothManager) Status2Activity.this.getSystemService(Context.BLUETOOTH_SERVICE);
        mbluetoothAdapter = mbluetoothManager.getAdapter();
        if(!mbluetoothAdapter.isEnabled()){
            addlog("請求 開啟藍芽");
            Intent enableBLEIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBLEIntent, 302);
        }else{
            addlog("開啟藍芽 成功");
            mbluetoothLeScanner=mbluetoothAdapter.getBluetoothLeScanner();
            mbluetoothLeScanner.stopScan(scanCallback);
            mbluetoothLeScanner.startScan(filters,setting,scanCallback);
            addlog("開始掃描......");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:{
                check_location_coarse();
                return;
            }
            case 102:{
                check_location_fine();
                return;
            }
            case 201:{
                check_ble();
                return;
            }
            case 202:{
                check_ble_admin();
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 301:{
                check_openlocation();
                return;
            }
            case 302:{
                check_openble();
                return;
            }
        };
    }

    /********** addlog **********/
    public void addlog(final String content){
        Status2Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date(System.currentTimeMillis());
                String T = DateFormat_time.format(date);
                sb.append(T+"："+content+"\n");
                txt_sb.setText(sb.toString());
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Status2Activity", "onDestroy");
        mbluetoothManager = null;
        bleGatt.disconnect();
    }

}
