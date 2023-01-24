package com.deloitte.todoapplication.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocumentUtil {
    public static byte[] convertFileToByteArray(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream  = file.getInputStream();
            return inputStreamToByte(inputStream);  //save the file into a byte array
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Save the file into a byte array
     */
    private static byte [] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bAOutputStream = new ByteArrayOutputStream();
        int ch;
        while((ch = is.read() ) != -1){
            bAOutputStream.write(ch);
        }
        byte data [] =bAOutputStream.toByteArray();
        bAOutputStream.close();
        return data;
    }
}
