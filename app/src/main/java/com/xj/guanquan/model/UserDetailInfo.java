package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/9/18.
 */
public class UserDetailInfo extends EBaseModel {
    private String phone;
    private String password;
    private String nickName;
    private String file_avatar;
    private String file_beautyCert;
    private Integer sex;//1-男 2-女
    private String birthday;
    private String height;
    private String weight;
    private String income;
    private String constellation;
    private String feelingStatus;
    private String job;
    private String industry;
    private Integer haveCar;//1-有车 0-车
    private String brand;
    private String model;
    private String configuration;
    private String file_idCard;
    private String file_trafficInsurance;
    private String file_drivingLicense;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFile_avatar() {
        return file_avatar;
    }

    public void setFile_avatar(String file_avatar) {
        this.file_avatar = file_avatar;
    }

    public String getFile_beautyCert() {
        return file_beautyCert;
    }

    public void setFile_beautyCert(String file_beautyCert) {
        this.file_beautyCert = file_beautyCert;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getFeelingStatus() {
        return feelingStatus;
    }

    public void setFeelingStatus(String feelingStatus) {
        this.feelingStatus = feelingStatus;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getHaveCar() {
        return haveCar;
    }

    public void setHaveCar(Integer haveCar) {
        this.haveCar = haveCar;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getFile_idCard() {
        return file_idCard;
    }

    public void setFile_idCard(String file_idCard) {
        this.file_idCard = file_idCard;
    }

    public String getFile_trafficInsurance() {
        return file_trafficInsurance;
    }

    public void setFile_trafficInsurance(String file_trafficInsurance) {
        this.file_trafficInsurance = file_trafficInsurance;
    }

    public String getFile_drivingLicense() {
        return file_drivingLicense;
    }

    public void setFile_drivingLicense(String file_drivingLicense) {
        this.file_drivingLicense = file_drivingLicense;
    }
}
