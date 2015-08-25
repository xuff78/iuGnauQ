package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/8/25.
 */
public class PictureInfo extends EBaseModel {
    private Integer id;
    private String picture;

    public PictureInfo() {
    }

    public PictureInfo(String pictureUrl) {
        this.picture = pictureUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
