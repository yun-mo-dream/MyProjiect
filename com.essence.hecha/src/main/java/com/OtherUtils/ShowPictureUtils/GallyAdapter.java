package com.OtherUtils.ShowPictureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.essence.hechaSystem.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class GallyAdapter extends BaseAdapter {

    private Context mContext;
    // 照片类型 排面或者竞争照等
    //private int mid = 0;
    private String TAG="pictrue";
    private FileUtils fileUtils;
    /** 图片加载器 优化了了缓存 */
    private ImageLoader mImageLoader;
    // 用户的登陆名
    int uid = 0;
    String yyyymmdd = DateUtils.getDateStr("yyyy-MM-dd");
    float weight, height;
    private List<String> picturePathList = new ArrayList<String>();
    /** 加载图片配置参数 */
    private DisplayImageOptions mOptions;

    public GallyAdapter(Context c, List<String> picturePathList) {
        mContext = c;
        this.picturePathList.addAll(picturePathList);
        fileUtils = new FileUtils();
        //图片加载器
        mImageLoader = ImageLoader.getInstance(c);
        // 设置网络图片加载参数
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder = builder.showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .displayer(new RoundedBitmapDisplayer(20));
        mOptions = builder.build();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // Log.i("打印信息",""+list.size());
        return picturePathList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return picturePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    Bitmap bit = null;
    RoundedDrawable rd = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gally,null);
            holder.imageView  = (ImageView) convertView.findViewById(R.id.gally_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        File file = new File(picturePathList.get(position));
        if (file.exists()) {
            holder.imageView.setImageResource(R.drawable.ic_stub);
            mImageLoader.loadImage(picturePathList.get(position), holder.imageView, mOptions,true);
        }
        return convertView;
    }

    public  class ViewHolder{
        public ImageView imageView;
    }
}

