package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/20.
 */

public class JsonUploadPictureResult {
    private int  ErrorCode=-1;
    private String ErrorMsg;
    private String Data;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
