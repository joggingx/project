package com.xcx.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class ManageServerThread {

    //定义一个容器，存放userid与对应的线程
    private static Map<String, UserServerThread> map = new HashMap<>();

    //定义放入线程方法
    public static void addThread(String userId, UserServerThread thread){
        map.put(userId, thread);
    }

    //根据userId获取线程
    public static UserServerThread getThread(String userId){
        return map.get(userId);
    }
    //获取所有用户
    public static String getAllUser() {
        //遍历map即可
        StringBuffer sb = new StringBuffer();
        for (String userId : map.keySet()) {
            sb.append(userId);
            sb.append(" ");

        }
        return sb.toString();
    }

    //移除线程
    public static void removeThread(String userId){
        map.remove(userId);
    }
    //返回所有的线程
    public static Map<String, UserServerThread> getMap(){
        return map;
    }
}
