package com.essence.UploadUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/2/9.
 */

public class GetNetPictureAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private ProgressDialog m_Dialog;
    private Context context;
    private OnGetNetPictureListener onGetNetPictureListener;
    public GetNetPictureAsyncTask(Context context){
        this.context=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage( "正在从网络拉取图片....");
        m_Dialog.setCancelable(true);
        m_Dialog.show();
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap=null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5*1000);
            conn.connect();
            InputStream in=conn.getInputStream();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();

            byte[] buffer=new byte[1024];
            int len = 0;
            while((len=in.read(buffer))!=-1){
                bos.write(buffer,0,len);
            }
            byte[] dataImage=bos.toByteArray();
            bos.close();
            in.close();
             bitmap= BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
        } catch (Exception e) {

        }
        return  bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (m_Dialog!=null && m_Dialog.isShowing()){
            m_Dialog.dismiss();
            m_Dialog=null;
        }
        if (onGetNetPictureListener!=null){
            onGetNetPictureListener.responsePicture(bitmap);
        }
    }

    public void setOnGetNetPictureListener(OnGetNetPictureListener onGetNetPictureListener) {
        this.onGetNetPictureListener = onGetNetPictureListener;
    }

    public interface  OnGetNetPictureListener{
        void responsePicture(Bitmap bitmap);
    }
}
