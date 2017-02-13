package com.com.essence.selfclass;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class JsonTianBao {
    private int ErrorCode;
    private String ErrorMsg;
    private List<TBProject> Data;
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
    public List<TBProject> getData() {
        return Data;
    }
    public void setData(List<TBProject> data) {
        Data = data;
    }
}
