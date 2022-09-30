package com.xcx.service;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.thread.ManageClientThread;

import java.io.*;

/**
 * @Author: xcx
 * @Since: 2022/9/30
 * 处理文件相关的类
 */
public class FileClient {

    /**
     * 发送文件的方法
     * @param desc 源文件
     * @param sender 发送方
     * @param receiver 接收方
     * @param dest 目的地
     */
    public static void sendFile(String desc, String sender, String receiver, String dest){
        //创建一个文件输入流，读取文件
        FileInputStream fis = null;
        byte[] fileBytes = null;
        try {
            fis = new FileInputStream(desc);
            //将文件转化成字节数组，封装到message对象中去，发送给服务端
            fileBytes = new byte[(int) new File(desc).length()];//创建文件大小的字节数组
            //将文件内容写到字节数组中去
            fis.read(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将字节数组设置到message对象里面去，发送给服务端
        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setFileBytes(fileBytes);

        msg.setFileSrc(desc);
        msg.setFileDest(dest);
        msg.setMsgType(MessageType.FILE_SEND);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThread(sender).getSocket().getOutputStream());
            //将文件写出去
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
