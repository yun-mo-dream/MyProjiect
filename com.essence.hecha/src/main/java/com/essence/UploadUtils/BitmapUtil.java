package com.essence.UploadUtils;

import android.graphics.*;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextPaint;
import android.util.Log;

import java.io.*;
import java.util.*;


/**
 * Bitmap������,�����ù���ָ��������ͼƬ,ʹ�ô˹�����,������Ҫ�ֶ�����Bitmap�ڴ� ԭ��:
 * ��һ�����б���ʹ��Bitmap��˳��,ÿ��ʹ��Bitmap�������ƶ�������ͷ ���ڴ治��,���ߴﵽ�ƶ��Ļ���������ʱ��,���ն���β��ͼƬ
 * ��֤��ǰʹ������ͼƬ�õ��ʱ��Ļ���,����ٶ�
 * 
 * @author liaoxingliao
 * 
 */
public final class BitmapUtil {

	private static final Size ZERO_SIZE = new Size(0, 0);
	private static final Options OPTIONS_GET_SIZE = new Options();
	private static final Options OPTIONS_DECODE = new Options();
	private static final byte[] LOCKED = new byte[0];

	private static final LinkedList<String> CACHE_ENTRIES = new LinkedList<String>(); // 此对象用来保持Bitmap的回收顺序,保证最后使用的图片被回收
	private static final Queue<QueueEntry> TASK_QUEUE = new LinkedList<QueueEntry>(); // 线程请求创建图片的队列
	private static final Set<String> TASK_QUEUE_INDEX = new HashSet<String>(); // 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列

	private static final Map<String, Bitmap> IMG_CACHE_INDEX = new HashMap<String, Bitmap>(); // 缓存Bitmap
	// 通过图片路径,图片大小

	private static int CACHE_SIZE = 50; // 缓存图片数量

	static {
		OPTIONS_GET_SIZE.inJustDecodeBounds = true;
		// 初始化创建图片线程,并等待处理
		new Thread() {
			{
				setDaemon(true);
			}

			public void run() {
				while (true) {
					synchronized (TASK_QUEUE) {
						if (TASK_QUEUE.isEmpty()) {
							try {
								TASK_QUEUE.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					QueueEntry entry = TASK_QUEUE.poll();
					String key = createKey(entry.path, entry.width,
							entry.height);
					TASK_QUEUE_INDEX.remove(key);
					// createBitmap(entry.path, entry.width, entry.height);
					// 修正过的代码
					getBitmap(entry.path, entry.width, entry.height);
				}
			}
		}.start();

	}

	/**
	 * 创建一张图片 如果缓存中已经存在,则返回缓存中的图,否则创建一个新的对象,并加入缓存
	 * 宽度,高度,为了缩放原图减少内存的,如果输入的宽,高,比原图大,返回原图
	 * 
	 * @param path
	 *            图片物理路径 (必须是本地路径,不能是网络路径)
	 * @param width
	 *            需要的宽度
	 * @param height
	 *            需要的高度
	 * @return
	 */
	public static Bitmap getBitmap(String path, int width, int height) {
		Bitmap bitMap = null;
		try {
			if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
				destoryLast();
			}
			bitMap = useBitmap(path, width, height);
			if (bitMap != null && !bitMap.isRecycled()) {
				return bitMap;
			}
			bitMap = createBitmap(path, width, height);
			String key = createKey(path, width, height);
			synchronized (LOCKED) {
				IMG_CACHE_INDEX.put(key, bitMap);
				CACHE_ENTRIES.addFirst(key);
			}
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			destoryLast();
			System.out.println(CACHE_SIZE);
			// return createBitmap(path, width, height);
			// 修正过的代码
			return getBitmap(path, 150, 150);
		} finally {
			if (bitMap == null) {
				try {
					destoryLast();
					bitMap = createBitmap(path, 150, 150);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return bitMap;
	}

	/**
	 * 设置缓存图片数量 如果输入负数,会产生异常
	 * 
	 * @param size
	 */
	public static void setCacheSize(int size) {
		if (size <= 0) {
			throw new RuntimeException("size :" + size);
		}
		while (size < CACHE_ENTRIES.size()) {
			destoryLast();
		}
		CACHE_SIZE = size;
	}

	/**
	 * 加入一个图片处理请求到图片创建队列
	 * 
	 * @param path
	 *            图片路径(本地)
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 */
	public static void addTask(String path, int width, int height) {
		QueueEntry entry = new QueueEntry();
		entry.path = path;
		entry.width = width;
		entry.height = height;
		synchronized (TASK_QUEUE) {
			String key = createKey(path, width, height);
			if (!TASK_QUEUE_INDEX.contains(key)
					&& !IMG_CACHE_INDEX.containsKey(key)) {
				TASK_QUEUE.add(entry);
				TASK_QUEUE_INDEX.add(key);
				TASK_QUEUE.notify();
			}
		}
	}

	/**
	 * 通过图片路径返回图片实际大小
	 * 
	 * @param path
	 *            图片物理路径
	 * @return
	 */
	public static Size getBitMapSize(String path) {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
				return new Size(OPTIONS_GET_SIZE.outWidth,
						OPTIONS_GET_SIZE.outHeight);
			} catch (FileNotFoundException e) {
				return ZERO_SIZE;
			} finally {
				closeInputStream(in);
			}
		}
		return ZERO_SIZE;
	}

	// ------------------------------------------------------------------
	// private Methods
	// 将图片加入队列头
	private static Bitmap useBitmap(String path, int width, int height) {
		Bitmap bitMap = null;
		String key = createKey(path, width, height);
		synchronized (LOCKED) {
			bitMap = IMG_CACHE_INDEX.get(key);
			if (null != bitMap) {
				if (CACHE_ENTRIES.remove(key)) {
					CACHE_ENTRIES.addFirst(key);
				}
			}
		}
		return bitMap;
	}

	// 回收最后一张图片
	private static void destoryLast() {
		synchronized (LOCKED) {
			String key = CACHE_ENTRIES.removeLast();
			if (key.length() > 0) {
				Bitmap bitMap = IMG_CACHE_INDEX.remove(key);
				if (bitMap != null && !bitMap.isRecycled()) {
					bitMap.recycle();
					bitMap = null;
				}
			}
		}
	}

	// 创建键
	private static String createKey(String path, int width, int height) {
		if (null == path || path.length() == 0) {
			return "";
		}
		return path + "_" + width + "_" + height;
	}

	// 通过图片路径,宽度高度创建一个Bitmap对象
	private static Bitmap createBitmap(String path, int width, int height) {
		File file = new File(path);
		Options opts = new Options();
		opts.inTempStorage = new byte[500 * 1024];
		opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		Bitmap bitMap;
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				Size size = getBitMapSize(path);
				if (size.equals(ZERO_SIZE)) {
					return null;
				}
				if (width == 0 || height == 0) {
					synchronized (OPTIONS_DECODE) {
						bitMap = BitmapFactory.decodeStream(in, null, opts);
						return bitMap;
					}

				} else if (width == 800 && height == 800) {
					// int hh=(size.getHeight()*1024)/width;
					// synchronized (OPTIONS_DECODE) {
					// OPTIONS_DECODE.outHeight = hh;
					// OPTIONS_DECODE.outWidth=width;
					// OPTIONS_DECODE.inJustDecodeBounds = false;
					// Bitmap bitMap = BitmapFactory.decodeStream(in, null,
					// OPTIONS_DECODE);
					// return bitMap;
					// }
					synchronized (OPTIONS_DECODE) {
						// final BitmapFactory.Options options = new
						// BitmapFactory.Options();
						// options.inJustDecodeBounds = true;
						bitMap = BitmapFactory.decodeStream(in, null, opts);
						int height2 = bitMap.getHeight();
						int width2 = bitMap.getWidth();
						// BitmapFactory.decodeFile(path, options);
						// int hh=(height*1024)/width;

						// options.inSampleSize = calculateInSampleSize(options,
						// 1024, (height*1024)/width);//自定义一个宽和高
						// options.inJustDecodeBounds = false;
						return ImageUtils.compressImage(Bitmap
								.createScaledBitmap(bitMap, 800,
										(height2 * 800) / width2, true));

						// Bitmap bitMap = BitmapFactory.decodeStream(in, null,
						// null);
						// return ImageUtils.comp1(bitMap);
					}
				} else {
					int scale = 1;
					int a = size.getWidth() / width;
					int b = size.getHeight() / height;
					scale = Math.max(a, b);
					opts.inSampleSize = scale;

					bitMap = BitmapFactory.decodeStream(in, null, opts);
					return bitMap;
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				closeInputStream(in);
			}
		}
		return null;
	}

	public static int calculateInSampleSize(Options options,
			int reqWidth, int reqHeight) {
		final int width = options.outWidth;// 获取图片的宽
		return width / 1024;// 求出缩放值
	}

	// 关闭输入流
	private static void closeInputStream(InputStream in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 图片大小
	static class Size {
		private int width, height;

		Size(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

	/**
	 * 加水印 也可以加文字
	 * 
	 * @param src
	 * @param watermark
	 * @param title
	 * @return
	 */
	public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
			String title) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();
		// 加入图片
		if (watermark != null) {
			int ww = watermark.getWidth();
			int wh = watermark.getHeight();
			paint.setAlpha(50);
			// cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);//
			// 在src的右下角画入水印
			cv.drawBitmap(watermark, 0, 0, paint);// 在src的左上角画入水印
		} else {
			Log.i("i", "water mark failed");
		}
		// 加入文字
		if (title != null) {
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.NORMAL);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(Color.RED);
			textPaint.setTypeface(font);
			textPaint.setTextSize(40);
			// 这里是自动换行的
			// StaticLayout layout = new
			// StaticLayout(title,textPaint,w,Alignment.ALIGN_OPPOSITE,1.0F,0.0F,true);
			// layout.draw(cv);
			// 文字就加左上角算了
			cv.drawText(title, w - 400, h - 40, textPaint);
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}

	// 队列缓存参数对象
	static class QueueEntry {
		public String path;
		public int width;
		public int height;
	}

	public static void saveBitmap(String path, String name, Bitmap bm) {
		Log.e("aa", "保存图片");
		File f = new File(path, name);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 根据需要可以对图片大小(于height和width而言)和质量两种方式结合进行图片压缩
	 * 
	 * @param filePath
	 *            图片的路径
	 * @param reWidth
	 *            压缩后的图片的宽
	 * @param reHeight
	 *            压缩后的图片高
	 * @param bitmapSize
	 *            压缩后的图片大小kb
	 * @return
	 */
	public synchronized static ByteArrayOutputStream getSmallBitmap(String filePath,int reWidth, int reHeight, int bitmapSize) throws IOException{
		Bitmap bitmap;
		// 更具图片要求的宽高进行压缩
		if (reWidth >0 && reHeight > 0) {
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			options.inSampleSize = calculateSampleSize(options, reWidth,reHeight);
			options.inPurgeable = true;// 同时设置才会有效
			options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filePath, options);
			// 不对宽高进行压缩
		} else {
			bitmap = BitmapFactory.decodeFile(filePath);
		}
		if (bitmap==null) {
			return null;
		}
		
		bitmap =fineCaculate(bitmap, reWidth, reHeight);

		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int qualityOptions = 60;
		// 质量压缩方法，把压缩后的数据存放到baos中
		bitmap.compress(Bitmap.CompressFormat.JPEG, qualityOptions, baos);
															       // (100表示不压缩，0表示压缩到最小)
			// 压缩到比指定大小小为止
			while (baos.toByteArray().length / 1024 > bitmapSize) {// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即让下一次的写入覆盖之前的内容
				qualityOptions -= 5;// 图片质量每次减少10
				bitmap.compress(Bitmap.CompressFormat.JPEG, qualityOptions,baos);// 将压缩后的图片保存到baos中
				if (qualityOptions == 40) {
					// 如果图片的质量已降到最低则，不再进行压缩 ，结束循环
					break;
				}
			}		
		if (bitmap!=null) {
			bitmap.recycle();
		}

		Log.i("time", " 质量压缩后大小 width-height-size -qualityOptions: "+bitmap.getWidth()+" , "+bitmap.getHeight()+" , "+baos.size()+","+qualityOptions);
		return baos;
	}

/**
 * caculate the samplesize
 * @param options
 * @param reWidth
 * @param reHeight
 * @return
 */
	public static int calculateSampleSize(Options options,
			int reWidth, int reHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		int heightSample = 1;
		int widthSample = 1;
		if (width < height) {
			widthSample = Math.round((float) width / (float) reWidth);
			heightSample = Math.round((float) height / (float) reHeight);
		} else {
			widthSample = Math.round((float) width / (float) reHeight);
			heightSample = Math.round((float) height / (float) reWidth);
		}
		inSampleSize = heightSample < widthSample ? widthSample : heightSample;
		if (inSampleSize < 1) {
			inSampleSize = 1;
		}
		Log.i("time", " 图片原始大小-width-height-inSampleSize : " + width + " , "
				+ height + " , " + inSampleSize);
		return inSampleSize;
	}
/**
 * Fine calculation 
 */
public static Bitmap fineCaculate(Bitmap bitmap,int reWidth, int reHeight){
	 // 获得图片的宽高    
    int width =bitmap.getWidth();    
    int height = bitmap.getHeight();    
    
    float scale;   
    if (width<height) {
    	 scale = (float)reWidth/(float)width;   
	}else {
		 scale = (float)reHeight/(float)width;   
	}
    // 取得想要缩放的matrix参数    
    Matrix matrix = new Matrix();    
    matrix.postScale(scale, scale);    
    // 得到新的图片    
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
	return bitmap;
}
}
