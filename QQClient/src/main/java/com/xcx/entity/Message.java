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
    private String msgType;//信息类型
    private String content;//信息内容
    private String fileSrc;//文件源路径
    private String fileDest;//文件目标路径
    private byte[] fileBytes;//文件转成的字节数组
    public Message(){}

    public Message(String sender, String receiver, String sendTime, String msgType, String content, String fileSrc, String fileDest, byte[] fileBytes) {
        this.sender = sender;
        this.receiver = receiver;
        this.sendTime = sendTime;
        this.msgType = msgType;
        this.content = content;
        this.fileSrc = fileSrc;
        this.fileDest = fileDest;
        this.fileBytes = fileBytes;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileDesc) {
        this.fileSrc = fileDesc;
    }

    public String getFileDest() {
        return fileDest;
    }

    public void setFileDest(String fileDest) {
        this.fileDest = fileDest;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
