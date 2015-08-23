package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/23.
 */
public class CircleInfo extends EBaseModel {
    private Integer id;
    private String circleName;
    private String headImage;
    private String level;
    private String distance;
    private String circleDesc;

    public CircleInfo() {
    }

    public CircleInfo(String circleName, String headImage, String level, String distance, String circleDesc) {
        this.circleName = circleName;
        this.headImage = headImage;
        this.level = level;
        this.distance = distance;
        this.circleDesc = circleDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCircleDesc() {
        return circleDesc;
    }

    public void setCircleDesc(String circleDesc) {
        this.circleDesc = circleDesc;
    }
}
