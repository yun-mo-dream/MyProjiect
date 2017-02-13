package com.essence.UploadUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.OtherUtils.SharePreferenceUtil;
import com.alibaba.fastjson.JSON;
import com.com.essence.selfclass.DynamicTinbaoDB;
import com.com.essence.selfclass.JsonUploadPictureResult;
import com.essece.networkutils.ConstantNetwork;
import com.essece.networkutils.MyHttpClient;
import com.essence.dbmanager.DataBaseUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class UploadDataAsyncTask extends AsyncTask<Void, Integer, Void> {
    Context context;
    DataBaseUtils dataBaseUtils;
    List<DynamicTinbaoDB> dataList;
    int totalCount = 0;
    int succedCount = 0;
    int failedCount = 0;
    int userId;
    ProgressDialog m_Dialog;

    public UploadDataAsyncTask(Context context) {
        this.context = context;
        dataBaseUtils = new DataBaseUtils(context);
        userId = SharePreferenceUtil.getUserId(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < dataList.size(); i++) {
            int position = i + 1;
            DynamicTinbaoDB dynamicTinbaoDB = dataList.get(i);
            Log.i("upjson", "upJson:" + dynamicTinbaoDB.getJsonString());
            String httpResult = MyHttpClient.UploadPicture(ConstantNetwork.url_upload_data, dynamicTinbaoDB.getJsonString());
            if (!"-1".equals(httpResult)) {
                JsonUploadPictureResult jsonUploadPictureResult = JSON.parseObject(httpResult, JsonUploadPictureResult.class);
                if (jsonUploadPictureResult.getErrorCode() == 0) {
                    dataBaseUtils.deleteDynamicTianbao(userId, dynamicTinbaoDB.getProejctId(), dynamicTinbaoDB.getStoreId(), dynamicTinbaoDB.getTableId(), dynamicTinbaoDB.getCreateDate());
                    succedCount++;
                    publishProgress(position);
                } else {
                    failedCount++;
                    publishProgress(position);
                }

            } else {
                Log.i("pictrue_f", "failed:" + position);
                failedCount++;
                publishProgress(position);
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        dataList = dataBaseUtils.queryDynamicTianBaoByUserId(userId);
        totalCount = dataList.size();
        Log.i("data_up", "total size:" + totalCount);
        m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle("提示");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        m_Dialog.setMessage("共填报" + totalCount + "张表单\n正在上传第1张....");
        m_Dialog.setIndeterminate(false);
        m_Dialog.setCancelable(true);
        m_Dialog.setMax(totalCount);
        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.incrementProgressBy(0);
        m_Dialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (totalCount==0){
            if (m_Dialog != null && m_Dialog.isShowing()) {
                m_Dialog.dismiss();
            }
            return;
        }
        String message;
        if (failedCount>0){
             message =  "有 " + failedCount + " 张填报表单数据上传失败，请将失败的重新上传";
        }else{
            message = "填报表单数据上传成功";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        if (values[0] == totalCount) {
            if (m_Dialog != null && m_Dialog.isShowing()) {
                m_Dialog.dismiss();
            }
        } else {
            if (m_Dialog != null && m_Dialog.isShowing()) {
                m_Dialog.setMessage("共" + totalCount + "张表单\n已传" + succedCount + "张,失败" + failedCount + "张\n正在上传第" + String.valueOf(succedCount + failedCount + 1) + "张....");
                m_Dialog.setProgress(values[0]);
            }
        }
        super.onProgressUpdate(values);
    }
}
