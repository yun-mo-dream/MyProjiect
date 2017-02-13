package com.essence.activitys;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.essence.hechaSystem.R;


public class ShowPictureDialog extends Dialog{
private ImageView imageView;
	private Bitmap bitmap;
	public ShowPictureDialog(Context context,Bitmap bitmap) {
		super(context, R.style.ImageloadingDialogStyle);
		this.bitmap=bitmap;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_showpicture);
		imageView = (ImageView) findViewById(R.id.showProductPicture);
		imageView.setImageBitmap(bitmap);
		
	}

}
