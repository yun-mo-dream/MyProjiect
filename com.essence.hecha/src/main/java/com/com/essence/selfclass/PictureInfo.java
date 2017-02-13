package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/19.
 */

public class PictureInfo {
    private int  userId;
    private int  storeId;
    private int  ControlId;
    private double lat;
    private double lng;
    private String createDate;
    private String picturePath;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getControlId() {
        return ControlId;
    }

    public void setControlId(int controlId) {
        ControlId = controlId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
