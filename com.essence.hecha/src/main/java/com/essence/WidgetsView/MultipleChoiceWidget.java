package com.essence.WidgetsView;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.essence.selfclass.JsonWidget;
import com.essence.hechaSystem.R;

public class MultipleChoiceWidget extends FrameLayout {
	private JsonWidget jsonWidget;
	private TextView nameTextView;
	private TextView contextTextView;
	private RelativeLayout choiceBar;
	private String[] dataArray;
	private List<String> choosedList= new ArrayList<String>();
	private Context context;
	private String controlName;
	public MultipleChoiceWidget(Context context, JsonWidget jsonWidget) {
		super(context);
		inflate(context, R.layout.widget_mult_bar, this);
		this.context = context;
		this.jsonWidget = jsonWidget;
		 controlName = jsonWidget.getName();	
		inite();
	}

	private void inite() {
		nameTextView = (TextView) findViewById(R.id.name_mult_select);
		nameTextView.setText(controlName);
	    contextTextView =(TextView) findViewById(R.id.text_mult_select);
		choiceBar = (RelativeLayout) findViewById(R.id.mult_select_bar);
//        nameTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String pictureNo=tianbaoObjiect.getControlLabel().replace(":", "");
//				ShowPictureDialog showPictureDialog = new ShowPictureDialog(context,pictureNo);
//				showPictureDialog.show();
//			}
//		});
		
		choiceBar.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
			    dataArray= jsonWidget.getOptions();
				boolean[] selectTaste = new boolean[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					selectTaste[i]=false;
				}				
				choosedList.clear();		
				AlertDialog.Builder builder = new AlertDialog.Builder(context);	
				builder.setTitle("多项选择");
				// 设置多选项
				builder.setMultiChoiceItems(dataArray,selectTaste,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1,
									boolean arg2) {
								// TODO Auto-generated method stub
								if (arg2) {
									choosedList.add(dataArray[arg1]);
								} else {
									choosedList.remove(dataArray[arg1]);
								}
							}
						});
				// 设置确定按钮
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						int size = choosedList.size();
					  String selecteTaseString="";
						for (int i = 0; i < size; i++) {
							if (i != 0) {
								selecteTaseString += (","+choosedList.get(i));
							}else {
								selecteTaseString += choosedList.get(i);
							}
							
						}
						contextTextView.setText(selecteTaseString);
					}
				});
				// 设置取消按钮
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				});

				builder.create().show();
				
			}
		});	
		if (jsonWidget.getShowFlag()) {
			showData();
		} 
	}
	public JsonWidget savedata() {
		jsonWidget.setDefaultValue(contextTextView.getText().toString());
		return jsonWidget;
	}
	public void showData() {
		choiceBar.setClickable(false);
		}
	public void changeData(){
		choiceBar.setClickable(true);
	}
	public void setValue(String value){
		contextTextView.setText(value);
	}	
}

