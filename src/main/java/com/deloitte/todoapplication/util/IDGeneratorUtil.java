package com.deloitte.todoapplication.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class IDGeneratorUtil {
    public static String genPrimaryKey(){
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a 5-digit random number
     */
    public static String getMessageID(){
        StringBuilder str = new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<5;i++){
            int num = rand.nextInt(3);
            switch(num){
                case 0:
                    char c1 = (char)(rand.nextInt(26)+'a');//生成随机小写字母
                    str.append(c1);
                    break;
                case 1:
                    char c2 = (char)(rand.nextInt(26)+'A');//生成随机大写字母
                    str.append(c2);
                    break;
                case 2:
                    str.append(rand.nextInt(10));//生成随机数字
            }
        }
        return str.toString();
    }

    /**
     * Generate a 2-digit random number
     */
    public static String getRandomNumber(){
        Random random = new Random();
        int i = random.nextInt(100);
        return String.format("%02d",i);
    }

    /**
     * Generate time series random number
     */
    public static synchronized String getEventId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date()) + getRandomNumber();
    }
}
