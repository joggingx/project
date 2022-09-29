package com.xcx.service;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.entity.User;
import com.xcx.thread.ManageClientThread;
import com.xcx.thread.UserClientThread;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class UserClient {
    //提供对用户的操作，完成登录验证注册等等

    //因为可能在其他地方使用User信息
    private User user = new User();
    private boolean flag = false;
    private Socket socket;
    public boolean checkUser(String userId, String pwd){
        //创建User对象
        user.setUserId(userId);
        user.setPwd(pwd);

        //连接到服务端，发送信息，进行判断
        try {
            socket = new Socket(InetAddress.getByName("10.73.149.182"),8888);
            //得到对象输出流，因为是以对象的形式写出去的
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //写出去
            oos.writeObject(user);

            //接受客户端返回回来的信息(Message对象)
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();
            //通过返回回来的msg对象来判断是否登录成功
            if(MessageType.LOGIN_SUCCESS.equals(msg.getMsgType())){//登录成功
                //登录成功之后需要单独创建一个线程与服务器端进行通信，不能影响主线程
                //创建线程，传入socket，表名该线程处理的是这个客户端socket的连接
                UserClientThread userClientThread = new UserClientThread(socket);
                //启动客户端线程
                userClientThread.start();
                //为了方便扩展，将线程放入集合中
                ManageClientThread.addThread(userId,userClientThread);
                flag = true;
            }else{
                //登录失败，只需要关闭socket就行了
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
