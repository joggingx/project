package com.xcx.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class ManageClientThread {//管理客户端的线程
    //用map集合来存放客户端的线程,以userID为key，具体的线程为value存储
    private static Map<String, UserClientThread> map = new HashMap<>();

    //放入线程的方法
    public static void addThread(String userId, UserClientThread thread){
        map.put(userId,thread);
    }

    //通过用户id获取线程对象
    public static UserClientThread getThread(String userId){
        return map.get(userId);
    }
}
