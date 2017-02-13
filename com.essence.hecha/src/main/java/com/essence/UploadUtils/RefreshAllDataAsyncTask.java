package com.essence.UploadUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.OtherUtils.MapToJson;
import com.OtherUtils.SharePreferenceUtil;
import com.alibaba.fastjson.JSON;
import com.com.essence.selfclass.JsonStoreInfo;
import com.com.essence.selfclass.JsonTianBao;
import com.com.essence.selfclass.StoreInfo;
import com.com.essence.selfclass.TBProject;
import com.essece.networkutils.ConstantNetwork;
import com.essece.networkutils.MyHttpClient;
import com.essece.networkutils.NetworkResponseInterface;
import com.essence.dbmanager.DataBaseUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class RefreshAllDataAsyncTask  implements NetworkResponseInterface {
    private Context context;
    private  DataBaseUtils dataBaseUtils;
    private  int userId;
    private ProgressDialog progressDialog;
    private Handler handler;
    public static final int request_user_info=0;
    public static final int request_download_stores=1;
    public static final int request_download_tianbaoTables=2;
    public static final int succeed_user_info=3;
    public static final int succeed_download_stores=4;
    public static final int succeed_download_tianbaoTables=5;
    public static final int failed_download=6;
    private int totalProjectCount;
    private int succedProjectCount;
    public RefreshAllDataAsyncTask(Context context) {
        this.context = context;
        dataBaseUtils = new DataBaseUtils(context);
        userId = SharePreferenceUtil.getUserId(context);
    }

    public void execute(){
        showProgressDialog("正在表单配置数据.....");
        getRefreshHandler();
        downLoadTables(userId);
    }
    //下载门店列表信息
    private void downloadStores(int userId){
        HashMap<String,Integer> projectMap=dataBaseUtils.queryTianbaoTablesForProjects();
        totalProjectCount = projectMap.size();
        if (totalProjectCount<=0){
            Message message = new Message();
            message.what=succeed_download_stores;
            handler.sendMessage(message);
            return;
        }
        for (String projectName:projectMap.keySet()){
            int projectId=projectMap.get(projectName);
            HashMap<String,Object> paramMap=new HashMap<String,Object>();
            Log.i("test","projectid:"+projectId+" , projectName:"+projectName);
            paramMap.put("userid",userId);
            paramMap.put("projectid",projectId);
            String jsonStreamString= MapToJson.MapToJson(paramMap);
            MyHttpClient myHttpClient = new MyHttpClient(ConstantNetwork.url_download_stores,null,request_download_stores);
            myHttpClient.setParamStremString(jsonStreamString);
            myHttpClient.setHttpType(MyHttpClient.uriconnection_post);
            myHttpClient.setNetworkResponseInterface(this);
            Thread thread = new Thread(myHttpClient);
            thread.start();
        }
    }

    //下载填报表单信息
    private void downLoadTables(int userId){
        HashMap<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userid",userId);
        String jsonStreamString= MapToJson.MapToJson(paramMap);
        MyHttpClient myHttpClient = new MyHttpClient(ConstantNetwork.url_download_tables,null,request_download_tianbaoTables);
        myHttpClient.setParamStremString(jsonStreamString);
        myHttpClient.setHttpType(MyHttpClient.uriconnection_post);
        myHttpClient.setNetworkResponseInterface(this);
        Thread thread = new Thread(myHttpClient);
        thread.start();
    }

     private void getRefreshHandler(){
        handler = new Handler(context.getMainLooper()){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case succeed_download_stores:
                        cancleProgressDialog();
                        Toast.makeText(context, "更新数据成功", Toast.LENGTH_LONG).show();
                        break;
                    case succeed_download_tianbaoTables:
                        showProgressDialog("正在下载填报表单数据.....");
                        dataBaseUtils.delecteAllStores();
                        downloadStores(userId);
                        break;
                    case failed_download:
                        cancleProgressDialog();
                        Toast.makeText(context, "更新数据失败", Toast.LENGTH_LONG).show();
                    default:
                        break;
                }
            }
        };
     }
    @Override
    public void networkSucceed(int code, String succeedJson) {
        {
            Log.i("test","code:"+code+" , succeedJson:"+succeedJson);
            Message message = new Message();
            switch (code){
                case request_download_stores:
                    JsonStoreInfo jsonStoreInfo=JSON.parseObject(succeedJson,JsonStoreInfo.class);
                    if (jsonStoreInfo.getErrorCode()==0){
                        {
                            succedProjectCount++;
                            List<StoreInfo>storeInfos = jsonStoreInfo.getData();
                            dataBaseUtils.insertOrReplaceStores(storeInfos);
                            if (succedProjectCount==totalProjectCount){
                                message.what=succeed_download_stores;
                                handler.sendMessage(message);
                            }
                        }
                    }else{
                        message.what=failed_download;
                        String failedMsg="下载门店列表失败,"+jsonStoreInfo.getErrorMsg();
                        message.obj=failedMsg;
                        handler.sendMessage(message);
                    }
                    break;
                case request_download_tianbaoTables:
                    JsonTianBao jsonTianBao = JSON.parseObject(succeedJson,JsonTianBao.class);
                    if (jsonTianBao.getErrorCode()==0){
                        List<TBProject>tbProjects=jsonTianBao.getData();
                        dataBaseUtils.delecteAllTianbaoTables();
                        dataBaseUtils.insertOrReplaceTBBiaoDans(tbProjects);
                        message.what=succeed_download_tianbaoTables;
                        handler.sendMessage(message);
                    }else{
                        message.what=failed_download;
                        String failedMsg="下载填报表单失败,"+jsonTianBao.getErrorMsg();
                        message.obj=failedMsg;
                        handler.sendMessage(message);
                    }
                    break;
                default:
                    break;
            }
}
}

    @Override
    public void networkFailed(int code, String failedJson) {
        Log.i("test","code:"+code+" , failedJson:"+failedJson);
        Message message=new Message();
        message.what=failed_download;
        String failedMsg="网络请求失败,"+failedJson;
        message.obj=failedMsg;
        handler.sendMessage(message);
    }

    @Override
    public void networkException(int code, String exceptionMessage) {
        Message message=new Message();
        message.what=failed_download;
        String failedMsg="网络请求异常,"+exceptionMessage;
        message.obj=failedMsg;
        handler.sendMessage(message);
    }

    private void showProgressDialog(String message) {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(context);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void cancleProgressDialog() {
        if (progressDialog!=null){
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }
        }
    }
}
