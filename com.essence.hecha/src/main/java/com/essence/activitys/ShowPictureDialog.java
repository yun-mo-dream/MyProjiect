package com.essence.activitys;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.essence.hechaSystem.R;


public class ShowPictureDialog extends Dialog{
private ImageView imageView;
	private Bitmap bitmap;
	private int height ;
	private int width=400;
	public ShowPictureDialog(Context context,Bitmap bitmap) {
		super(context, R.style.ImageloadingDialogStyle);
		this.bitmap=bitmap;

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_showpicture);
		imageView = (ImageView) findViewById(R.id.showProductPicture);
		setImageViewParam();
		imageView.setImageBitmap(bitmap);
		
	}


	private void setImageViewParam(){

		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		int sWidth = wm.getDefaultDisplay().getWidth();
		int bitW=bitmap.getWidth();
		int bitH=bitmap.getHeight();
		float scale=(float) bitmap.getHeight()/(float)bitmap.getWidth();
		if (bitW<bitH){
			width=sWidth/2;
		}else{
			width=sWidth*3/4;
		}
		ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
		height=(int)(width*scale);
		layoutParams.width=width;
		layoutParams.height=height;
		imageView.setLayoutParams(layoutParams);
		Log.i("test","width:"+width+" , height:"+height);

	}
}
