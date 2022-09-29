package com.xcx.entity;

import java.io.Serializable;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class Message implements Serializable {
    private final static long serialVersionUID = 1001111L;//以便序列化

    private String sender;//发送者
    private String receiver;//接收者
    private String sendTime;//发送时间
    private String msgType;//消息类型

    public Message(){}

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public Message(String sender, String receiver, String sendTime, String msgType){
        this.sender = sender;
        this.receiver = receiver;
        this.sendTime = sendTime;
        this.msgType = msgType;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
