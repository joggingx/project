package com.xcx.view;

import com.xcx.service.ChatMessage;
import com.xcx.service.FileClient;
import com.xcx.service.UserClient;

import java.util.Scanner;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class QQView {

    private boolean loop = true;
    private String key = "";
    Scanner scanner = new Scanner(System.in);//接受用户键盘输入

    private UserClient userClient = new UserClient();//这个是用于登录服务操作
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统。。。。。");
    }

    public void mainMenu(){

        while (loop){
            System.out.println("==================欢迎登录网络通讯系统==================");
            System.out.println("\t\t\t\t\t1、登录系统");
            System.out.println("\t\t\t\t\t9、退出系统");
            System.out.print("请输入你的选择：");


            key = scanner.next();
            switch (key){
                case "1":
                    System.out.print("请输入用户名: ");
                    String userId = scanner.next();
                    System.out.print("请输入密  码: ");
                    String pwd = scanner.next();
                    if(userClient.checkUser(userId,pwd)){//已经将用户的信息设置好了
                        System.out.println("========欢迎 "+userId+" 用户登录系统=======");
                        //登录成功之后处理逻辑(又是一层循环)
                        while (loop){
                            System.out.println("<-------网络通讯系统二级菜单------->");
                            System.out.println("\t\t 1、显示在线用户列表");
                            System.out.println("\t\t 2、群发消息");
                            System.out.println("\t\t 3、私聊消息");
                            System.out.println("\t\t 4、发送文件");
                            System.out.println("\t\t 9、退出系统");
                            System.out.println("请输入你的选择");
                            key = scanner.next();
                            switch (key){
                                case "1":
                                    //System.out.println("显示在线用户菜单");
                                    userClient.getOnlineUserList(userId);
                                    break;
                                case "2":
                                    //System.out.println("群发消息");
                                    System.out.print("请输入想对大家说的话：");
                                    String str = scanner.next();
                                    //发送群聊消息
                                    ChatMessage.sendMessageToAll(userId,str);
                                    break;
                                case "3":
                                    //System.out.println("私聊消息");
                                    System.out.print("请输入要发送的用户：");
                                    String receiver = scanner.next();
                                    System.out.print("请输入要发送的内容：");
                                    String content = scanner.next();
                                    //发送私聊消息
                                    ChatMessage.sendMessage(receiver,userId,content);
                                    break;
                                case "4":
                                    //System.out.println("发送文件");
                                    System.out.print("请选择要发送的用户(在线用户)：");
                                    String getter = scanner.next();
                                    System.out.print("请选择需要发送的文件路径(文件格式为d:\\123.jpg):");
                                    String desc = scanner.next();
                                    System.out.print("请设置发送的路径(文件格式为d:\\123.jpg):");
                                    String dest = scanner.next();
                                    //发送文件
                                    FileClient.sendFile(desc,userId,getter,dest);
                                    break;
                                case "9":
                                    //调用客户端线程结束的方法
                                    userClient.exitClient();
                                    loop = false;//退出客户端
                                    break;
                            }

                        }
                    }else{
                        System.out.println("登入失败");
                    }

                    break;
                case "9":
                    loop = false;//退出循环
                    break;
                default:
                    break;
            }
        }
    }
}
