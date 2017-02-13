package com.essence.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableSelectActivity extends AppCompatActivity {
    private ListView listview;
    private SimpleAdapter adapter;
    private DataBaseUtils dataBaseUtils;
    private LinkedHashMap<String,Integer> tabmap;
    private Context context;
    private ArrayList<LinkedHashMap<String, Object>> data;
    private RelativeLayout backBtn;
    private int projectId;
    private int storeId;
    private String storeName;
    private String projectName;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_table_activity);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        projectId=intent.getIntExtra("projectId",-1);
        storeId=intent.getIntExtra("storeId",-1);
        projectName=intent.getStringExtra("projectName");

        storeName=intent.getStringExtra("storeName");
        title = (TextView) findViewById(R.id.table_title);
        title.setText(storeName);
        backBtn = (RelativeLayout) findViewById(R.id.back_table);
        dataBaseUtils=new DataBaseUtils(this);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableSelectActivity.this.finish();
            }
        });
        listview =(ListView)findViewById(R.id.table_list);
        data = getData();
        adapter = new SimpleAdapter(this,data , R.layout.dialog_bar_listview, new String[]{"titlename"}, new int[]{R.id.dialog_title_name});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map=data.get(position);
                String selectName= (String) map.get("titlename");
             //   Toast.makeText(TableSelectActivity.this, "您选择了："+selectName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TableSelectActivity.this,TianBaoActivity.class);

                intent.putExtra("projiectId",projectId);
                intent.putExtra("storeId",storeId);
                intent.putExtra("tableId",tabmap.get(selectName));
                intent.putExtra("storeName",storeName);
                startActivity(intent);
            }
        });
    }
    private ArrayList<LinkedHashMap<String, Object>> getData(){
        tabmap=dataBaseUtils.queryTableIdByProjectId(projectId);
        Log.i("test","projectName--"+projectName+",projectId--"+projectId);
        ArrayList<LinkedHashMap<String, Object>> arrayList = new ArrayList<LinkedHashMap<String,Object>>();
        for (String keyName:tabmap.keySet()){
            LinkedHashMap<String, Object> tempHashMap = new LinkedHashMap<String, Object>();
            tempHashMap.put("titlename", keyName);
            arrayList.add(tempHashMap);
        }
        return arrayList;
    }
}
