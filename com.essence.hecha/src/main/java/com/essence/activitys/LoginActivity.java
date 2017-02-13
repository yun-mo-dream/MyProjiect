package com.essence.activitys;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.OtherUtils.CheckNetworAndGpsUtil;
import com.OtherUtils.EncripUtil;
import com.OtherUtils.MapToJson;
import com.OtherUtils.SharePreferenceUtil;
import com.OtherUtils.ShowMessageUtils;
import com.alibaba.fastjson.JSON;
import com.com.essence.selfclass.JsonTianBao;
import com.com.essence.selfclass.JsonUserInfo;
import com.com.essence.selfclass.JsonStoreInfo;
import com.com.essence.selfclass.StoreInfo;
import com.com.essence.selfclass.TBProject;
import com.com.essence.selfclass.UserInfo;
import com.essece.networkutils.ConstantNetwork;
import com.essece.networkutils.MyHttpClient;
import com.essece.networkutils.NetworkResponseInterface;
import com.essence.hechaSystem.R;
import com.essence.dbmanager.DataBaseUtils;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener,NetworkResponseInterface{
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button longinButton;
    public static final int request_user_info=0;
    public static final int request_download_stores=1;
    public static final int request_download_tianbaoTables=2;

    public static final int succeed_user_info=3;
    public static final int succeed_download_stores=4;
    public static final int succeed_download_tianbaoTables=5;

    public static final int failed_download=6;
    private  ProgressDialog progressDialog ;
    private SharedPreferences sharedPreferences;
    private int userId;
    private DataBaseUtils dataBaseUtils;
    private int totalProjectCount;
    private int succedProjectCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inite();
      //  handler.sendEmptyMessage(request_download_data);
    }
    private void inite(){
        usernameEdit=(EditText) findViewById(R.id.username);
        passwordEdit=(EditText) findViewById(R.id.password);
        passwordEdit.setOnTouchListener(new PasswordTouchListener());
        initeNamePass();
        longinButton=(Button) findViewById(R.id.button_login);
        longinButton.setOnClickListener(this);
        dataBaseUtils= new DataBaseUtils(LoginActivity.this);
    }
    private void initeNamePass(){
        UserInfo userInfo =SharePreferenceUtil.getUserInfo(this);
        if (userInfo.getUserId()>0){
            usernameEdit.setText(userInfo.getUserName());
            passwordEdit.setText(userInfo.getUserPassword());
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                login();
                break;
            default:
                break;
        }
    }
private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case  succeed_user_info:
                showProgressDialog("正在下载填报表单数据.....");
                downLoadTables(msg.arg1);
                break;
            case succeed_download_tianbaoTables:
                showProgressDialog("正在下载门店数据.....");
                downloadStores(userId);
                break;
            case succeed_download_stores:
                cancleProgressDialog();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;

            case  failed_download:
                cancleProgressDialog();
                String failedString=(String)msg.obj;
                ShowMessageUtils.showDialog(LoginActivity.this,failedString);
            default:
                break;
        }
    }
};
    //验证用户账号密码
    private void login(){
        if (CheckNetworAndGpsUtil.getNetworkType(this)==CheckNetworAndGpsUtil.not_connected){
            Toast.makeText(this,"当前无网络连接",Toast.LENGTH_LONG).show();
            return;
        }
        showProgressDialog("正在验证账号和密码.....");
        String jsonStreamString =generateUserInfoParams();
        MyHttpClient myHttpClient = new MyHttpClient(ConstantNetwork.url_userinfo,null,request_user_info);
        myHttpClient.setParamStremString(jsonStreamString);
        myHttpClient.setHttpType(MyHttpClient.uriconnection_post);
        myHttpClient.setNetworkResponseInterface(this);
        Thread thread = new Thread(myHttpClient);
        thread.start();
    }
private String  generateUserInfoParams(){
    HashMap<String,Object> paramMap=new HashMap<String,Object>();
    String userName=usernameEdit.getText().toString();
    paramMap.put("username",userName);
    String password=passwordEdit.getText().toString();
    paramMap.put("password", EncripUtil.getMD5(password));
    String jsonStreamString= MapToJson.MapToJson(paramMap);
    return jsonStreamString;
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
    @Override
    public void networkSucceed(int code, String succeedJson) {
        Log.i("test","code:"+code+" , succeedJson:"+succeedJson);
        Message message = new Message();
        switch (code){
            case request_user_info:
                JsonUserInfo jsonUserInfo =  JSON.parseObject(succeedJson,JsonUserInfo.class);
                if (jsonUserInfo.getErrorCode()==0){
                    String userName=usernameEdit.getText().toString();
                    String password=passwordEdit.getText().toString();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(jsonUserInfo.getData().getID());
                    userInfo.setUserNickName(jsonUserInfo.getData().getName());
                    userInfo.setUserName(userName);
                    userInfo.setUserPassword(password);
                    SharePreferenceUtil.saveUserInfo(this,userInfo);
                    message.what=succeed_user_info;
                    userId=userInfo.getUserId();
                    message.arg1=userId;
                    handler.sendMessage(message);
                }else{
                    message.what=failed_download;
                    String failedMsg="账号验证失败,"+jsonUserInfo.getErrorMsg();
                    message.obj=failedMsg;
                    handler.sendMessage(message);
                }
                break;
            case request_download_stores:
                JsonStoreInfo jsonStoreInfo=JSON.parseObject(succeedJson,JsonStoreInfo.class);
                if (jsonStoreInfo.getErrorCode()==0){
                    succedProjectCount++;
                    List<StoreInfo>storeInfos = jsonStoreInfo.getData();
                    dataBaseUtils.insertOrReplaceStores(storeInfos);
                    if (succedProjectCount==totalProjectCount){
                        message.what=succeed_download_stores;
                        message.arg1=userId;
                        handler.sendMessage(message);
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



    public class PasswordTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片，此处是获取右边的
            Drawable drawable = passwordEdit.getCompoundDrawables()[2];
            //如果右边没有图片，不再处理
            if (drawable == null)
                return false;
            //如果不是按下事件，不再处理
            if (event.getAction() != MotionEvent.ACTION_UP)
                return false;
            if (event.getX() > passwordEdit.getWidth()
                    - passwordEdit.getPaddingRight()
                    - drawable.getIntrinsicWidth()){
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            return false;
        }
    }


    private void showProgressDialog(String message) {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
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
