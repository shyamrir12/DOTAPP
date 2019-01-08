package com.example.awizom.dotapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionList {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("PermissionName")
    @Expose
    private String permissionName;
    @SerializedName("PermissionID")
    @Expose
    private Integer permissionID;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Integer getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(Integer permissionID) {
        this.permissionID = permissionID;
    }

}
