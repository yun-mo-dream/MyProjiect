package com.OtherUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2017/1/20.
 */

public class CheckNetworAndGpsUtil {
    public static void checkGps(final Activity activity) {
        // 判断GPS模块是否开启，如果没有则开启
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps && !network) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("请打开定位");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
//            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    arg0.dismiss();
//                }
//            } );
            dialog.show();
        }
    }

    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == State.CONNECTING)
            return true;
        return false;
    }

    public boolean isMobileConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (mobile == State.CONNECTED || mobile == State.CONNECTING)
            return true;
        return false;
    }

    public boolean isNetworkConnected(Context context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        return false;
    }

    //返回值 -1：没有网络  1：WIFI网络2：wap网络3：net网络

    public  static  final  int not_connected=-1;
    public  static  final int wifi_connected=1;
    public  static  final int mobile_2g_connected=2;
    public  static  final int mobile_3g_connected=3;
    public  static  final int mobile_4g_connected=4;
    public static int getNetworkType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info == null) {
            return not_connected;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return wifi_connected;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                    || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                return mobile_2g_connected;
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                return mobile_3g_connected;
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                return mobile_4g_connected;
            }
        }
        return not_connected;
    }
}
