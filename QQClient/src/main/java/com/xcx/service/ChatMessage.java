package com.xcx.service;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.thread.ManageClientThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: xcx
 * @Since: 2022/9/30
 * 发送聊天相关的消息
 */
public class ChatMessage {
    /**
     * 私聊消息
     * @param receiver 接受者
     * @param userId 发送者id
     * @param content 发送的内容
     */
    public static void sendMessage(String receiver,String userId,String content){
        Message msg = new Message();
        msg.setSender(userId);
        msg.setReceiver(receiver);
        msg.setContent(content);

        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = sdf.format(time);
        msg.setSendTime(sendTime);
        msg.setMsgType(MessageType.COMM_EMS);

        //获取socket发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThread(userId).getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送群聊消息

    /**
     * 群聊消息
     * @param userId 发消息的人
     * @param content 发送的内容
     */
    public static void sendMessageToAll(String userId, String content){
        Message msg = new Message();
        msg.setMsgType(MessageType.GROUP_CHAT);
        msg.setSender(userId);
        msg.setContent(content);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = sdf.format(date);
        msg.setSendTime(sendTime);

        //获取socket发送消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThread(userId).getSocket().getOutputStream());
            //将信息发送出去
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
