package com.xcx.thread;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

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
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //读取返回过来的信息
                Message msg = (Message) ois.readObject();//如果服务端没有数据发过来，线程会阻塞在这里

                //在客户端对服务端返回过来的信息进行相应的业务处理
                if(MessageType.SET_OLINE_LIST.equals(msg.getMsgType())){//获取在线用户列表
                    //服务端将用户列表信息封装成字符串，写在信息内容里面
                    String content = msg.getContent();
                    //规定内容以空格隔开
                    String[] onlineList = content.split(" ");
                    System.out.println("当前在线用户列表========");
                    for(String user : onlineList){
                        System.out.println("用户："+ user);
                    }
                }else if(MessageType.COMM_EMS.equals(msg.getMsgType())){//私聊消息

                    System.out.println(msg.getSendTime()+": "+msg.getSender()+" 对 "+msg.getReceiver()+" 说:"+msg.getContent());
                }else if(MessageType.GROUP_CHAT.equals(msg.getMsgType())){//群聊消息

                    System.out.println(msg.getSendTime()+": "+msg.getSender()+" 对大家说："+msg.getContent());
                }else if(MessageType.FILE_SEND.equals(msg.getMsgType())){
                    System.out.println(msg.getSender()+" 给 "+msg.getReceiver()+" 发送 "+msg.getFileSrc()+" 文件 "+" 到 "+msg.getFileDest());
                    //将文件写入到客户端本地磁盘上
                    FileOutputStream fos = new FileOutputStream(msg.getFileDest());
                    fos.write(msg.getFileBytes());
                    //写完之后关闭文件输出流
                    fos.close();
                }
                else{
                    System.out.println("其他信息暂时不做处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
