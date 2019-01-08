package com.example.awizom.dotapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPermissionModel {
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("PermissionList")
    @Expose
    private List<PermissionList> permissionList = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PermissionList> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionList> permissionList) {
        this.permissionList = permissionList;
    }
}
