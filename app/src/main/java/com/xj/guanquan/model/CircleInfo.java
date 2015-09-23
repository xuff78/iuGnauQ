package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;
import common.eric.com.ebaselibrary.util.StringUtils;

/**
 * Created by eric on 2015/8/23.
 */
public class CircleInfo extends EBaseModel {
    private Integer id;
    private String name;
    private String picture;
    private String level;
    private String distance;
    private String description;
    private String file_logo;
    private String logo;

    public CircleInfo() {
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


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLevel() {
        if (!StringUtils.isEmpty(level))
            return " LV " + level + " ";
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

    public String getFile_logo() {
        return file_logo;
    }

    public void setFile_logo(String file_logo) {
        this.file_logo = file_logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
