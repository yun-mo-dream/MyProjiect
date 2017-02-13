package com.essence.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.OtherUtils.SharePreferenceUtil;
import com.OtherUtils.ShowPictureUtils.Constant;
import com.OtherUtils.ShowPictureUtils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.com.essence.selfclass.DynamicTinbaoDB;
import com.com.essence.selfclass.JsonRows;
import com.com.essence.selfclass.JsonWidget;
import com.com.essence.selfclass.Location;
import com.com.essence.selfclass.PictureInfo;
import com.com.essence.selfclass.UpItem;
import com.com.essence.selfclass.UpJson;
import com.com.essence.selfclass.WidgetType;
import com.essence.WidgetsView.DateTimeWidget;
import com.essence.WidgetsView.EditeWidget;
import com.essence.WidgetsView.MultipleChoiceWidget;
import com.essence.WidgetsView.SingleChoiceWidget;
import com.essence.WidgetsView.TakePictrueWidget;
import com.essence.WidgetsView.TitleBarWidget;
import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TianBaoActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layoutGroup;
    private RelativeLayout back;
    private TextView saveButton;
    private int userId;
    private int storeId;
    private int projiectId;
    private int tableId;
    // 数据库有值，则不需要填 而是从数据库里面取数据显示，否则就填写，即第一次进来填报
    private Boolean showFlag = false;
    private DataBaseUtils dbDatabaseUtil;
    // 存放控件的list
    private List<ViewGroup> widgetList = new ArrayList<ViewGroup>();
    public  int  pictureControlId;
    private  String dateHMS;
    private  String rootSavePath;
    private  String nowPicturePath;
    private  String fileName;
    private String dateYMD;
    public static int default_error_id=-1;
    private final static int load_data_succeed = 2;
    private final static int load_data_failed = 3;
    private ProgressDialog progressDialog;
    //private  List<JsonRows> jsonRowsList;
    private   List<JsonRows> jsonRowsList = new ArrayList<JsonRows>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tian_bao);
        inite();
        startLocation();
    }
    private void inite(){
        TextView titleName = (TextView) findViewById(R.id.tianbao_title);
        back = (RelativeLayout) findViewById(R.id.back_tianbao);
        layoutGroup = (LinearLayout) findViewById(R.id.tianbao_bar);
        saveButton = (TextView) findViewById(R.id.save_tianbao);
        back.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        userId = SharePreferenceUtil.getUserId(this);
        Intent intent = getIntent();
        storeId=intent.getIntExtra("storeId",default_error_id);
        projiectId=intent.getIntExtra("projiectId",default_error_id);
        tableId=intent.getIntExtra("tableId",default_error_id);
        titleName.setText(intent.getStringExtra("storeName"));
        dbDatabaseUtil = new DataBaseUtils(this);
        dateYMD = DateUtils.getDateStr("yyyy-MM-dd");
        rootSavePath = Constant.rootPath + "/" + dateYMD;
    }

    private void getData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //添加控件
                String jsonTable = dbDatabaseUtil.queryTableJsonByProjectIdTableId(projiectId,tableId);
             //   Log.i("test", "jsonTable---" + jsonTable);
                if (jsonTable!=null){
                    List<JsonRows> jsonRowses = JSON.parseArray(jsonTable, JsonRows.class);
                    jsonRowsList.clear();
                    jsonRowsList.addAll(jsonRowses);
                    handler.sendEmptyMessage(load_data_succeed);
                }else{
                    handler.sendEmptyMessage(load_data_failed);
                }
            }
        });
        thread.start();
    }
    /**
     * 根据json生成要显示的控件，加入到集合中，为显示做准备
     */
    private void generateWidgets(List<JsonRows> jsonRowsList){
        widgetList.clear();
        DynamicTinbaoDB daDynamicTinbaoDB = dbDatabaseUtil.queryDynamicTianBao(userId,projiectId, storeId,tableId, dateYMD);
        UpJson upJson = null;
        // 有值，则List<KongJianData>需要转换为List<TianbaoObjiect>方便显示
        if (daDynamicTinbaoDB!=null) {
            showFlag = true;
            String jsonString = daDynamicTinbaoDB.getJsonString();
            Log.i("test", "savedb:jsonString---" + jsonString);
            upJson = JSON.parseObject(jsonString, UpJson.class);
        } else {
            showFlag = false;
        }
        for (JsonRows jsoRow : jsonRowsList) {
            TitleBarWidget titleBarWidget = new TitleBarWidget(TianBaoActivity.this, jsoRow.getName(),jsoRow.getImg());
            widgetList.add(titleBarWidget);
            for (JsonWidget jsonWidget : jsoRow.getContorls()) {
                switch (WidgetType.getWidgetType(jsonWidget.getControlType())) {
                    case WidgetType.type_text:
                        EditeWidget editeWidget = new EditeWidget(TianBaoActivity.this, jsonWidget);
                        widgetList.add(editeWidget);
                        //显示用的
                        if (showFlag == true) {
                            String value = checkDbhasSku(jsonWidget.getID(), upJson);
                            if (value != null) {
                                editeWidget.setValue(value);
                            }
                        }
//                        DateTimeWidget dateTimeWidget = new DateTimeWidget(TianBaoActivity.this, jsonWidget);
//                        widgetList.add(dateTimeWidget);
//                        //显示用的
//                        if (showFlag == true) {
//                            String value = checkDbhasSku(jsonWidget.getID(), upJson);
//                            if (value != null) {
//                                dateTimeWidget.setValue(value);
//                            }
//                        }
                        break;
                    case WidgetType.type_sigleChoice:
                        SingleChoiceWidget singleChoiceWidget = new SingleChoiceWidget(TianBaoActivity.this, jsonWidget);
                        widgetList.add(singleChoiceWidget);
                        //显示用的
                        if (showFlag == true) {
                            String value = checkDbhasSku(jsonWidget.getID(), upJson);
                            if (value != null) {
                                singleChoiceWidget.setValue(value);
                            }
                        }
                        break;
                    case WidgetType.type_multChoice:
                        MultipleChoiceWidget multipleChoiceWidget = new MultipleChoiceWidget(TianBaoActivity.this, jsonWidget);
                        widgetList.add(multipleChoiceWidget);
                        if (showFlag == true) {
                            String value = checkDbhasSku(jsonWidget.getID(), upJson);
                            if (value != null) {
                                multipleChoiceWidget.setValue(value);
                            }
                        }
                        break;
                    case WidgetType.type_camera:
                        TakePictrueWidget takePictrueWidget = new TakePictrueWidget(handler, TianBaoActivity.this, jsonWidget,storeId);
                        widgetList.add(takePictrueWidget);
                        break;
                    default:
                        break;
                }
            }

        }
    }
    boolean isAddData=false;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case loaction_succeed:
                    if (!isAddData){
                     //   cancleProgressDialog();
                        showProgressDialog("正在初始化数据....");
                        getData();
                        isAddData=true;
                    }
                    break;
                case Constant.startTakePictrue:
                    pictureControlId = msg.arg1;
                    openCamera();
                    break;
                case Constant.TakePictrueRestart:
                    openCamera();
                    break;
                case load_data_succeed:
                    generateWidgets(jsonRowsList);
                    addWidgetsToWindow();
                    cancleProgressDialog();
                    break;
                case load_data_failed:
                    cancleProgressDialog();
                    Toast.makeText(TianBaoActivity.this, "未查询到填报表单", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
  private void addWidgetsToWindow(){
     changWidgetStatus(showFlag);
    if (showFlag){
        saveButton.setText("修改");
    }else{
        saveButton.setText("保存");
    }
    for (ViewGroup viewGroup : widgetList) {
        layoutGroup.addView(viewGroup);
    }
}
    /**
     * 修改控件的显示状态
     *
     * @param showFlag
     */
    private void changWidgetStatus(boolean showFlag) {
        for (ViewGroup viewGroup : widgetList) {
            if (viewGroup instanceof EditeWidget) {
                EditeWidget editeWidget = (EditeWidget) viewGroup;
                if (showFlag) {
                    editeWidget.showData();
                } else {
                    editeWidget.changeData();
                }
            } else if (viewGroup instanceof SingleChoiceWidget) {
                SingleChoiceWidget singleChoiceWidget = (SingleChoiceWidget) viewGroup;
                if (showFlag) {
                    singleChoiceWidget.showData();
                } else {
                    singleChoiceWidget.changeData();
                }
            } else if (viewGroup instanceof MultipleChoiceWidget) {
                MultipleChoiceWidget multipleChoiceWidget = (MultipleChoiceWidget) viewGroup;
                if (showFlag) {
                    multipleChoiceWidget.showData();
                } else {
                    multipleChoiceWidget.changeData();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        refreshTakePictureWidget();
        super.onResume();
    }

    private void refreshTakePictureWidget() {
        for (ViewGroup viewGroup : widgetList) {
            if (viewGroup instanceof TakePictrueWidget) {
                TakePictrueWidget takePictrueWidget = (TakePictrueWidget) viewGroup;
                takePictrueWidget.refreshData();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_tianbao:
                dataSaveCheck();
                break;
            case R.id.save_tianbao:
                saveData();
                break;
            default:
                break;
        }
    }

    /**
     * 点击了保存按钮，检查是否有没有填的数据，并将其插入数据库
     *
     * @return
     */
    private void saveData() {
        List<UpItem> upItems = new ArrayList<UpItem>();
        if (!showFlag) {
            //获取控件数据 并检查是否规范  并写入数据库
            for (ViewGroup viewGroup : widgetList) {
                if (viewGroup instanceof EditeWidget) {
                    EditeWidget editeWidget = (EditeWidget) viewGroup;
                    JsonWidget jsonWidget = editeWidget.savedata();
                    if (!checkAddList(jsonWidget, upItems)){
                        return;
                    }
                } else if (viewGroup instanceof SingleChoiceWidget) {
                    SingleChoiceWidget singleChoiceWidget = (SingleChoiceWidget) viewGroup;
                    JsonWidget jsonWidget = singleChoiceWidget.savedata();
                    if (!checkAddList(jsonWidget, upItems)){
                        return;
                    }
                } else if (viewGroup instanceof MultipleChoiceWidget) {
                    MultipleChoiceWidget multipleChoiceWidget = (MultipleChoiceWidget) viewGroup;
                    JsonWidget jsonWidget = multipleChoiceWidget.savedata();
                    if (!checkAddList(jsonWidget, upItems)){
                        return;
                    }
                }else if (viewGroup instanceof DateTimeWidget) {
                    DateTimeWidget dateTimeWidget = (DateTimeWidget) viewGroup;
                    JsonWidget jsonWidget = dateTimeWidget.savedata();
                    if (!checkAddList(jsonWidget, upItems)){
                        return;
                    }
                }
            }
            //封装成要上传的json 存入数据库
            saveToDB(upItems);
            //通知各个控件已经保存，成显示状态，不能修改
            changWidgetStatus(true);
        } else {
            saveButton.setText("保存");
            showFlag = false;
            changWidgetStatus(false);
        }

    }

    /**
     * 检查并加入到list中 为存入数据库做准备
     *
     * @param jsonWidget
     * @param upItems
     * @return
     */
    private boolean checkAddList(JsonWidget jsonWidget, List<UpItem> upItems) {
        if (!checkRequiredAndVlaue(jsonWidget)){
            return  false;
        }
        UpItem upItem = new UpItem();
        upItem.setStoreId(storeId);
        upItem.setProejctId(projiectId+"");
        upItem.setControlID(jsonWidget.getID());
        upItem.setControlValue(jsonWidget.getDefaultValue());
        upItem.setCreateDate(DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss"));
        upItems.add(upItem);
        return true;
    }

    /**
     * 检查是否是必填
     * @param jsonWidget
     * @return
     */
    private boolean checkRequiredAndVlaue(JsonWidget jsonWidget){
        //如果是必填
        if (WidgetType.data_required_yes.equals(jsonWidget.getRequired())){
            String value= jsonWidget.getDefaultValue();
            //值为null 或者""时不满足条件
            if (null == value ||("").equals(value)) {
                Toast.makeText(this, jsonWidget.getName()+"要求必填！", Toast.LENGTH_LONG).show();
                return  false;
            }
            switch (WidgetType.getWidgetType(jsonWidget.getControlType())) {
                case WidgetType.type_text:
                if (!checkEditextValue(jsonWidget)) {
                    return false;
                }
                    break;
                case WidgetType.type_sigleChoice:
                    break;

                case WidgetType.type_multChoice:
                    break;

                default:
                    break;
            }
        }
        return  true;
    }

    /**
     * 检查文本框时 填写的数字是否规范
     * @param jsonWidget
     * @return
     */
    private  boolean checkEditextValue(JsonWidget jsonWidget){
        String maxVlaue=jsonWidget.getMaxValue();
        String minValue=jsonWidget.getMinValue();
        String value= jsonWidget.getDefaultValue();
      //是 填数字 且 最大值不为空 且最小值不为空
    if (!WidgetType.editext_data_type_text.equals(jsonWidget.getDataType())&&maxVlaue!=null&&minValue!=null){
        if (Float.valueOf(value)<Float.valueOf(minValue)||Float.valueOf(value)>Float.valueOf(maxVlaue)){
            Toast.makeText(this, jsonWidget.getName()+"填写范围不符合规范！", Toast.LENGTH_LONG).show();
            return  false;
        }
    }
        return  true;
}
    /**
     * 将集合存入到数据库
     *
     * @param upItems
     */
    private void saveToDB(List<UpItem> upItems) {
        UpJson upJson = new UpJson();
        upJson.setUserId(userId);
        upJson.setItems(upItems);
        String jsonString = JSON.toJSONString(upJson);
        Log.i("test", "savedb:jsonString---" + jsonString);
        List<DynamicTinbaoDB> dynamicTinbaoDBs = new ArrayList<DynamicTinbaoDB>();
        DynamicTinbaoDB dynamicTinbaoDB = new DynamicTinbaoDB();
        dynamicTinbaoDB.setStoreId(storeId);
        dynamicTinbaoDB.setUserId(userId);
        dynamicTinbaoDB.setCreateDate(dateYMD);
        dynamicTinbaoDB.setJsonString(jsonString);
        dynamicTinbaoDBs.add(dynamicTinbaoDB);
        dynamicTinbaoDB.setProejctId(projiectId);
        dynamicTinbaoDB.setTableId(tableId);
        boolean flag = dbDatabaseUtil.insertDynamicTianBao(dynamicTinbaoDBs);
        if (flag) {
//            saveButton.setText("修改");
//            showFlag = true;
            Toast.makeText(this, "数据保存成功", Toast.LENGTH_LONG).show();
             TianBaoActivity.this.finish();
        } else {
            Toast.makeText(this, "数据保存失败", Toast.LENGTH_LONG).show();
        }
    }

    private String checkDbhasSku(int id, UpJson upJson) {
        if (upJson != null) {
            List<UpItem> list = upJson.getItems();
            for (UpItem upItem : list) {
                if (id == upItem.getControlID()) {
                    Log.i("test", "id:" + id + "   value:" + upItem.getControlValue());
                    return upItem.getControlValue();
                }
            }
        }
        return null;

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        nowPicturePath = savedInstanceState.getString("nowPicturePath");
        pictureControlId = savedInstanceState.getInt("pictureControlId");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("nowPicturePath", nowPicturePath);
        outState.putInt("pictureControlId", pictureControlId);
        super.onSaveInstanceState(outState);
    }

    /**
     * 拍照
     */
    private void openCamera() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(this, "当前sd卡不可用，请检查是否开启相应权限！", Toast.LENGTH_LONG).show();
            return;
        }
        dateHMS = DateUtils.getDateStr("HH_mm_ss");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = dateHMS + ".jpg";
        File file = new File(rootSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        nowPicturePath = rootSavePath + "/" + fileName;
        Uri imageUri = Uri.fromFile(new File(file, fileName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constant.take_picture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.take_picture:
                    File file = new File(nowPicturePath);
                    if (!file.exists()) {
                        Toast.makeText(this, "对不起，拍照失败", Toast.LENGTH_LONG).show();
                        return;
                    }
                    PictureInfo caremaInfo = new PictureInfo();
                    caremaInfo.setPicturePath(nowPicturePath);
                    caremaInfo.setUserId(userId);
                    caremaInfo.setCreateDate(DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss"));
                    caremaInfo.setLat(Location.Lat);
                    caremaInfo.setLng(Location.Lng);
                    caremaInfo.setControlId(pictureControlId);
                    caremaInfo.setUserId(userId);
                    caremaInfo.setStoreId(storeId);

                    if (dbDatabaseUtil == null) {
                        dbDatabaseUtil = new DataBaseUtils(TianBaoActivity.this);
                    }
                    Boolean flag = dbDatabaseUtil.insertPictures(caremaInfo);
                    if (!flag) {
                        Toast.makeText(this, "保存数据失败，请重拍！", Toast.LENGTH_LONG).show();
                        file.delete();
                        return;
                    }
                    handler.sendEmptyMessage(Constant.TakePictrueRestart);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dataSaveCheck();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dataSaveCheck() {
        if (showFlag) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("未保存数据，是否退出？");
            builder.setCancelable(false);
            builder.setPositiveButton("继续退出",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
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

    LocationClient mLocationClient;
    public static  final  int loaction_succeed=11;
    private void startLocation() {
        showProgressDialog("正在获取地理位置....");
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(getApplicationContext());
        }
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.disableCache(false);
        option.setScanSpan(1000*30);
        option.setIgnoreKillProcess(true);//设置是否退出定位进程 true:不退出进程； false:退出进程，默认为true
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // TODO Auto-generated method stub
                if (bdLocation != null) {
                    if (bdLocation.getLatitude() != 0&& bdLocation.getLongitude() != 0) {
                        Location.Lat=bdLocation.getLatitude();
                        Location.Lng=bdLocation.getLongitude();
                        Location.Time=bdLocation.getTime();
                        Message message = new Message();
                        message.what=loaction_succeed;
                        handler.sendMessage(message);
    //                    Log.i("test","mLocationClient"+Location.Lat+"---"+ Location.Lng+"---"+Location.Time);
                    }
                }else{
                }
            }
        });
        mLocationClient.start();
        mLocationClient.requestLocation();// 再次启动一个定位，并在页面销毁时结束
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null) {
            mLocationClient.stop();
            mLocationClient=null;
        }
    }

}
