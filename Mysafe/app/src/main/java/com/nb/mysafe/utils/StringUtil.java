package com.nb.mysafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 侯紫睿
 * @time 2016/5/2 0002  上午 9:37
 * @desc ${TODD}
 */
public class StringUtil {
    public static String readStream(InputStream is){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while((len=is.read(buffer)) != -1){
                 byteArrayOutputStream.write(buffer,0,len);
            }
            is.close();
            return byteArrayOutputStream.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
      return  null;
    }
}
