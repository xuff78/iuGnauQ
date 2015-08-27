package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/27.
 */
public class MessageInfo extends EBaseModel {
    private String name;
    private String headImage;
    private Integer msgNum;
    private String time;
    private String lastMsg;

    public MessageInfo() {
    }

    public MessageInfo(String name, String headImage, Integer msgNum, String time, String lastMsg) {
        this.name = name;
        this.headImage = headImage;
        this.msgNum = msgNum;
        this.time = time;
        this.lastMsg = lastMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(Integer msgNum) {
        this.msgNum = msgNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
