package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/22.
 */
public class UserInfo extends EBaseModel {
    private Integer id;
    private String name;
    private String headImage;
    private String sex;
    private int age;
    private String height;
    private String weight;
    private String carDescript;
    private String dateDescript;

    private String time;
    private String distance;

    public UserInfo() {
    }

    public UserInfo(String name, String headImage, String sex, int age, String height, String weight, String carDescript, String dateDescript) {
        this.name = name;
        this.headImage = headImage;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.carDescript = carDescript;
        this.dateDescript = dateDescript;
    }

    public UserInfo(String name, String headImage, String sex, int age, String dateDescript, String time, String distance) {
        this.name = name;
        this.headImage = headImage;
        this.sex = sex;
        this.age = age;
        this.dateDescript = dateDescript;
        this.time = time;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCarDescript() {
        return carDescript;
    }

    public void setCarDescript(String carDescript) {
        this.carDescript = carDescript;
    }

    public String getDateDescript() {
        return dateDescript;
    }

    public void setDateDescript(String dateDescript) {
        this.dateDescript = dateDescript;
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
}
