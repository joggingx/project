package com.xcx.entity;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public interface MessageType {

    //在接口中定义一些常量，表示消息类型
    String LOGIN_SUCCESS = "1";//登录成功
    String LOGIN_FAIL = "0";//登录失败
    String COMM_EMS = "2";//普通信息
    String GROUP_CHAT = "7";//群聊消息
    String GET_OLINE_LIST = "4";//得到用户在线列表
    String SET_OLINE_LIST = "5";//返回用户在线列表
    String CLIENT_EXIT = "6";//客户端退出
    String FILE_SEND = "8";//发送文件
}