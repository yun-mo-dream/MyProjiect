package com.essence.activitys;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essence.hechaSystem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/18.
 */

public class SelectProjectStoreAdapter extends RecyclerView.Adapter<SelectProjectStoreAdapter.SelectViewHoler>{
    private Context context;
    private Map<String,Integer> dataMap;
    private List<String> nameList= new ArrayList<String>();
    private OnItemSelectLisenter onItemSelectLisenter;
    public SelectProjectStoreAdapter(Context context){
        this.context=context;
    }
    public void setDataMap(Map<String, Integer> dataMap) {
        this.dataMap = dataMap;
        nameList.clear();
        nameList.addAll(dataMap.keySet());
        notifyDataSetChanged();
    }
    @Override
    public SelectViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(context).inflate(R.layout.bar_select_adapter,parent,false);
        final SelectViewHoler selectViewHoler = new SelectViewHoler(layoutView);
        selectViewHoler.rootRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             int postion=(Integer)selectViewHoler.getTag();
                String  name=nameList.get(postion);
                int id=dataMap.get(name);
                if (onItemSelectLisenter!=null){
                    onItemSelectLisenter.selectItem(id,name);
                }
            }
        });
        return selectViewHoler ;
    }

    @Override
    public void onBindViewHolder(SelectViewHoler holder, int position) {
         String nameKey =nameList.get(position);
         int idValue=dataMap.get(nameKey);
         holder.barName.setText(nameKey);
         holder.setTag(position);
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public void setOnItemSelectLisenter(OnItemSelectLisenter onItemSelectLisenter) {
        this.onItemSelectLisenter = onItemSelectLisenter;
    }


    public class SelectViewHoler extends RecyclerView.ViewHolder{
        RelativeLayout rootRelativeLayout;
        TextView barName;
        Object tag;
        public SelectViewHoler(View itemView) {
            super(itemView);
            rootRelativeLayout =(RelativeLayout) itemView.findViewById(R.id.select_bar);
            barName =(TextView) itemView.findViewById(R.id.bar_name);
        }
        void setTag(Object tag){
            this.tag=tag;
        }
        Object getTag(){
            return tag;
        }
    }

    interface  OnItemSelectLisenter{
        void selectItem(int id,String selectName);
    }
}
