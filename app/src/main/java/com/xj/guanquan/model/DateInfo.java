package com.xj.guanquan.model;

/**
 * Created by 可爱的蘑菇 on 2015/9/2.
 */
public class DateInfo extends NoteInfo{

    private String address="";
    private Long beginTime=0l;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }
}
