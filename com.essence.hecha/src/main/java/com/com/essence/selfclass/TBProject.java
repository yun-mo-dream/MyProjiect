package com.com.essence.selfclass;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class TBProject {
    private int ID;
    private String Name;
    private String Logo;
   // private String Tables;
    private List<TBBiaodan> Tables;
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
    public String getLogo() {
        return Logo;
    }
    public void setLogo(String logo) {
        Logo = logo;
    }

//    public String getTables() {
//        return Tables;
//    }
//
//    public void setTables(String tables) {
//        Tables = tables;
//    }
    public List<TBBiaodan> getTables() {
        return Tables;
    }
    public void setTables(List<TBBiaodan> tables) {
        Tables = tables;
    }

}
