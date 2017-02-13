package com.com.essence.selfclass;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class JsonStoreInfo {
    private String ErrorMsg;
    private int ErrorCode;
    private List<StoreInfo> Data;

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public List<StoreInfo> getData() {
        return Data;
    }

    public void setData(List<StoreInfo> data) {
        Data = data;
    }
}
