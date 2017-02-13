package com.essence.activitys;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

import java.util.HashMap;
import java.util.Map;

public class SelectProjectStoreAcitivty extends AppCompatActivity {
    private RelativeLayout backBtn;
    private TextView titleTextview;
    private EditText searchEdit;
    private RecyclerView recyclerView;
    private SelectProjectStoreAdapter selectProjectStoreAdapter;
    private MySelectListener mySelectListener;
    private DataBaseUtils dataBaseUtils;
    private String selectProjectName;
    private int selectProjectId;
    private boolean selectProjectFlag=true;
    private Map<String,Integer> dataMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_project_store_acitivty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseUtils= new DataBaseUtils(this);
        backBtn =(RelativeLayout) findViewById(R.id.back_select);
        titleTextview=(TextView) findViewById(R.id.select_title);
        searchEdit=(EditText) findViewById(R.id.edt_search);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectProjectStoreAcitivty.this.finish();
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serachFilter(s.toString());
            }
        });
        initeData();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initeData(){
        recyclerView=(RecyclerView) findViewById(R.id.recycleview_select);
        selectProjectStoreAdapter = new SelectProjectStoreAdapter(this);
        mySelectListener= new MySelectListener();
        selectProjectStoreAdapter.setOnItemSelectLisenter(mySelectListener);
        dataMap=dataBaseUtils.queryStoresInfoForAllProjects();
        selectProjectStoreAdapter.setDataMap(dataMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LineraLayoutRecycleviewDivider lineraLayoutRecycleviewDivider = new LineraLayoutRecycleviewDivider(this);
        lineraLayoutRecycleviewDivider.setDividerHeight(1);
        lineraLayoutRecycleviewDivider.setDividerColor(Color.parseColor("#D0D0D0"));
        recyclerView.addItemDecoration(lineraLayoutRecycleviewDivider);
        recyclerView.setAdapter(selectProjectStoreAdapter);
    }
    public class MySelectListener implements  SelectProjectStoreAdapter.OnItemSelectLisenter{

        @Override
        public void selectItem(int id, String selectName) {
            if (selectProjectFlag){
                selectProjectName=selectName;
                selectProjectId=id;
                selectProjectFlag=false;
                changeToSetStoreData(selectProjectId,selectProjectName);
            }else{
//                Intent intent = new Intent();
//                intent.putExtra("projectId",selectProjectId);
//                intent.putExtra("projectName",selectProjectName);
//                intent.putExtra("storeId",id);
//                intent.putExtra("storeName",selectName);
//                setResult(Activity.RESULT_OK,intent);
//                SelectProjectStoreAcitivty.this.finish();

//                TableSelectDialog tableSelectDialog = new TableSelectDialog(SelectProjectStoreAcitivty.this,selectProjectId);
//                tableSelectDialog.show();

                Intent intent = new Intent(SelectProjectStoreAcitivty.this,TableSelectActivity.class);
                intent.putExtra("projectId",selectProjectId);
                intent.putExtra("projectName",selectProjectName);
                intent.putExtra("storeId",id);
                intent.putExtra("storeName",selectName);
               startActivity(intent);
            }
         //   Toast.makeText(SelectProjectStoreAcitivty.this,selectName,Toast.LENGTH_LONG).show();
        }
    }

    private void changeToSetStoreData(int projectId,String projectName){
        titleTextview.setText(projectName);
        dataMap=dataBaseUtils.queryStoresInfoStoresByProjectId(projectId);
        selectProjectStoreAdapter.setDataMap(dataMap);
    }

    private void serachFilter(String s){
        if (dataMap!=null&&s!=null){
            Map<String,Integer> serachedMap= new HashMap<String,Integer>();
            for (String name:dataMap.keySet()){
                if (name.contains(s)){
                    serachedMap.put(name,dataMap.get(name));
                }
            }
            selectProjectStoreAdapter.setDataMap(serachedMap);
        }
    }
}
