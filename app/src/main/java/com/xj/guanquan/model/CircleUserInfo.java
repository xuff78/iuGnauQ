package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/26.
 */
public class CircleUserInfo extends EBaseModel {
    private String name;
    private String sex;
    private Integer age;
    private String relation;
    private String headImg;
    private String distance;
    private String time;

    public CircleUserInfo() {
    }

    public CircleUserInfo(String name, String sex, Integer age, String relation, String headImg, String distance, String time) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.relation = relation;
        this.headImg = headImg;
        this.distance = distance;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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
}
