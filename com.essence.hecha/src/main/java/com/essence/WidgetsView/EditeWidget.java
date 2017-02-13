package com.essence.WidgetsView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.com.essence.selfclass.JsonWidget;
import com.com.essence.selfclass.WidgetType;
import com.essence.activitys.TianBaoActivity;
import com.essence.hechaSystem.R;

import java.util.Calendar;

public class EditeWidget extends FrameLayout {

	private JsonWidget jsonWidget;
	private TextView nameTextView;
	private EditText editText;
	private	Context context;
	private	String controlName;
	public EditeWidget(Context context, JsonWidget jsonWidget) {
		super(context);
		inflate(context, R.layout.widget_edittext_bar, this);
		this.jsonWidget = jsonWidget;
		controlName = jsonWidget.getName();
		this.context =context;
		inite();
	}
	
	private void inite() {
		nameTextView = (TextView) findViewById(R.id.name_editext);
		WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int maxpixels =windowManager.getDefaultDisplay().getWidth()*2/3;
		nameTextView.setMaxWidth(maxpixels);
		editText = (EditText) findViewById(R.id.edit_text);
		nameTextView.setText(jsonWidget.getName());
		String defaultValue=jsonWidget.getDefaultValue();
		setHint();
		checkDataTypeForDateTime();
		if (defaultValue!=null&&!"".equals(defaultValue)){
			editText.setText(defaultValue);
		}
		if (jsonWidget.getShowFlag()){
			editText.setEnabled(false);
		} else {
			editText.setEnabled(true);
		}
	}

	/**
	 * 设置提示
	 */
    private void setHint(){
	String hinttext="";
	if (WidgetType.data_required_yes.equals(jsonWidget.getRequired())) {
		hinttext=hinttext+"必填";
	}else {
		hinttext=hinttext+"非必填";
	}
	hinttext=hinttext+jsonWidget.getDataType();
	if (WidgetType.editext_data_type_text.equals(jsonWidget.getDataType())) {
		//主要从填数字切换到可以填文本
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
	}else if (WidgetType.editext_data_type_int.equals(jsonWidget.getDataType())){
		if (jsonWidget.getMaxValue()!=null&&jsonWidget.getMinValue()!=null){
			hinttext = hinttext+"范围 "+jsonWidget.getMinValue()+"--"+jsonWidget.getMaxValue();
		}

	}else if (WidgetType.editext_data_type_float.equals(jsonWidget.getDataType())){
		if (jsonWidget.getMaxValue()!=null&&jsonWidget.getMinValue()!=null){
			hinttext = hinttext+" 值范围 "+jsonWidget.getMinValue()+"--"+jsonWidget.getMaxValue();
		}
	}
	editText.setHint(hinttext);
}

	private void checkDataTypeForDateTime(){
		if (WidgetType.editext_data_type_datetime.equals(jsonWidget.getDataType())){
			editText.setInputType(InputType.TYPE_NULL);
			editText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) {
						editText.clearFocus();
						Calendar calendar= Calendar.getInstance();
						int year=calendar.get(Calendar.YEAR);
						int month=calendar.get(Calendar.MONTH);
						int day=calendar.get(Calendar.DAY_OF_MONTH);
						new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								String contextValue=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
								editText.setText(contextValue);
							}
						}, year, month, day).show();
					}
				}
			});
		}
	}
	public JsonWidget savedata() {
		this.jsonWidget.setDefaultValue(editText.getText().toString());
		return jsonWidget;
	}

	public void showData() {
		editText.setEnabled(false);
	}
	public void changeData(){
		editText.setEnabled(true);
	}	
	public void setValue(String value){
		editText.setText(value);
	}
	
	 /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public  int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
