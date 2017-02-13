package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/22.
 */

public class KaoqinDb {
    private int UserId;
    private int ProjectId;
    private int StoreID;
    private String ProjectName;
    private String StoreName;
    private String Date;
    private String Time;

    private String PhoneImei;
    private String signInLat;
    private String signInLng;
    private String signInTime;
    private String signOutLat;
    private String signOutLng;
    private String signOutTime;
    private int  IsUpload;
    private String Expand1;
    private String Expand2;

    public String getSignOutLat() {
        return signOutLat;
    }

    public void setSignOutLat(String signOutLat) {
        this.signOutLat = signOutLat;
    }

    public String getSignInLng() {
        return signInLng;
    }

    public void setSignInLng(String signInLng) {
        this.signInLng = signInLng;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getProjectId() {
        return ProjectId;
    }

    public void setProjectId(int projectId) {
        ProjectId = projectId;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPhoneImei() {
        return PhoneImei;
    }

    public void setPhoneImei(String phoneImei) {
        PhoneImei = phoneImei;
    }

    public String getSignInLat() {
        return signInLat;
    }

    public void setSignInLat(String signInLat) {
        this.signInLat = signInLat;
    }

    public String getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(String signInTime) {
        this.signInTime = signInTime;
    }

    public String getSignOutLng() {
        return signOutLng;
    }

    public void setSignOutLng(String signOutLng) {
        this.signOutLng = signOutLng;
    }

    public String getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(String signOutTime) {
        this.signOutTime = signOutTime;
    }


    public String getExpand1() {
        return Expand1;
    }

    public void setExpand1(String expand1) {
        Expand1 = expand1;
    }

    public String getExpand2() {
        return Expand2;
    }

    public void setExpand2(String expand2) {
        Expand2 = expand2;
    }

    public int getIsUpload() {
        return IsUpload;
    }

    public void setIsUpload(int isUpload) {
        IsUpload = isUpload;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
