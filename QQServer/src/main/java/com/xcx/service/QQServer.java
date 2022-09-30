package com.xcx.service;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.entity.User;
import com.xcx.thread.ManageServerThread;
import com.xcx.thread.NewServerThread;
import com.xcx.thread.UserServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class QQServer {
    //服务端定义一个hashMap来模拟数据库，存储用户信息(userId,pwd)
    private static ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
    //定义一个map来存放离线的留言或者文件
    private static ConcurrentHashMap<String, ArrayList<Message>> offline = new ConcurrentHashMap<>();
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
            new Thread(new NewServerThread()).start();//执行发送新闻线程
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
                if(map.get(userId) != null && map.get(userId).equals(pwd)) {//验证成功
                    msg.setMsgType(MessageType.LOGIN_SUCCESS);
                    //将message返回给客户端
                    oos.writeObject(msg);

                    //TODO:获取留言信息
                    ArrayList<Message> mesList = QQServer.getMes(userId);
                    if(mesList != null){//先将留言发送给客户端
                        for(Message mes : mesList){
                            ObjectOutputStream toMes = new ObjectOutputStream(socket.getOutputStream());
                            toMes.writeObject(mes);
                        }
                    }
                    //创建一个线程和客户端通信(将socket，userId传进去，表示是哪个客户端与线程在通信)
                    UserServerThread serverThread = new UserServerThread(socket, userId);
                    serverThread.start();
                    //将线程交给容器管理，以便后面与多个客户端通信时创建多个线程
                    ManageServerThread.addThread(userId, serverThread);

                }else{//登录失败
                    System.out.println("用户= "+userId+" 密码= "+pwd+" 登录失败");
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

    //提供一个方法，往集合里面添加离线的用户
    public static void addOffline(String userId, Message msg){
        if(offline.get(userId) != null){
            ArrayList<Message> list = offline.get(userId);
            list.add(msg);
            offline.put(userId,list);
        }else {
            ArrayList<Message> userMes = new ArrayList<>();
            userMes.add(msg);
            offline.put(userId,userMes);
        }

    }

    //在用户刚连接时，判断离线集合里面有没有信息，有就返回
    public static ArrayList<Message> getMes(String userId){
        if(offline.containsKey(userId)){
            return offline.get(userId);
        }
        return null;
    }
}
