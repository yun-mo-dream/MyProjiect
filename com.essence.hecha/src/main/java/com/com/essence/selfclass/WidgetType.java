package com.com.essence.selfclass;

public class WidgetType {


	public static final int type_text=0;
	public static final int type_sigleChoice=1;
	public static final int type_multChoice=2;
	public static final int type_camera=3;
	public static final int type_default=4;
	public static final int type_datetime=5;

	public static final String editext_data_type_text="文本";
	public static final String editext_data_type_float="小数";
	public static final String editext_data_type_int="整数";
	public static final String editext_data_type_datetime="日期";

	public static final String data_required_yes="是";
	public static final String data_required_no="否";


public static int getWidgetType(String type){
		if (type.equals("Input")) {
			return type_text;
		}else if (type.equals("Select")) {
			return type_sigleChoice;
		}else if (type.equals("CheckBox")) {
			return type_multChoice;
		}else if (type.equals("Camara")) {
			return type_camera;
		}else {
			return type_default;
		}
	}
}
