package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/23.
 */
public class CircleInfo extends EBaseModel {
    private Integer groupId;
    private String groupName;
    private String picture;
    private String level;
    private String distance;
    private String description;

    public CircleInfo() {
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
