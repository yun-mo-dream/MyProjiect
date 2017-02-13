package com.OtherUtils.ShowPictureUtils;

import android.os.Environment;

public class Constant {
public final static int chooseItemType_projiect = 0;
public final static int chooseItemType_supermarket = 1;
public final static int choose_supermarket_over = 2;
public final static int take_picture = 3;
public final static int choose_skulist = 9;
public final static int from_login = 4;
public final static int from_selsect_projiect_supermarket = 5;
public final static int startTakePictrue=111;
public final static int TakePictrueRestart=112;
public static String rootPath=Environment.getExternalStorageDirectory() + "/BS_APP/Picture/";
public static String crashPath=Environment.getExternalStorageDirectory() + "/BS_APP/crashLog/";
public static String dataBaseBackPath=Environment.getExternalStorageDirectory() + "/BS_APP/dataBaseBack/";
}
