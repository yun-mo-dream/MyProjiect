package com.essence.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19.
 */

public class TableSelectDialog extends Dialog {
    private ListView listview;
    private SimpleAdapter adapter;
    private int projectId;
    private DataBaseUtils dataBaseUtils;
    private Map<String,Integer> tabmap;
    private Context context;
    private ArrayList<HashMap<String, Object>> data;
    public TableSelectDialog(Context context,int projectId) {
        super(context);
        this.projectId=projectId;
        dataBaseUtils=new DataBaseUtils(context);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_table);
        listview =(ListView)findViewById(R.id.table_listview);
        setTitle("请选择填报表单");
        data = getData();
        adapter = new SimpleAdapter(getContext(),data , R.layout.dialog_bar_listview, new String[]{"titlename"}, new int[]{R.id.dialog_title_name});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map=data.get(position);
                Toast.makeText(context, "您选择了："+map.get("titlename"), Toast.LENGTH_LONG).show();
                switch (position){
                    case  0:
                }
            }
        });
    }

    private ArrayList<HashMap<String, Object>> getData(){
       tabmap=dataBaseUtils.queryTableIdByProjectId(projectId);
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();
        for (String keyName:tabmap.keySet()){
            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
            tempHashMap.put("titlename", keyName);
            arrayList.add(tempHashMap);
        }
        return arrayList;
    }
}
