package com.xcx.thread;

import com.xcx.entity.Message;
import com.xcx.entity.MessageType;
import com.xcx.thread.ManageServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @Author: xcx
 * @Since: 2022/9/30
 * 服务端推送新闻
 */
public class NewServerThread implements Runnable{
    private Scanner scanner = new Scanner(System.in);
    @Override
    public void run() {
        while (true){
            System.out.println("请输入服务端所推送的新闻(exit退出)：");
            String news = scanner.next();
            if("exit".equals(news)){
                break;
            }
            //封装message对象，将信息发给全部的客户端
            Message message = new Message();
            message.setMsgType(MessageType.GROUP_CHAT);
            message.setSender(" 服务端 ");
            message.setContent(news);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(date);
            message.setSendTime(time);

            System.out.println(message.getSendTime()+": "+message.getSender()+" 对大家说:"+message.getContent());
            //发送消息
            Set<String> set = ManageServerThread.getMap().keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()){
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getThread(iterator.next()).getSocket().getOutputStream());
                    //发送消息
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
