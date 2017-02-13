package com.essence.WidgetsView;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.com.essence.selfclass.JsonWidget;
import com.essence.hechaSystem.R;

public class SingleChoiceWidget extends FrameLayout {

	private JsonWidget jsonWidget;
	private TextView nameTextView;
	private Spinner singeSpinner;
	private String[] dataArray;
	private Context context;
	private	String controlName;
	public SingleChoiceWidget(Context context, JsonWidget jsonWidget) {
		super(context);
		inflate(context, R.layout.widget_single_bar, this);
		this.context = context;
		this.jsonWidget = jsonWidget;
		controlName = jsonWidget.getName();
		inite();

	}

	private void inite() {
		nameTextView = (TextView) findViewById(R.id.name_single_select);
		WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int maxpixels =windowManager.getDefaultDisplay().getWidth()*2/3;
		nameTextView.setMaxWidth(maxpixels);
		nameTextView.setText(controlName);
//		nameTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String pictureNo=tianbaoObjiect.getControlLabel().replace(":", "");
//				ShowPictureDialog showPictureDialog = new ShowPictureDialog(context,pictureNo);
//				showPictureDialog.show();
//			}
//		});
		singeSpinner = (Spinner) findViewById(R.id.single_select_spiner);
		
		dataArray=jsonWidget.getOptions();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, dataArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		singeSpinner.setAdapter(adapter);

		String value = jsonWidget.getDefaultValue();

		for(int i=0;i<dataArray.length;i++){
			if (dataArray[i].equals(value)) {
				singeSpinner.setSelection(i,true);
			}
		}
		if (jsonWidget.getShowFlag()) {
			showData();
		} 
	}
	public JsonWidget savedata() {
		String saveString = (String)singeSpinner.getSelectedItem();
		jsonWidget.setDefaultValue(saveString);
		return jsonWidget;
	}
	public void showData() {
		singeSpinner.setEnabled(false);
	}
	
	public void changeData(){
		singeSpinner.setEnabled(true);
	}
	
	
	public void setValue(String value){
		Log.i("tag", "value:  "+value);
		for(int i = 0;i<dataArray.length;i++){
			if (dataArray[i].equals(value)) {
				singeSpinner.setSelection(i);
			}
		}

	}	
}
