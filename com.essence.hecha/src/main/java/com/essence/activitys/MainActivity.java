package com.essence.activitys;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.OtherUtils.ApkVersionUtil;
import com.OtherUtils.CheckNetworAndGpsUtil;
import com.OtherUtils.ShowMessageUtils;
import com.essence.UploadUtils.RefreshAllDataAsyncTask;
import com.essence.UploadUtils.UploadDataAsyncTask;
import com.essence.UploadUtils.UploadPictrueAsyncTask;
import com.essence.hechaSystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private ListView listview;
  //  private String[] titleNames=new String[]{"工作填报","数据上传","数据更新","设置中心","项目管理","工作记录"};
  private String[] titleNames=new String[]{"工作填报","数据上传","数据更新","设置中心"};
    private Integer[] imameId= new Integer[]{R.drawable.ok,R.drawable.ok,R.drawable.ok,R.drawable.ok,R.drawable.ok,R.drawable.ok};
    private SimpleAdapter adapter;
    public static  MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        listview =(ListView)findViewById(R.id.main_listview);
        /* 参数一多，有些人就头晕了。这里解说下，各个参数的意思。
         * 第一个参数 this 代表的是当前上下文，可以理解为你当前所处的activity
         * 第二个参数 getData() 一个包含了数据的List,注意这个List里存放的必须是map对象。simpleAdapter中的限制是这样的List<? extends Map<String, ?>> data
         * 第三个参数 R.layout.user 展示信息的组件
         * 第四个参数 一个string数组，数组内存放的是你存放数据的map里面的key。
         * 第五个参数：一个int数组，数组内存放的是你展示信息组件中，每个数据的具体展示位置，与第四个参数一一对应
         * */
        adapter = new SimpleAdapter(this, getData(), R.layout.bar_mainactivity, new String[]{"image","titlename"}, new int[]{R.id.image_bar,R.id.title_bar});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case  0:
                       // Intent intent = new Intent(MainActivity.this,WorkActivity.class);
                        Intent intent = new Intent(MainActivity.this,SelectProjectStoreAcitivty.class);
                        startActivity(intent);
                    break;
                    case  1:
                     int netWorkType= CheckNetworAndGpsUtil.getNetworkType(MainActivity.this);
                        switch (netWorkType){
                            case CheckNetworAndGpsUtil.wifi_connected:
                                uploadAllData();
                                break;
                            case CheckNetworAndGpsUtil.mobile_2g_connected:
                            case CheckNetworAndGpsUtil.mobile_3g_connected:
                            case CheckNetworAndGpsUtil.mobile_4g_connected:
                                showUploadNowOrNot();
                                break;
                            case CheckNetworAndGpsUtil.not_connected:
                                ShowMessageUtils.showDialog(MainActivity.this,"当前无可用网络");
                                break;
                            default:
                                break;
                        }
                        break;
                    case  2:
                        if (CheckNetworAndGpsUtil.getNetworkType(MainActivity.this)==CheckNetworAndGpsUtil.not_connected){
                            ShowMessageUtils.showDialog(MainActivity.this,"当前无可用网络");
                            break;
                        }
                        RefreshAllDataAsyncTask refreshAllDataAsyncTask = new RefreshAllDataAsyncTask(MainActivity.this);
                        refreshAllDataAsyncTask.execute();
                        break;
                    case  3:
                        Intent intentSet = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intentSet);
                        break;
                    case  4:
                        break;
                    case  5:
                        break;
                    default:
                        break;
                }
            }
        });
        ApkVersionUtil.checkVersionInBackgound(this);//检查版本更新
        CheckNetworAndGpsUtil.checkGps(this);
        checkPermisson();
    }

    private ArrayList<HashMap<String, Object>> getData(){
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<titleNames.length;i++){
            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
            tempHashMap.put("image", imameId[i]);
            tempHashMap.put("titlename", titleNames[i]);
            arrayList.add(tempHashMap);
        }
        return arrayList;
    }

    private void showUploadNowOrNot(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("当前网络为手机流量，是否继续上传");
        builder.setCancelable(false);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
                uploadAllData();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void uploadAllData(){
        UploadDataAsyncTask uploadData = new UploadDataAsyncTask(MainActivity.this);
        uploadData.execute();
        UploadPictrueAsyncTask uploadPictrue = new UploadPictrueAsyncTask(MainActivity.this);
        uploadPictrue.execute();
    }

    /**
     * 动态获取权限
     */
    public static  final  int permisson_code=19;
    private void checkPermisson(){
        if (Build.VERSION.SDK_INT<23){
            return;
        }
        List<String> baiduDituPermisson=new ArrayList<String>();
        List<String> needPermisson=new ArrayList<String>();
        baiduDituPermisson.add(Manifest.permission.READ_PHONE_STATE);
        baiduDituPermisson.add(Manifest.permission.ACCESS_FINE_LOCATION);
        baiduDituPermisson.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        baiduDituPermisson.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        baiduDituPermisson.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        baiduDituPermisson.add(Manifest.permission.CAMERA);
        for (String permisson:baiduDituPermisson){
            if (ContextCompat.checkSelfPermission(MainActivity.this,permisson)!= PackageManager.PERMISSION_GRANTED){
                needPermisson.add(permisson);
            }else{
            }
        }
        int count =needPermisson.size();
        if (count>0){
            String[] needArray=new String[count];
            for (int i=0;i<count;i++){
                needArray[i]=needPermisson.get(i);
            }
            ActivityCompat.requestPermissions(MainActivity.this,needArray,permisson_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i=0;i<grantResults.length;i++){
            if ( grantResults[i] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(MainActivity.this,"您不同意的权限可能会导致无法正产工作",Toast.LENGTH_LONG).show();
            }
        }
        return;
    }
}
