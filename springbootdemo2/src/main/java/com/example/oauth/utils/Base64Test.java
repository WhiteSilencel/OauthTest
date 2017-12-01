package com.example.oauth.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by LiMengXi on 2017/11/28.
 */
public class Base64Test {
    public String stringToBase64(String str){
        BASE64Encoder encoder=new BASE64Encoder();
        String encoded=encoder.encode(str.getBytes());
        return encoded;
    }
    public String base64ToString(String str){
        BASE64Decoder decoder=new BASE64Decoder();
        try {
            byte[] decoded=decoder.decodeBuffer(str);
            return new String(decoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
