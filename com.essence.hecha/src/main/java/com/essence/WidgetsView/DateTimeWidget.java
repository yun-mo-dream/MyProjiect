package com.essence.WidgetsView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.essence.selfclass.JsonWidget;
import com.essence.hechaSystem.R;

/**
 * Created by Administrator on 2017/2/10.
 */

public class DateTimeWidget extends FrameLayout {
    private JsonWidget jsonWidget;
    private TextView nameTextView;
    private EditText editText;
    private	Context context;
    private	String controlName;
    private TextView contextTextView;
    private RelativeLayout choiceBar;
    public DateTimeWidget(Context context, JsonWidget jsonWidget) {
        super(context);
        inflate(context, R.layout.widget_datetime_bar, this);
        this.jsonWidget = jsonWidget;
        this.context =context;
        inite();
    }

    private void inite() {
        nameTextView = (TextView) findViewById(R.id.name_datetime);
        controlName = jsonWidget.getName();
        nameTextView.setText(controlName);
        contextTextView =(TextView) findViewById(R.id.datetime_text);
        choiceBar = (RelativeLayout) findViewById(R.id.datetime_bar);
        choiceBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String contextValue=year+"-"+monthOfYear+"-"+dayOfMonth;
                        contextTextView.setText(contextValue);
                    }
                }, 2017, 1, 1).show();
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
