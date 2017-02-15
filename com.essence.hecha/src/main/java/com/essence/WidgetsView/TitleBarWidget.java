package com.essence.WidgetsView;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essece.networkutils.ConstantNetwork;
import com.essence.UploadUtils.GetNetPictureAsyncTask;
import com.essence.activitys.ShowPictureDialog;
import com.essence.hechaSystem.R;

public class TitleBarWidget extends FrameLayout{
	private Context context;
	private String titleName;
	private LinearLayout barLinearLayout;
	private String imag;
	public TitleBarWidget(Context context,String titleName,String imagPath) {
		super(context);
		inflate(context, R.layout.widget_title_bar, this);
		this.context = context;
		this.titleName = titleName;
		imag=imagPath;
		inite();
	}
	
	private void inite(){
		TextView textView = (TextView) findViewById(R.id.widget_titlebar_name);
		if (imag!=null&&imag.length()>5){
			titleName=titleName+" 图示";
		}
		textView.setText(titleName);
		barLinearLayout = (LinearLayout) findViewById(R.id.widget_titlebar_bar);
		barLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
               showPicture(imag);
			}
		});

	}

	private void showPicture(String imagPath){
		if (imagPath==null||imagPath.length()<5){
			Toast.makeText(context,"没有图片",Toast.LENGTH_LONG).show();
			return;
		}
		GetNetPictureAsyncTask getNetPictureAsyncTask = new GetNetPictureAsyncTask(context);
		getNetPictureAsyncTask.setOnGetNetPictureListener(new GetNetPictureAsyncTask.OnGetNetPictureListener() {
			@Override
			public void responsePicture(Bitmap bitmap) {
                 if (bitmap!=null){
					 ShowPictureDialog showPictureDialog = new ShowPictureDialog(context,bitmap);
					 showPictureDialog.setCanceledOnTouchOutside(true);
					 showPictureDialog.show();
				 }else{
					 Toast.makeText(context,"获取图片失败",Toast.LENGTH_LONG).show();
				 }
			}
		});
		getNetPictureAsyncTask.execute(ConstantNetwork.url_path+"/"+imagPath);
	}


}
