package com.xj.guanquan.model;

import android.widget.ImageView;
import android.widget.TextView;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by 可爱的蘑菇 on 2015/8/26.
 */
public class NoteInfo extends EBaseModel {

    private Integer id=-1;
    private String avatar="";
    private String nickName="";
    private Integer age=-1;
    private String time="";
    private String commentNum="";
    private Integer userId=-1;
    private Integer isLike=-1;
    private Integer likeNum=0;
    private String content="";
    private Integer sex=-1;
    private String picture="";
    private String datingLng="";
    private String datingLat="";

    public String getDatingLng() {
        return datingLng;
    }

    public void setDatingLng(String datingLng) {
        this.datingLng = datingLng;
    }

    public String getDatingLat() {
        return datingLat;
    }

    public void setDatingLat(String datingLat) {
        this.datingLat = datingLat;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
