package com.OtherUtils.ShowPictureUtils;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.essence.selfclass.PictureInfo;
import com.essence.dbmanager.DataBaseUtils;
import com.essence.hechaSystem.R;

public class AlbumItemAty extends Activity implements OnClickListener,MatrixImageView.OnSingleTapListener {
	public final static String TAG = "AlbumDetailAty";
	private AlbumViewPager mViewPager;
	private ImageView mBackView;
	private ImageView mCameraView;
	private TextView mCountView;
	private View mHeaderBar, mBottomBar;
	private Button mDeleteButton;
	private Button mEditButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.albumitem);
		mViewPager = (AlbumViewPager) findViewById(R.id.albumviewpager);
		mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
		mCameraView = (ImageView) findViewById(R.id.header_bar_photo_to_camera);
		mCountView = (TextView) findViewById(R.id.header_bar_photo_count);
		mHeaderBar = findViewById(R.id.album_item_header_bar);
		mBottomBar = findViewById(R.id.album_item_bottom_bar);
		mDeleteButton = (Button) findViewById(R.id.delete);
		mEditButton = (Button) findViewById(R.id.edit);
		mBackView.setOnClickListener(this);
		mCameraView.setOnClickListener(this);
		mCountView.setOnClickListener(this);
		mDeleteButton.setOnClickListener(this);
		mEditButton.setOnClickListener(this);	
	}
//String code;
//String pictureTypeString;
//String date;
String selectPath;
private int storeId;
int controlId;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
	
		storeId = getIntent().getIntExtra("storeId", 0);
		controlId = getIntent().getIntExtra("controlId", 0);
		selectPath= getIntent().getStringExtra("selectPath");
		mViewPager.setOnPageChangeListener(pageChangeListener);
		mViewPager.setOnSingleTapListener(this);
		loadAlbum();
	}

	public void loadAlbum( ) {
			List<String> paths = new ArrayList<String>();
			List<PictureInfo> pictureInfos=new DataBaseUtils(this).queryPicturesByUserIdControlId(storeId,controlId);
		for (PictureInfo pictureInfo:pictureInfos){
			paths.add(pictureInfo.getPicturePath());
		}
			int currentItem = 0;
			currentItem = paths.indexOf(selectPath);
			mViewPager.setAdapter(mViewPager.new ViewPagerAdapter(paths));
			mViewPager.setCurrentItem(currentItem);
			mCountView.setText((currentItem + 1) + "/" + paths.size());
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			if (mViewPager.getAdapter() != null) {
				String text = (position + 1) + "/"+ mViewPager.getAdapter().getCount();
				mCountView.setText(text);
			} else {
				mCountView.setText("0/0");
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onSingleTap() {
		if (mHeaderBar.getVisibility() == View.VISIBLE) {
			AlphaAnimation animation = new AlphaAnimation(1, 0);
			animation.setDuration(300);
			mHeaderBar.startAnimation(animation);
			mBottomBar.startAnimation(animation);
			mHeaderBar.setVisibility(View.GONE);
			mBottomBar.setVisibility(View.GONE);
		} else {
			AlphaAnimation animation = new AlphaAnimation(0, 1);
			animation.setDuration(300);
			mHeaderBar.startAnimation(animation);
			mBottomBar.startAnimation(animation);
			mHeaderBar.setVisibility(View.VISIBLE);
			mBottomBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.header_bar_photo_back:
			AlbumItemAty.this.finish();;
			break;
		case R.id.header_bar_photo_to_camera:
	//		startActivity(new Intent(this, CameraAty.class));
			break;
		case R.id.delete:
			String result = mViewPager.deleteCurrentPath();
			if (result != null) {
				mCountView.setText(result);
				if (result.equals("0/0")) {
					AlbumItemAty.this.finish();
				}
			}
			break;
		default:
			break;
		}
	}

	
	@Override
	protected void onStop() {

		super.onStop();
	}
}
