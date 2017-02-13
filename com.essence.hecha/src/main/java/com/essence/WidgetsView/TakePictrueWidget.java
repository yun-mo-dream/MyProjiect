package com.essence.WidgetsView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.OtherUtils.ShowPictureUtils.AlbumItemAty;
import com.OtherUtils.ShowPictureUtils.AlignLeftGallery;
import com.OtherUtils.ShowPictureUtils.Constant;
import com.OtherUtils.ShowPictureUtils.DateUtils;
import com.OtherUtils.ShowPictureUtils.GallyAdapter;
import com.com.essence.selfclass.JsonWidget;
import com.com.essence.selfclass.PictureInfo;
import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

public class TakePictrueWidget extends FrameLayout {
	private Context context;
	private JsonWidget jsonWidget;
	public  Handler handler;
	private AlignLeftGallery alignLeftGallery;
	private List<String> picturePathList = new ArrayList<String>();
	private int pictureControlId;
	private DataBaseUtils dataBaseUtils;
	private int storeId;
	public TakePictrueWidget(Handler handler, Context context,JsonWidget jsonWidget,int storeId) {
		super(context);
		inflate(context, R.layout.widget_take_picture_bar, this);
		this.context = context;
		this.handler = handler;
		this.jsonWidget = jsonWidget;
		this.storeId=storeId;
		inite();
	}

	private void inite() {
		dataBaseUtils= new DataBaseUtils(context);
		ImageView takePicture = (ImageView) findViewById(R.id.view_take_picture);
		alignLeftGallery = (AlignLeftGallery) findViewById(R.id.take_pictrue_widget_gallery);	
		//----------------------从标题上截取照片类型---------
		pictureControlId = jsonWidget.getID();
		TextView name = (TextView) findViewById(R.id.take_pictrue_name);
		WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int maxpixels =windowManager.getDefaultDisplay().getWidth()*4/5;
		name.setMaxWidth(maxpixels);
		name.setText(jsonWidget.getName());

		refreshData();
		takePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.arg1=pictureControlId;
				message.what = Constant.startTakePictrue;
				handler.sendMessage(message);
			}
		});
	}


	public void refreshData() {
		picturePathList.clear();
		final String date= DateUtils.getDateStr("yyyyMMdd");
		List<PictureInfo> pictureInfos=dataBaseUtils.queryPicturesByUserIdControlId(storeId,jsonWidget.getID());
		List<String> paths= new ArrayList<String>();
		for (PictureInfo pictureInfo:pictureInfos){
			paths.add(pictureInfo.getPicturePath());
		}
		picturePathList.addAll(paths);
		alignLeftGallery.setAdapter(new GallyAdapter(context, picturePathList));
		alignLeftGallery.setOnItemClickListener(new AlignLeftGallery.IOnItemClickListener() {

					@Override
					public void onItemClick(int position) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, AlbumItemAty.class);
//						intent.putExtra("Cont", pictureTypeString);
//						intent.putExtra("date", date);
//						intent.putExtra("position", position);
//						intent.putExtra("selectPath", picturePathList.get(position));
						intent.putExtra("storeId", storeId);
						intent.putExtra("controlId", jsonWidget.getID());
						intent.putExtra("selectPath", picturePathList.get(position));
//						intent.putExtra("position", position);

						context.startActivity(intent);
					}
				});

	}

	
}
