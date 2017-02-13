package com.essence.activitys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.OtherUtils.DisplayUtil;

/**
 * Created by Administrator on 2017/1/22.
 */

public class LineraLayoutRecycleviewDivider  extends RecyclerView.ItemDecoration{

    private Context context;
    private Paint paint;
    private int dividerColor;
    private int dividerHeight_dp;
    int dividerHeight_px;
    public LineraLayoutRecycleviewDivider(Context context) {
        this.context=context;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        paint.setColor(dividerColor);
    }

    public int getDividerHeight() {
        return dividerHeight_dp;
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight_dp = dividerHeight;
        dividerHeight_px= DisplayUtil.dip2px(context,dividerHeight_dp);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        int dividerLeft=parent.getPaddingLeft();
        int dividerRight=parent.getWidth()-parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
             View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int dividerTop = child.getBottom() + params.bottomMargin;

            int dividerBottom = dividerTop + dividerHeight_px;
//            Log.i("test","left-"+dividerLeft+",t-"+dividerTop+",r-"+dividerRight+",b-"+dividerBottom);
            RectF oval3 = new RectF(dividerLeft, dividerTop, dividerRight,dividerBottom);// 设置个新的长方形
            c.drawRoundRect(oval3, 0, 0, paint);//第二个参数是x半径，第三个参数是y半径
        }
    }



    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, dividerHeight_px);
    }
}
