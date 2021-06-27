package com.example.a10609516.smartinternet;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MatchActivity extends WQPToolsActivity {

    private EditText device_no, device_name;
    private ImageView add_imv, cancel_imv;

    /*private StringBuilder sb = new StringBuilder();
    private Button btn_01,btn_02,btn_03,btn_04,btn_05,btn_06;
    private TextView txt_sb;
    private Spinner spinner;
    private ScrollView scrollView;
    private String[] value = {"0A","01","02","03","04","05","06"};
    private SimpleDateFormat DateFormat_dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat DateFormat_time = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat DateFormat_cmd03 = new SimpleDateFormat("ssmmHH0uddMMyy");
    private SimpleDateFormat DateFormat_cmd04 = new SimpleDateFormat("ssmmHHddMMyy");
    *//**********BLE**********//*
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
    *//********** MCU **********//*
    private String MCU_ID=null;
    private String MCU_PWD=null;
    private boolean MCU_PAIR=false,set_char=false;
    private ProgressDialog dialog_pair;*/

    private String LOG = "MatchActivity";

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        // 動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
        /********** CHECK LOCATION BLE **********/
        /*Scansetting_init();
        check_location_coarse();
*/
        //layout_init();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction() {
        device_no = (EditText) findViewById(R.id.device_no);
        device_name = (EditText) findViewById(R.id.device_name);
        add_imv = (ImageView) findViewById(R.id.add_imv);
        cancel_imv = (ImageView) findViewById(R.id.cancel_imv);
    }

    /**
     * 自動變更圖片適中大小
     */
    private void ResizeIcon() {
        Drawable drawable1 = getResources().getDrawable(R.drawable.icon_id);
        drawable1.setBounds(0, 0, device_no.getHeight(), device_no.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        device_no.setCompoundDrawables(drawable1, null, null, null);//只放左邊
        Drawable drawable2 = getResources().getDrawable(R.drawable.icon_setup);
        drawable2.setBounds(0, 0, device_name.getHeight(), device_name.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        device_name.setCompoundDrawables(drawable2, null, null, null);//只放左邊
    }

    /**
     * 按壓事件監聽器
     */
    private void OnClickListener() {

        add_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //與OKHttp連線(配對使用者與設備的連線)
                sendRequestWithOkHttpForDeviceMatch();
                finish();
            }
        });

        cancel_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 與OKHttp連線(配對使用者與設備的連線)
     */
    private void sendRequestWithOkHttpForDeviceMatch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences user_email = getSharedPreferences("user_email", MODE_PRIVATE);
                String U_ACC = user_email.getString("ID", "");
                String device_mac = device_no.getText().toString();
                String device_id = device_name.getText().toString();

                Log.e(LOG, U_ACC + " - " + device_mac + " - " + device_id);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .add("device_mac", device_mac)
                            .add("device_id", device_id)
                            .build();
                    Log.e(LOG, device_mac + " - " + device_id);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/BWT/DeviceMatch.php")
                            //.url("http://192.168.0.172/BWT/DeviceMatch.php")
                            .post(requestBody)
                            .build();
                    Log.e(LOG, requestBody.toString());
                    Response response = client.newCall(request).execute();
                    Log.e(LOG, response.toString());
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
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

    /********** BLE  **********/
    /*@RequiresApi(api = Build.VERSION_CODES.M)
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String deviceName = result.getScanRecord().getDeviceName();
            String deviceMac = result.getDevice().getAddress();
            addlog("掃描到："+deviceName+"("+deviceMac+")");
            mbluetoothLeScanner.stopScan(scanCallback);
            if("BWTBLE".equals(deviceName)){
                Log.e("Scan",deviceMac);
                if (null!=mbluetoothAdapter.getRemoteDevice(deviceMac)) {  //判断是否已经添加
                    result.getDevice().connectGatt(MatchActivity.this, true,bluetoothGattCallback);
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
            *//******************** 確認哪一事件********************//*
            switch (CMD) {
                *//******************** 0x01 詢問Serial Number ********************//*
                case "01" : //成功
                    MCU_ID = ble.Bytes2HexString(ID);
                    Log.e("TAG","ID："+MCU_ID);
                    addlog("ID:"+MCU_ID);
                    break;
                *//******************** 0x02 配對 ********************//*
                case "02" : //通知APP配對
                    if(MCU_PAIR){
                        MCU_PWD = ble.randomPWD();
                        Log.e("TAG","ID ："+MCU_ID+"　　PWD:"+MCU_PWD);
                        senddata = "E102"+MCU_ID+MCU_PWD;
                        senddata = senddata+ble.crc(senddata)+"D7";
                        SendtoBLE(ble.HexString2Bytes(senddata));
                        addlog("開始配對\nID:"+MCU_ID+" PWD:"+MCU_PWD);

                        SharedPreferences ID_sharedPreferences = getSharedPreferences("MCU_ID", MODE_PRIVATE);
                        ID_sharedPreferences.edit().putString("ID", MCU_ID).apply();

                        SharedPreferences PWD_sharedPreferences = getSharedPreferences("MCU_PWD", MODE_PRIVATE);
                        PWD_sharedPreferences.edit().putString("PWD", MCU_PWD).apply();

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
                *//******************** 0x03 校正時間 ********************//*
                case "A3" : //成功
                    addlog("CMDA3 校正時間 成功");
                    break;
                case "B3" : //失敗
                    addlog("校正時間 失敗");
                    break;
                *//******************** 0x04 讀取時間 ********************//*
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
                *//******************** 0x05 讀取DATA ********************//*
                case "A5" : //成功
                    byte[] flow = {input[8],input[9]};
                    Integer int_flow = ble.Bytes2Int(flow);
                    addlog("CMDA5 讀取DATA "+ble.Bytes2HexString(data)+" 成功 "+Integer.toString(int_flow)+" L");
                    break;
                case "B5" : //失敗
                    addlog("CMDB5 讀取DATA "+ble.Bytes2HexString(data)+" 失敗");
                    break;
                *//******************** 0x06 重置DATA ********************//*
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

    *//********** init  **********//*
    public void layout_init(View view){

    }

    *//********** button listener **********//*
    private Button.OnClickListener btn_01_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendtoBLE(ble.HexString2Bytes("E101E2D7"));
            addlog("CMD01 詢問Serial Number");

            btn_02.setEnabled(true);
        }
    };

    private Button.OnClickListener btn_02_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog_pair();
        }
    };

    private void showDialog_pair() {
        MCU_PAIR=true;
        addlog("CMD02 等待配對........");
        dialog_pair = new ProgressDialog(MatchActivity.this);
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

    *//********** CHECK LOCATION BLE  **********//*
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_location_coarse() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(MatchActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            addlog("請求 ACCESS_COARSE_LOCATION權限");
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }else{
            addlog("取得 ACCESS_COARSE_LOCATION權限");
            check_location_fine();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_location_fine() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(MatchActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            addlog("請求 ACCESS_FINE_LOCATION權限");
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},102);
        }else{
            addlog("取得 ACCESS_FINE_LOCATION權限");
            check_ble();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_ble() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(MatchActivity.this, android.Manifest.permission.BLUETOOTH)){
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH},201);
        }else{
            addlog("取得 BLUETOOTH權限");
            check_ble_admin();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_ble_admin() {
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(MatchActivity.this, android.Manifest.permission.BLUETOOTH_ADMIN)){
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN},202);
        }else{
            addlog("取得 BLUETOOTH_ADMIN權限");
            check_openlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_openlocation() {
        locationManager = (LocationManager) MatchActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            addlog("請求 開啟定位");
            Toast.makeText(MatchActivity.this,"請開啟 定位功能!",Toast.LENGTH_SHORT);
            Intent enableLOCATIONIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLOCATIONIntent, 301);
        }else{
            addlog("開啟定位 成功");
            check_openble();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void check_openble() {
        mbluetoothManager = (BluetoothManager) MatchActivity.this.getSystemService(Context.BLUETOOTH_SERVICE);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    *//********** addlog **********//*
    public void addlog(final String content){
        MatchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date(System.currentTimeMillis());
                String T = DateFormat_time.format(date);
                sb.append(T+"："+content+"\n");
                txt_sb.setText(sb.toString());
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }*/
}
