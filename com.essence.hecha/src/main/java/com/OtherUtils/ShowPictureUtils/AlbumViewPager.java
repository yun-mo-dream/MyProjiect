package com.OtherUtils.ShowPictureUtils;

import java.util.List;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;


public class AlbumViewPager extends ViewPager implements MatrixImageView.OnMovingListener {
	public final static String TAG="AlbumViewPager";

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;	

	private boolean mChildIsBeingDragged=false;

	private MatrixImageView.OnSingleTapListener onSingleTapListener;

	private OnPlayVideoListener onPlayVideoListener;
	public AlbumViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mImageLoader= ImageLoader.getInstance(context);
		DisplayImageOptions.Builder builder= new DisplayImageOptions.Builder();
		builder =builder.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(false)
				.displayer(new MatrixBitmapDisplayer());
		mOptions=builder.build();
	}

	public String deleteCurrentPath(){
		return ((ViewPagerAdapter)getAdapter()).deleteCurrentItem(getCurrentItem());
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(mChildIsBeingDragged)
			return false;
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void startDrag() {
		// TODO Auto-generated method stub
		mChildIsBeingDragged=true;
	}


	@Override
	public void stopDrag() {
		// TODO Auto-generated method stub
		mChildIsBeingDragged=false;
	}

	public void setOnSingleTapListener(MatrixImageView.OnSingleTapListener onSingleTapListener) {
		this.onSingleTapListener = onSingleTapListener;
	}
	public void setOnPlayVideoListener(OnPlayVideoListener onPlayVideoListener) {
		this.onPlayVideoListener = onPlayVideoListener;
	}
	public interface OnPlayVideoListener{
		void onPlay(String path);
	}

	public class ViewPagerAdapter extends PagerAdapter {
		private List<String> paths;
		public ViewPagerAdapter(List<String> paths){
			this.paths=paths;
		}

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup viewGroup, int position) {

			View imageLayout = inflate(getContext(), R.layout.item_album_pager, null);
			viewGroup.addView(imageLayout);
			assert imageLayout != null;
			MatrixImageView imageView = (MatrixImageView) imageLayout.findViewById(R.id.image);
			imageView.setOnMovingListener(AlbumViewPager.this);
			imageView.setOnSingleTapListener(onSingleTapListener);
			String path=paths.get(position);
			imageLayout.setTag(path);
			mImageLoader.loadImage(path, imageView, mOptions,false);
			return imageLayout;
		}

		OnClickListener playVideoListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String path=v.getTag().toString();
				path=path.replace("Thumbnail", "Video");
				path=path.replace(".jpg", ".3gp");
				if(onPlayVideoListener!=null)
					onPlayVideoListener.onPlay(path);
				else {
					Toast.makeText(getContext(), "onPlayVideoListener", Toast.LENGTH_SHORT).show();
//					throw new RuntimeException("onPlayVideoListener is null");
				}
			}
		};

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int arg1, Object object) {
			((ViewPager) container).removeView((View) object);  
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;			
		}

		public String deleteCurrentItem(int position) {
			String path=paths.get(position);
			if(path!=null) {
				FileOperateUtil.deleteSourceFile(path, getContext());
				new DataBaseUtils(getContext()).deletePicturesByPath(path);
				paths.remove(path);
				notifyDataSetChanged();
				if(paths.size()>0)
					return (getCurrentItem()+1)+"/"+paths.size();
				else {
					return "0/0";
				}
			}
			return null;
		}
	}
}
