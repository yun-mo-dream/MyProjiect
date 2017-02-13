package com.com.essence.selfclass;

/**
 * Created by Administrator on 2017/1/13.
 */

public class JsonUserInfo {
    private String ErrorMsg;
    private int ErrorCode;
    private InerData Data;

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

    public InerData getData() {
        return Data;
    }

    public void setData(InerData Data) {
        this.Data = Data;
    }

    public class InerData{
        private   int ID;
        private  String Name;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
