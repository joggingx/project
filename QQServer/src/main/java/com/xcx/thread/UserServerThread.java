package com.xcx.thread;

import com.xcx.entity.Message;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class UserServerThread implements Runnable{

    private Socket socket;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserServerThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public UserServerThread() {
    }

    public UserServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {//与客户端进行通信
        while(true){
            System.out.println("服务端与客户端正在通讯。。。。");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //将结果读出来
                Message msg = (Message) ois.readObject();
                //TODO 后面会使用msg
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
