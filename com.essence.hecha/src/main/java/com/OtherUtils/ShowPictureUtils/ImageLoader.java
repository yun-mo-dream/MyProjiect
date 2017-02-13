package com.OtherUtils.ShowPictureUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;


/** 
 * @ClassName: ImageLoader 
 * @Description:  ͼƬ������
 * @author LinJ
 * @date 2015-1-8 ����9:02:25 
 *  
 */
public class ImageLoader {
	private static final String TAG = "ImageLoader";
	/**
	 * ��������
	 */
	private static ImageLoader mInstance;
	/**
	 * ��Ϣ����
	 */
	private LinkedBlockingDeque<Runnable> mTaskQueue;

	/**
	 * ͼƬ����ĺ��Ķ���?
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * �̳߳�
	 */
	private ExecutorService mThreadPool;
	private static final int DEAFULT_THREAD_COUNT = 1;
	/**
	 * ���еĵ��ȷ�ʽ
	 */
	private Type mType = Type.LIFO;

	/**
	 * ��̨��ѯ�߳�
	 */
	private Thread mPoolThread;
	/**
	 * UI�߳��е�Handler
	 */
	private Handler mUIHandler;

	/**   �źſ���*/ 
	private Semaphore mSemaphoreThreadPool;

	private Context mContext;

	public enum Type
	{
		FIFO, LIFO;
	}
	public static ImageLoader getInstance(Context context)
	{
		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, Type.LIFO,context);
				}
			}
		}
		return mInstance;
	}
	private ImageLoader(int threadCount, Type type,Context context)
	{
		init(threadCount, type,context);
	}
	public static ImageLoader getInstance(int threadCount, Type type,Context context)
	{
		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{

					mInstance = new ImageLoader(threadCount, type,context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param threadCount
	 * @param type
	 */
	@SuppressLint("NewApi") 
	private void init(int threadCount, Type type,Context context)
	{
		// ��ȡ����Ӧ�õ��������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		//ע��˴�Ҫ��ȡȫ��Context����������Activity�����Դ�޷��ͷ�?
		mContext=context.getApplicationContext();
		mLruCache = new LruCache<String, Bitmap>(cacheMemory){
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				//				return value.getAllocationByteCount();
				return value.getRowBytes() * value.getHeight(); //�ɰ汾����
			}

		};

		// �����̳߳�
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mType = type;
		mSemaphoreThreadPool = new Semaphore(threadCount,true);
		mTaskQueue = new LinkedBlockingDeque<Runnable>();	
		initBackThread();
	}
	/**
	 * ��ʼ����̨��ѯ�߳�
	 */
	private void initBackThread()
	{
		// ��̨��ѯ�߳�
		mPoolThread = new Thread()
		{
			@Override
			public void run()
			{
				while(true){
					try {
						// ��ȡһ���źţ����޿����źţ������߳�
						mSemaphoreThreadPool.acquire();
						// �̳߳�ȥȡ��һ���������ִ�У����������Ϊ��ʱ�������߳�
						Runnable runnable=getTask();
						//ʹ���̳߳�ִ������
						mThreadPool.execute(runnable);	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		};

		mPoolThread.start();
	}

	/**
	 * ����pathΪimageview����ͼƬ
	 * 
	 * @param path ͼƬ·��
	 * @param imageView ����ͼƬ��ImageView
	 * @param options ͼƬ���ز���
	 * @param type  �����Ǵ�ͼ����Сͼ  ��ҪҪСͼ ��Ҫ����ѽ  ������Ҫ����ѹ��
	 * @throws InterruptedException 
	 */
	public void loadImage( String path,  ImageView imageView, DisplayImageOptions options,Boolean getThumbulPicture) 
	{
		//�����ڼ�������ͼʱ�ı���ͼƬ���س���
		options.displayer.display(options.imageResOnLoading, imageView);
		if (mUIHandler == null)
		{
			mUIHandler = new Handler(mContext.getMainLooper())
			{
				public void handleMessage(Message msg)
				{
					// ��ȡ�õ�ͼƬ��Ϊimageview�ص�����ͼƬ
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView view = holder.imageView;
					DisplayImageOptions options=holder.options;
					if(bm!=null){			
						 options.displayer.display(bm, view);
					}
					else {
						options.displayer.display(options.imageResOnFail, view);
					}
				};
			};
		}
		Bitmap bm = null;
		// ����path,���Ҽ��ش�ͼ���ܴӻ����л�ȡbitmap
		if (getThumbulPicture) {
			 bm = getBitmapFromLruCache(path);
		}
		

		if (bm != null)
		{
			refreashBitmap(path, imageView, bm,options);
		} else{
			addTask(buildTask(path, imageView,options,getThumbulPicture));
		}

	}
	
	/**
	 * ���ݴ���Ĳ������½�һ������?
	 * 
	 * @param path
	 * @param imageView
	 * @return
	 */
	private Runnable buildTask(final String path, final ImageView imageView,
			final DisplayImageOptions options,final Boolean getThumbulPicture)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				Bitmap bm = null;
				if (options.fromNet)
				{
					//��ȥ�����ļ��в���
					File file = getDiskCacheDir(imageView.getContext(),
							md5(path));
					// ����ڻ����ļ��з���?
					if (file.exists()){
						bm = loadImageFromLocal(file.getAbsolutePath(),
								imageView);
					} else{
						// ����Ƿ���Ӳ�̻���?
						if (options.cacheOnDisk){
							boolean downloadState = DownloadImgUtils
									.downloadImgByUrl(path, file);
							if (downloadState){
								bm = loadImageFromLocal(file.getAbsolutePath(),
										imageView);
							}
						} else{
							bm = DownloadImgUtils.downloadImgByUrl(path,
									imageView);
						}
					}
				} else{
					if (getThumbulPicture) {
						//bm = getImageThumbnail(path, getScreenInfo(), getScreenInfo());
						bm = getImageThumbnail(path, 200, 200);
					}else {
						bm = loadImageFromLocal(path, imageView);
					}
					//��ȡͼƬ����ת�Ƕ� ��Ϊ��Щ�ֻ����������ǾͿ����ĳ�������Ƭ����ת��һ���Ƕ�
					if (bm==null) {
						return;
					}
					int roateDegree = readPictureDegree(path);
					bm =rotaingImageView(roateDegree, bm);
					Log.i("baishi", "roateDegree--"+roateDegree);
				}
				// �Ƿ����ڴ��л���
				if (options.cacheInMemory) {
					//ֻ��������ͼ��������뻺���У���Ȼ�鿴��ͼҲ��ֱ�Ӽ��ظ�����ͼ��?
					if (getThumbulPicture) {
						addBitmapToLruCache(path, bm);
					}
					
				}
				//������Ϣ��UI�߳�
				refreashBitmap(path, imageView, bm,options);
				//�ͷ��ź�
				mSemaphoreThreadPool.release();
			}
		};
	}
//	private int  getScreenInfo() {
//		// ��ȡ��Ļ��Ϣ
//		DisplayMetrics dm = new DisplayMetrics();
//		WorkActivity.wokActivityInstance.getWindowManager()
//				.getDefaultDisplay().getMetrics(dm);
//		float density = dm.density;
//		int marginLeft = (int) (10*density+0.5f);
//		int screenWidth = dm.widthPixels;
//		int pictureWidth = (screenWidth-marginLeft)/4;
//		Log.i("test", "density:  "+density);
//		Log.i("test", "marginLeft:  "+marginLeft);
//		Log.i("test", "screenWidth:  "+screenWidth);
//		Log.i("test", "pictureWidth:  "+pictureWidth);
//		return pictureWidth;
//	}
	
	/**
	  * ��ȡͼƬ��Ϣ
	  * 
	  * @param path
	  * @return
	  */
	 public static int readPictureDegree(String path) {
	  int degree = 0;
	  try {
	   ExifInterface exifInterface = new ExifInterface(path);
	   int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	   switch (orientation) {
	   case ExifInterface.ORIENTATION_ROTATE_90:
	    degree = 90;
	    break;
	   case ExifInterface.ORIENTATION_ROTATE_180:
	    degree = 180;
	    break;
	   case ExifInterface.ORIENTATION_ROTATE_270:
	    degree = 270;
	    break;
	   }
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  return degree;
	 }
	 
	 /**
	  * ͼƬ��ת
	  * 
	  * @param angle
	  * @param bitmap
	  * @return
	  */
	 public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		 if (bitmap!=null) {
			// ��תͼƬ ����
			  Matrix matrix = new Matrix();
			  matrix.postRotate(angle);
			  System.out.println("angle=" + angle);
			  // �����µ�ͼƬ
			  Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
			    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			  return resizedBitmap;
		}else{
			return null;
		}
	  
	 }
	/**
	 * ����ָ����ͼ��·���ʹ�С����ȡ����ͼ �˷���������ô���? 1.
	 * ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�?
	 * �ڶ��ζ�ȡ��bitmap�Ǹ��ݱ���ѹ������ͼ�񣬵����ζ�ȡ��bitmap����Ҫ������ͼ�� 2.
	 * ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ ������������ɵ�ͼ�񲻻ᱻ����?
	 * 
	 * @param imagePath
	 *            ͼ���·��?
	 * @param width
	 *            ָ�����ͼ��Ŀ��?
	 * @param height
	 *            ָ�����ͼ��ĸ߶�
	 * @return ���ɵ�����ͼ
	 */
	public Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��Ϊtrue����������ķ���һ��Bitmap����������Ŀ���ȡ�����������Ͳ���ռ��̫����ڴ棬Ҳ�Ͳ��ᷢ��OOM�ˡ�
		options.inJustDecodeBounds = true;
		options.inTempStorage = new byte[100 * 1024];
		options.inPreferredConfig = Bitmap.Config.ALPHA_8;
		options.inPurgeable = true;
		options.inInputShareable = true;
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // ��Ϊ false

		// �������ű�
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	private Bitmap loadImageFromLocal(final String path,
			final ImageView imageView)
	{
		Bitmap bm;
		ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2��ѹ��ͼƬ
		bm = decodeSampledBitmapFromPath(path, imageSize.width,
				imageSize.height);
		return bm;
	}


	/**
	 * ����ǩ�������࣬���ַ����ֽ�����
	 * 
	 * @param str
	 * @return
	 */
	public String md5(String str)
	{
		byte[] digest = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			digest = md.digest(str.getBytes());
			return bytes2hex02(digest);

		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ʽ��
	 * 
	 * @param bytes
	 * @return
	 */
	public String bytes2hex02(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes)
		{
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)
			{
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}

		return sb.toString();

	}
/**
 * �����װ���෢�͸�handler
 * @param path
 * @param imageView
 * @param bm
 * @param options
 */
	private void refreashBitmap(String path, ImageView imageView,
			Bitmap bm,DisplayImageOptions options){
		Message message = Message.obtain();
		ImgBeanHolder holder = new ImgBeanHolder();
		holder.bitmap = bm;
		holder.path = path;
		holder.imageView = imageView;
		holder.options=options;
		message.obj = holder;
		mUIHandler.sendMessage(message);
	}

	/**
	 * ��ͼƬ����LruCache
	 * 
	 * @param path
	 * @param bm
	 */
	protected void addBitmapToLruCache(String path, Bitmap bm)
	{
		if (getBitmapFromLruCache(path) == null){
			if (bm != null)
				mLruCache.put(path, bm);
		}
	}

	/**
	 * ����ͼƬ��Ҫ��ʾ�Ŀ�͸߶�ͼƬ����ѹ��?
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	protected Bitmap decodeSampledBitmapFromPath(String path, int width,
			int height)
	{
		// ���ͼƬ�Ŀ�͸ߣ�������ͼƬ���ص��ڴ���
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
				width, height);

		// ʹ�û�õ���InSampleSize�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}



	/**
	 * ��û���ͼƬ�ĵ��?
	 * 
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(Context context, String uniqueName)
	{
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
		{
			cachePath = context.getExternalCacheDir().getPath();
		} else
		{
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * ����path�ڻ����л�ȡbitmap
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}


	/**
	 * ���������ȡ��һ��������������Ϊ��ʱ���������÷���?
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	private Runnable getTask() throws InterruptedException
	{
		if (mType == Type.FIFO)
		{
			return mTaskQueue.takeFirst();
		} else 
		{
			return mTaskQueue.takeLast();
		}
	}
	/**
	 * ��������������
	 * @param runnable
	 * @throws InterruptedException
	 */
	private  void addTask(Runnable runnable)
	{
		try {
			mTaskQueue.put(runnable);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}

	}
	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
		DisplayImageOptions options;
	}
}
