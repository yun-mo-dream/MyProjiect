package com.com.essence.selfclass;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class TBSkurows {

    private int ID;
    private String Name;
    private List<TBWidget> Contorls;
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
    public List<TBWidget> getContorls() {
        return Contorls;
    }



    public void setContorls(List<TBWidget> contorls) {
        Contorls = contorls;
    }
}
