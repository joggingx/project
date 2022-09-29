package com.xcx.thread;

import com.xcx.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class UserClientThread extends Thread{
    private Socket socket;//线程需要持有socket来进行通信

    public UserClientThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {//因为线程需要和服务器通信，所以用while一直通信
        while (true){
            System.out.println("客户端，等待服务端发送过来的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //读取返回过来的信息
                Message msg = (Message) ois.readObject();//如果服务端没有数据发过来，线程会阻塞在这里
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
