package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/9/3.
 */
public class KeyValue extends EBaseModel {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
