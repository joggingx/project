package com.xcx.service;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.entity.User;
import com.xcx.thread.ManageServerThread;
import com.xcx.thread.UserServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class QQServer {
    //服务端定义一个hashMap来模拟数据库，存储用户信息(userId,pwd)
    private static ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
    //初始化用户集合
    static {
        map.put("1001","123");
        map.put("1002","123");
        map.put("张三","123");
        map.put("李四","123");
        map.put("王二","123");
    }
    private ServerSocket serverSocket;
    public QQServer(){
        try {
            System.out.println("服务端在 8888 端口监听。。。。。。");
            //连接客户端
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true){//因为要一直监听，所以死循环
                Socket socket = serverSocket.accept();

                //接受客户端发送过来的数据
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                //判断登录逻辑
                String userId = user.getUserId();
                String pwd = user.getPwd();
                //将处理结果返回给客户端
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message msg = new Message();
                if(map.get(userId) != null && map.get(userId).equals(pwd)){//验证成功
                    msg.setMsgType(MessageType.LOGIN_SUCCESS);
                    //将message返回给客户端
                    oos.writeObject(msg);
                    //创建一个线程和客户端通信(将socket，userId传进去，表示是哪个客户端与线程在通信)
                    UserServerThread serverThread = new UserServerThread(socket,userId);
                    //将线程交给容器管理，以便后面与多个客户端通信时创建多个线程
                    ManageServerThread.addThread(userId,serverThread);
                }else{//登录失败
                    msg.setMsgType(MessageType.LOGIN_FAIL);
                    //失败关闭socket
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //无论如何，客户端与服务端连接关闭之后，通道需要关闭
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
