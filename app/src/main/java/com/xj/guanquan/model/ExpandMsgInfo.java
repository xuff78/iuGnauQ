package com.xj.guanquan.model;

import common.eric.com.ebaselibrary.model.EBaseModel;

/**
 * Created by eric on 2015/9/15.
 */
public class ExpandMsgInfo extends EBaseModel {
    private String userName;
    private String userIcon;
    private String userId;
    private String groupName;
    private String groupIcon;
    private String groupId;

    public ExpandMsgInfo() {
    }

    public ExpandMsgInfo(String userName, String userIcon, String userId, String groupName, String groupIcon, String groupId) {
        this.userName = userName;
        this.userIcon = userIcon;
        this.userId = userId;
        this.groupName = groupName;
        this.groupIcon = groupIcon;
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
