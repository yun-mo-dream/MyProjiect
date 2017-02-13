package com.essence.activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.OtherUtils.ApkVersionUtil;
import com.OtherUtils.CheckNetworAndGpsUtil;
import com.OtherUtils.SharePreferenceUtil;
import com.com.essence.selfclass.UserInfo;
import com.essence.hechaSystem.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
private Toolbar toolbar;
private RelativeLayout backBtn;
    private LinearLayout checkBtn;
    private LinearLayout clearBtn;
    private LinearLayout changeUserBtn;
    private LinearLayout backData;
    private LinearLayout restoreData;
    private TextView userTextView;
    private TextView versonTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        backBtn = (RelativeLayout) findViewById(R.id.back_set);
        backBtn.setOnClickListener(this);
        userTextView = (TextView) findViewById(R.id.username_text);
        versonTextView = (TextView) findViewById(R.id.version_text);
        UserInfo userInfo = SharePreferenceUtil.getUserInfo(this);
        userTextView.setText(userInfo.getUserName());
        versonTextView.setText(ApkVersionUtil.getVersionName(this));
        checkBtn = (LinearLayout) findViewById(R.id.check_bar);
        checkBtn.setOnClickListener(this);
        clearBtn = (LinearLayout) findViewById(R.id.clear_db_bar);
        clearBtn.setOnClickListener(this);
        changeUserBtn = (LinearLayout) findViewById(R.id.change_user_bar);
        changeUserBtn.setOnClickListener(this);
        backData = (LinearLayout) findViewById(R.id.databack_bar);
        backData.setOnClickListener(this);
        restoreData = (LinearLayout) findViewById(R.id.datarestore_bar);
        restoreData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_set:
                SettingActivity.this.finish();
                break;
            case R.id.check_bar:
                ApkVersionUtil.checkVersion(SettingActivity.this);
                break;
            case R.id.change_user_bar:
                SharePreferenceUtil.clearUserInfo(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
                break;

        }
    }



}
