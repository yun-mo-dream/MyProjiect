package com.com.essence.selfclass;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class TBBiaodan {
    private int ID;
    private String Name;
    private String ReportingFrequency;
   // private List<TBSkurows> Rows;
    private String Rows;
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
//    public List<TBSkurows> getRows() {
//        return Rows;
//    }
//    public void setRows(List<TBSkurows> rows) {
//        Rows = rows;
//    }
    public String getReportingFrequency() {
        return ReportingFrequency;
    }
    public void setReportingFrequency(String reportingFrequency) {
        ReportingFrequency = reportingFrequency;
    }

    public String getRows() {
        return Rows;
    }

    public void setRows(String rows) {
        Rows = rows;
    }
}
