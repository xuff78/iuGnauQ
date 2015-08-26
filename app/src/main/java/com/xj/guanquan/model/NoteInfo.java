package com.xj.guanquan.model;

import android.widget.ImageView;
import android.widget.TextView;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by 可爱的蘑菇 on 2015/8/26.
 */
public class NoteInfo extends EBaseModel {

    private String userImg="";
    private String userName="";
    private String userAge="";
    private String createTime="";
    private String usrComment="";
    private String favorBtn="";
    private String replyNums="";

    public NoteInfo(String userImg,String userName, String userAge, String createTime, String usrComment, String favorBtn, String replyNums) {
        this.userImg = userImg;
        this.userAge = userAge;
        this.createTime = createTime;
        this.usrComment = usrComment;
        this.favorBtn = favorBtn;
        this.replyNums = replyNums;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsrComment() {
        return usrComment;
    }

    public void setUsrComment(String usrComment) {
        this.usrComment = usrComment;
    }

    public String getFavorBtn() {
        return favorBtn;
    }

    public void setFavorBtn(String favorBtn) {
        this.favorBtn = favorBtn;
    }

    public String getReplyNums() {
        return replyNums;
    }

    public void setReplyNums(String replyNums) {
        this.replyNums = replyNums;
    }
}
