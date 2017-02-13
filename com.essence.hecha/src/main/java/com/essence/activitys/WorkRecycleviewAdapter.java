package com.essence.activitys;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.essence.selfclass.KaoqinDb;
import com.essence.hechaSystem.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/17.
 */

public class WorkRecycleviewAdapter extends RecyclerView.Adapter<WorkRecycleviewAdapter.MyRecycleHolder>{
    private List<KaoqinDb> workInfos= new ArrayList<KaoqinDb>();
    private RecycleAdapterOnclickListener recycleAdapterOnclickListener;
    private Context context;
    public void setWorkInfos( List<KaoqinDb> workInfos){
        this. workInfos.clear();
        this.workInfos .addAll(workInfos);
        notifyDataSetChanged();
    }
    public void addWorkInfo(KaoqinDb workInfo){
        workInfos.add(workInfo);
        notifyDataSetChanged();
    }
    public WorkRecycleviewAdapter(Context context)
        {
          this.context=context;
        }
    @Override
    public MyRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("test", "onCreateViewHolder: position-");
        View layoutView= LayoutInflater.from(context).inflate(R.layout.bar_work_adapter,parent,false);
        final MyRecycleHolder myRecycleHolder = new MyRecycleHolder(layoutView);
        myRecycleHolder.qianDao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleAdapterOnclickListener!=null){
                        recycleAdapterOnclickListener.qianDaoOnclick((KaoqinDb) myRecycleHolder.getTag());
                        KaoqinDb  workInfo=(KaoqinDb) myRecycleHolder.getTag();


                    }
                }
            });
        myRecycleHolder.qianChu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleAdapterOnclickListener!=null){
                        recycleAdapterOnclickListener.qianChuOnclick((KaoqinDb) myRecycleHolder.getTag());
                    }
                }
            });
        //设填报监听点击事件
        myRecycleHolder.titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recycleAdapterOnclickListener!=null){
                    recycleAdapterOnclickListener.gotoWorkOnclick((KaoqinDb) myRecycleHolder.getTag());
                }
            }
        });
        return myRecycleHolder;
    }

    @Override
    public void onBindViewHolder(MyRecycleHolder holder, int position) {
         KaoqinDb workInfo=workInfos.get(position);
         holder.setTag(workInfo);
         holder.titleName.setText(workInfo.getStoreName()+"/"+workInfo.getProjectName());
        //设置签到信息并监听点击事件
         if (workInfo.getSignInTime()!=null&&!("null").equals(workInfo.getSignInTime())) {
             holder.qianDao.setText(workInfo.getSignInTime());
             holder.qianDao.setClickable(false);
         }else{
             holder.qianDao.setText("签到");
             holder.qianDao.setClickable(true);

         }
        //设置签出信息并监听点击事件
        if (workInfo.getSignOutTime()!=null&&!("null").equals(workInfo.getSignOutTime())){
            holder.qianChu.setText(workInfo.getSignOutTime());
            holder.qianChu.setClickable(false);
        }else{
            holder.qianChu.setText("签出");
            holder.qianChu.setClickable(true);
        }
        Log.i("test", "onBindViewHolder: position-"+position+"    workInfo.getSignInTime()"+workInfo.getSignInTime()+"workInfo.getSignOutTime()"+workInfo.getSignOutTime());
    }

    @Override
    public int getItemCount() {
        return workInfos.size();
    }

    public void setRecycleAdapterOnclickListener(RecycleAdapterOnclickListener recycleAdapterOnclickListener) {
        this.recycleAdapterOnclickListener = recycleAdapterOnclickListener;
    }


    public class MyRecycleHolder extends RecyclerView.ViewHolder {
        RelativeLayout titleBar;
        TextView titleName;
        TextView qianChu;
        TextView qianDao;
        private Object tag;
        public MyRecycleHolder(View itemView) {
            super(itemView);
            titleBar=(RelativeLayout) itemView.findViewById(R.id.bar_work_btn);
            titleName=(TextView) itemView.findViewById(R.id.bar_work_name);
            qianChu=(TextView) itemView.findViewById(R.id.bar_work_qianchu);
            qianDao=(TextView) itemView.findViewById(R.id.bar_work_qiandao);
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }

    public interface  RecycleAdapterOnclickListener{
        void  gotoWorkOnclick(KaoqinDb kaoqinDb);
        void  qianDaoOnclick(KaoqinDb kaoqinDb);
        void  qianChuOnclick(KaoqinDb kaoqinDb);
    }
}
