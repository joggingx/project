package com.xcx.thread;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.service.QQServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class UserServerThread extends Thread{

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
            System.out.println("服务端与客户端 "+userId+" 正在通讯。。。。");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //将结果读出来
                Message msg = (Message) ois.readObject();
                //TODO 后面会使用msg
                //开始通讯，接受客户端的消息
                if(MessageType.GET_OLINE_LIST.equals(msg.getMsgType())){//得到了一个需要用户列表的消息
                    //从管理线程集合的容器中获取与该客户端关联的线程
                    String allUser = ManageServerThread.getAllUser();
                    //将用户列表加到返回到消息中
                    System.out.println("用户："+msg.getSender()+"拉取在线用户列表");
                    Message message = new Message();
                    message.setContent(allUser);
                    message.setMsgType(MessageType.SET_OLINE_LIST);
                    message.setReceiver(msg.getSender());

                    //消息封装完毕，返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }else if(MessageType.CLIENT_EXIT.equals(msg.getMsgType())){//读取到了客户端退出的线程
                    //接收到退出的消息，要把socket关闭，将线程从线程池中移除
                    ManageServerThread.removeThread(userId);
                    System.out.println("客户端用户"+msg.getSender()+"正在退出。。。");
                    socket.close();
                    break;//退出循环
                }else if(MessageType.COMM_EMS.equals(msg.getMsgType())){//私聊消息
                    //判断连接的socket是否上线了
                    if(ManageServerThread.getThread(msg.getReceiver()) == null){//判断线程集合里面是否有这个用户
                        //如果没有将信息添加到离线集合中去
                        QQServer.addOffline(msg.getReceiver(),msg);
                    }else{
                        //将消息转发给客户端指定的用户
                        ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getThread(msg.getReceiver()).getSocket().getOutputStream());
                        //封装message对象
                        Message message = new Message();
                        message.setMsgType(MessageType.COMM_EMS);
                        message.setContent(msg.getContent());
                        message.setSender(msg.getSender());
                        message.setReceiver(msg.getReceiver());
                        message.setSendTime(msg.getSendTime());

                        //转发消息
                        oos.writeObject(message);
                    }
                }else if(MessageType.GROUP_CHAT.equals(msg.getMsgType())){//群聊消息
                    //将消息转发给指定的用户，除了自己不需要
                    //从线程集合中获取所有的线程,获取socket，再转发
                    String[] userIdList = ManageServerThread.getAllUser().split(" ");
                    ObjectOutputStream oos = null;
                    Message message = new Message();
                    message.setMsgType(MessageType.GROUP_CHAT);
                    message.setContent(msg.getContent());
                    message.setSendTime(msg.getSendTime());
                    message.setSender(msg.getSender());
                    for(String userId : userIdList){
                        if(!userId.equals(msg.getSender())){
                            oos = new ObjectOutputStream(ManageServerThread.getThread(userId).getSocket().getOutputStream());
                            //发送消息给该用户
                            oos.writeObject(message);
                        }
                    }
                }else if(MessageType.FILE_SEND.equals(msg.getMsgType())){//发送文件的类型

                    if(ManageServerThread.getThread(msg.getReceiver()) == null){//判断线程集合里面是否有这个用户
                        //如果没有将信息添加到离线集合中去
                        QQServer.addOffline(msg.getReceiver(),msg);
                    }else{
                        System.out.println(msg.getSender()+" 给 "+msg.getReceiver()+" 发送 "+msg.getFileSrc()+" 文件 "+" 到 "+msg.getFileDest());
                        //将文件发送给指定的客户端
                        ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getThread(msg.getReceiver()).getSocket().getOutputStream());
                        oos.writeObject(msg);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
