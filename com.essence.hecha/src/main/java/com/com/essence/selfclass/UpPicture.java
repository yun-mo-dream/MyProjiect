package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/19.
 */

public class UpPicture {
    private   double lng;
    private  double lat;
    private   int userID;
    private  int storeID;
    private  int controlID;
    private  String createDate;
    private  String picBase64;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getControlID() {
        return controlID;
    }

    public void setControlID(int controlID) {
        this.controlID = controlID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }
}
