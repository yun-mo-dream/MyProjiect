package com.essence.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.OtherUtils.SharePreferenceUtil;
import com.OtherUtils.ShowPictureUtils.DateUtils;
import com.OtherUtils.ShowPictureUtils.TelephoneManagerUtil;
import com.com.essence.selfclass.KaoqinDb;
import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

import java.util.List;

public class WorkActivity extends AppCompatActivity implements WorkRecycleviewAdapter.RecycleAdapterOnclickListener,View.OnClickListener{
    RelativeLayout backBtn;
    RelativeLayout addWorkBtn;
    public static  final  int request_select_project_store=1;
    private String dateYMD;
    private String time;
    private int userId;
    private DataBaseUtils dataBaseUtils;
    private RecyclerView recyclerView;
    private WorkRecycleviewAdapter workRecycleviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backBtn=(RelativeLayout) findViewById(R.id.back_work);
        backBtn.setOnClickListener(this);
        addWorkBtn=(RelativeLayout) findViewById(R.id.work_add);
        addWorkBtn.setOnClickListener(this);
        dateYMD= DateUtils.getDateStr("yyyy-MM-dd");
        userId= SharePreferenceUtil.getUserId(this);
        dataBaseUtils= new DataBaseUtils(this);
        recyclerView= (RecyclerView) findViewById(R.id.work_list);
        workRecycleviewAdapter = new WorkRecycleviewAdapter(this);
        workRecycleviewAdapter.setRecycleAdapterOnclickListener(this);
        bindKaoqinDBToWindow();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LineraLayoutRecycleviewDivider lineraLayoutRecycleviewDivider = new LineraLayoutRecycleviewDivider(this);
        lineraLayoutRecycleviewDivider.setDividerHeight(1);
        lineraLayoutRecycleviewDivider.setDividerColor(Color.parseColor("#D0D0D0"));
        recyclerView.addItemDecoration(lineraLayoutRecycleviewDivider);
        recyclerView.setAdapter(workRecycleviewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_work:
                this.finish();
                break;
            case R.id.work_add:
                Intent intent = new Intent(WorkActivity.this,SelectProjectStoreAcitivty.class);
                startActivityForResult(intent,request_select_project_store);
                break;
            default:
                break;
        }
    }

    private void bindKaoqinDBToWindow(){
        List<KaoqinDb> kaoqinDbList = dataBaseUtils.queryKaoqinByUserIdDate(userId,dateYMD);
        workRecycleviewAdapter.setWorkInfos(kaoqinDbList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==request_select_project_store){
                Log.i("test", "onActivityResult: RESULT_OK-"+RESULT_OK);
                addKaoqinToDB(data);
            }
        }
    }

    private void addKaoqinToDB(Intent data){
        int projectId=data.getIntExtra("projectId",-1);
        int storeId=data.getIntExtra("storeId",-1);
        String  projectName=data.getStringExtra("projectName");
        String  storeName=data.getStringExtra("storeName");
        String createTime=DateUtils.getDateStr("HH:mm:ss");
        KaoqinDb kaoqinDb =  new KaoqinDb();
        kaoqinDb.setUserId(userId);
        kaoqinDb.setDate(dateYMD);
        kaoqinDb.setTime(createTime);
        kaoqinDb.setIsUpload(Constant.ISUPLOAD_NO);
        kaoqinDb.setProjectId(projectId);
        kaoqinDb.setProjectName(projectName);
        kaoqinDb.setStoreName(storeName);
        kaoqinDb.setStoreID(storeId);
        kaoqinDb.setPhoneImei(TelephoneManagerUtil.getIMEI(this));
        dataBaseUtils.insertKaoQin(kaoqinDb);
        bindKaoqinDBToWindow();
    }

    @Override
    public void gotoWorkOnclick(KaoqinDb kaoqinDb) {
        tianBaoBtn(kaoqinDb);
    }

    @Override
    public void qianDaoOnclick(KaoqinDb kaoqinDb) {
        qianDaoBtn(kaoqinDb);
    }

    @Override
    public void qianChuOnclick(KaoqinDb kaoqinDb) {
        qianChuBtn(kaoqinDb);
    }

    private void  qianDaoBtn(KaoqinDb kaoqinDb){
        Log.i("test", "StoreName"+kaoqinDb.getStoreName());
        double lat=0;
        double lng=0;
        String signinTime=  DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss");
        String createTime=kaoqinDb.getTime();
        dataBaseUtils.updateKaoqinSignin(lat+"",lng+"",signinTime,createTime);
        bindKaoqinDBToWindow();
    }
    private void  qianChuBtn(KaoqinDb kaoqinDb){
        Log.i("test", "StoreName"+kaoqinDb.getStoreName());
        double lat=0;
        double lng=0;
        String signinTime=  DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss");
        String createTime=kaoqinDb.getTime();
        dataBaseUtils.updateKaoqinSignout(lat+"",lng+"",signinTime,createTime);
        bindKaoqinDBToWindow();
    }
    private void  tianBaoBtn(KaoqinDb kaoqinDb){
                Intent intent = new Intent(WorkActivity.this,TableSelectActivity.class);
                intent.putExtra("projectId",kaoqinDb.getProjectId());
                intent.putExtra("projectName",kaoqinDb.getProjectName());
                intent.putExtra("storeId",kaoqinDb.getStoreID());
                intent.putExtra("storeName",kaoqinDb.getStoreName());
                startActivity(intent);
    }
}
