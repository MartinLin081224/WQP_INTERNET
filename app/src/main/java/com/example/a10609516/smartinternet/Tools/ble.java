package com.example.a10609516.smartinternet.Tools;

import java.util.Locale;

public class ble {

    //Byte轉16進制String
    public static String Bytes2HexString(byte[] bytes) {
        String result = "";
        for (byte item : bytes) {
            String hex = Integer.toHexString(item & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result += hex.toUpperCase(Locale.CHINA);
        }
        return result;
    }

    //16進制String轉Byte
    public static byte[] HexString2Bytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    //16進制Byte轉int
    public static int Bytes2Int(byte[] b) {
        return   b[1] & 0xFF |
                (b[0] & 0xFF) << 8;
    }

    public static String randomPWD(){
        String result="";
        int a = (int) (Math.random() *90000000 + 10000000);
        result = Integer.toString(a);
        return result;
    }

    public static String randomPWD8(){
        String result="";
        for(int i=0;i<8;i++){
            int a = (int) (Math.random() *9 + 1)*1;
            result += Integer.toString(a);
        }
        return result;
    }

    public synchronized static String crc(String data){
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0,num = 0,len = data.length();
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /*** 用256求余最大是255，即16進制0xFF ***/
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 長度不夠，補0
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

}

