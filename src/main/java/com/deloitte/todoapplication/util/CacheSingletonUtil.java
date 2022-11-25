package com.deloitte.todoapplication.util;

import java.util.HashMap;
import java.util.Map;

public class CacheSingletonUtil {
    private static volatile CacheSingletonUtil cacheSingletonUtil;
    private static Map<String,Object> cacheSingletonMap;

//    public static final String LOGIN_USER_KEY = "loginUser";

    private CacheSingletonUtil(){
        cacheSingletonMap = new HashMap<String, Object>();
    }

    /*
     * There are two types of singleton patterns
     * Lazy style: create the singleton class object only when you really need to use the object
     * Hungry Chinese style: the singleton object has been created when the class is loaded, waiting to be used by the program
     */

    // lazy singleton pattern
    public static CacheSingletonUtil getInstance(){
        if (cacheSingletonUtil == null){// Thread A and thread B see cacheSingletonUtil = null at the same time, if not null, return cacheSingletonUtil directly
            synchronized (CacheSingletonUtil.class) {// Thread A or Thread B acquires the lock for initialization
                if (cacheSingletonUtil == null) {// One of the threads enters the branch, and the other thread does not enter the branch
                    cacheSingletonUtil = new CacheSingletonUtil();
                }
            }
        }
        return cacheSingletonUtil;
    }

    /**
     * add to memory
     */
    public void addCacheData(String key,Object obj){
        cacheSingletonMap.put(key,obj);
    }

    /**
     * fetch from memory
     */
    public Object getCacheData(String key){
        return cacheSingletonMap.get(key);
    }

    /**
     * clear from memory
     */
    public void removeCacheData(String key){
        cacheSingletonMap.remove(key);
    }
}
