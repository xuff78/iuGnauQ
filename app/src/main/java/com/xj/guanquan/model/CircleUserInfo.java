package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/26.
 */
public class CircleUserInfo extends EBaseModel {
    private Integer userId;
    private String nickName;
    private String sex;
    private Integer age;
    private String relation;
    private String avatar;
    private String distance;
    private String time;
    private String name;
    private Integer id;

    //环信属性
    private Integer huanxinName;
    private String huanxinPassword;

    public CircleUserInfo() {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRelation() {
        if (relation != null) {
            String relationTxt = "";
            switch (Integer.valueOf(relation)) {
                case 0:
                    relationTxt = "自己";
                    break;
                case 1:
                    relationTxt = "粉丝";
                    break;
                case 2:
                    relationTxt = "关注";
                    break;
                case 3:
                    relationTxt = "好友";
                    break;
                case 4:
                    relationTxt = "陌生人";
                    break;
            }
            return "关系 ： " + relationTxt;
        }
        return null;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
