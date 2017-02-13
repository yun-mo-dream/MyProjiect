package com.OtherUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.essence.activitys.SettingActivity;
import com.essence.hechaSystem.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ApkVersionUtil {
    public static  void checkVersionInBackgound(final Activity activity){
        PgyUpdateManager.register(activity,new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
                final AppBean appBean = getAppBeanFromString(result);
                String newVersion=appBean.getVersionName().replace(".", "");
                int versionNew=Integer.parseInt(newVersion);
                if (versionNew<=getCurrentVersion(activity.getApplicationContext())) {
                    return;
                }
                if (activity.isFinishing()) {
                    return;
                }
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setMessage(activity.getString(R.string.app_name)+" 发现新版本："+appBean.getVersionName()+"\n"+"现在就去更新！")
                        .setNegativeButton("确定",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                startDownloadTask(activity,appBean.getDownloadURL());
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
            }
            @Override
            public void onNoUpdateAvailable() {
            }
        });
    }


    public static void checkVersion(final Activity activity) {
        if (CheckNetworAndGpsUtil.getNetworkType(activity)!=CheckNetworAndGpsUtil.not_connected) {
            final ProgressDialog dialog = ProgressDialog.show(activity, null, "正在检查版本中.....");
            PgyUpdateManager.register(activity,new UpdateManagerListener() {
                @Override
                public void onUpdateAvailable(final String result) {
                    dialog.dismiss();
                    final AppBean appBean = getAppBeanFromString(result);
                    String newVersion=appBean.getVersionName().replace(".", "");
                    int versionNew=Integer.parseInt(newVersion);
                    if (versionNew<=getCurrentVersion(activity.getApplicationContext())) {

                        Toast.makeText(activity, "已经是最新版本", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (activity.isFinishing()) {
                        return;
                    }
                    new AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage(activity.getString(R.string.app_name)+" 发现新版本："+appBean.getVersionName()+"\n"+"现在就去更新！")
                            .setNegativeButton("确定",new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    startDownloadTask(activity,appBean.getDownloadURL());
                                }
                            })
                            .setNeutralButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            }).show();
                }
                @Override
                public void onNoUpdateAvailable() {
                    dialog.dismiss();
                    Toast.makeText(activity, "已经是最新版本", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(activity, "请打开网络", Toast.LENGTH_LONG).show();
        }
    }

    public static int getCurrentVersion(Context context) {
        int version=0;
        String versionCode =getVersionName( context);
        versionCode=versionCode.replace(".", "");
        version=Integer.parseInt(versionCode);
        return version;
    }

    public static  String getVersionName(Context context) {
        String versionName="1.0";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
