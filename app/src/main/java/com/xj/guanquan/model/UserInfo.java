package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;
import common.eric.com.ebaselibrary.util.StringUtils;

/**
 * Created by eric on 2015/8/22.
 */
public class UserInfo extends EBaseModel {
    private Integer userId;
    private String nickName;
    private String avatar;
    private String phone;
    private int sex;
    private int age;
    private String height;
    private String weight;
    private String car;
    private String dating;
    private String signature;

    private String time;
    private String distance;

    private String sexTxt;

    //环信属性
    private Integer huanxinName;
    private String huanxinPassword;

    public UserInfo() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
        if (sex == 1) {
            sexTxt = "♂ ";
        } else {
            sexTxt = "♀ ";
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getHuanxinName() {
        return huanxinName;
    }

    public void setHuanxinName(Integer huanxinName) {
        this.huanxinName = huanxinName;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
    }

    public String getSexTxt() {
        if (StringUtils.isEmpty(sexTxt))
            sexTxt = "♂ ";
        return sexTxt;
    }

    public void setSexTxt(String sexTxt) {
        this.sexTxt = sexTxt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
