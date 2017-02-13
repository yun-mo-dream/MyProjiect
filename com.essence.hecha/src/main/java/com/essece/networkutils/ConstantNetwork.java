package com.essece.networkutils;

/**
 * Created by Administrator on 2017/1/13.
 */

public class ConstantNetwork {
  //  public static  final String url_path="http://192.168.60.50/hecha";
    public static  final String url_path="http://wxapi.egocomm.cn/Perfetti";

    public static  final String url_userinfo=url_path+"/service/app.ashx?action=login";
    public static  final String url_download_tables=url_path+"/service/app.ashx?action=loadconfig";
    public static  final String url_download_stores=url_path+"/service/app.ashx?action=loadstore";
    public static  final String url_upload_data=url_path+"/service/uploaddata.ashx";
    public static  final String url_upload_picture=url_path+"/service/uploadfile.ashx?type=json";
}
