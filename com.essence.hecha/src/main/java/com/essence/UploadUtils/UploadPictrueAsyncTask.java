package com.essence.UploadUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.OtherUtils.SharePreferenceUtil;
import com.alibaba.fastjson.JSON;
import com.com.essence.selfclass.JsonUploadPictureResult;
import com.com.essence.selfclass.PictureInfo;
import com.com.essence.selfclass.UpPicture;
import com.essece.networkutils.ConstantNetwork;
import com.essece.networkutils.MyHttpClient;
import com.essence.activitys.MainActivity;
import com.essence.dbmanager.DataBaseUtils;


public class UploadPictrueAsyncTask {
	public static Boolean uploadFlag=true;
	/**
	 * 等待任务执行的队列
	 */
	private LinkedBlockingDeque<Runnable> mTaskQueue;
	/**
	 * 执行任务的线程池
	 */
	private ExecutorService mThreadPool;
	//the default work thread count
	private  int threadCount = 2;
	/**
	 * the queue work order
	 */
	private Type mType = Type.LIFO;

	/**
	 * manager threadpool thread
	 */
	private Thread mPoolThread;


	/** 线程池管理器*/ 
	private Semaphore mSemaphoreThreadPool;
	private Context mContext;
	private StringBuffer errorLogBuffer;
    public static final int succeed_msg=1;
    public static final int failed_msg=2;
    public static final int begain_msg=0;
    public static final int net_speed=3;
	public enum Type
	{
		FIFO, LIFO;
	}
	private DataBaseUtils dataBaseUtils;
	private List<PictureInfo> caremaInfoList;
	private ProgressDialog m_Dialog;
	private int totalCount=0;
	private int succedCount=0;
	private int failedCount=0;
	private Handler mUIHandler;
    private int userId;

	@SuppressLint("NewApi") 
	public UploadPictrueAsyncTask(Context context)
   {
		mContext = context;
	   dataBaseUtils = new DataBaseUtils(context);
	   userId= SharePreferenceUtil.getUserId(context);
		errorLogBuffer=new StringBuffer();
		threadCount = Runtime.getRuntime().availableProcessors();
		if (threadCount>4) {
			threadCount =3;
		}	
		IniteRefreshUI();
	}
	@SuppressLint("NewApi") 
	public void execute(){	
		caremaInfoList = dataBaseUtils.queryALLPicturesByUserId(userId);
		failedCount=0;
		succedCount=0;
		totalCount = caremaInfoList.size();
		if (totalCount ==0 ) {
			return;
		}
		Log.i("errorcheck", "uploadFlag--"+ UploadPictrueAsyncTask.uploadFlag);
        if (!uploadFlag) {
			return;
		}
        uploadFlag = false;
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		Log.i("testthread", "threadCount-"+ threadCount);
		mSemaphoreThreadPool = new Semaphore(threadCount, true);
		mTaskQueue = new LinkedBlockingDeque<Runnable>();
		mTaskQueue.clear();
		refreshProgress(begain_msg);
		BuildUploadPictureTasks();
		begainBackThread();
//		initeTimer();
	}
	
//	private long total_data ;
//	private int count=2;
//	private Timer timer;
//	private TimerTask task;
//	private void initeTimer(){
//		total_data = TrafficStats.getTotalTxBytes();
//		timer = new Timer();
//		task = new TimerTask() {
//			
//			@Override
//			public void run() {
//				float kk = getNetSpeed()/1024f;
//				BigDecimal bigDecimal = new BigDecimal(kk); 
//				kk = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
//		        String speed=kk + "kb/s";
//				Message message = new Message();
//				message.obj=speed;
//				message.what=net_speed;
//				mUIHandler.sendMessage(message);
//			}
//		};
//		timer.schedule(task, 0, count*1000);
//	}
//	/**
//     * 核心方法，得到当前网速
//     * @return
//     */
//    private float getNetSpeed() {  
//    	long now = TrafficStats.getTotalTxBytes();
//        long traffic_data = now - total_data;
//        total_data = now;
//        return traffic_data /count ;
//    }
	private void BuildUploadPictureTasks() {

		for (int i = 0; i <totalCount; i++) {
			final int position = i;
			addTask(new Runnable() {
				public void run() {
					PictureInfo pictureInfo = caremaInfoList.get(position);

//					Log.i("pictrue_begain", "begain:" + position);
//					Log.i("pictrue_begain", "path:" + pictureInfo.getPicturePath());
					try {
						ByteArrayOutputStream baos = BitmapUtil.getSmallBitmap(pictureInfo.getPicturePath(), 960, 1280, 250);
						byte[] xdByteArray = baos.toByteArray();
						String encodeString = Base64.encodeToString(xdByteArray, Base64.DEFAULT);
						if (baos!=null) {
							baos.close();	
						}
						UpPicture upPicture = new UpPicture();
						upPicture.setUserID(userId);
						upPicture.setControlID(pictureInfo.getControlId());
						upPicture.setLat(pictureInfo.getLat());
						upPicture.setLng(pictureInfo.getLng());
						upPicture.setPicBase64(encodeString);
                        upPicture.setCreateDate(pictureInfo.getCreateDate());
						upPicture.setStoreID(pictureInfo.getStoreId());
						String upJson= JSON.toJSONString(upPicture);
						Log.i("pictrue_begain", "UPJson--:" +upJson);
						String result6 = MyHttpClient.UploadPicture(ConstantNetwork.url_upload_picture,upJson);
						JsonUploadPictureResult jsonUploadPictureResult=JSON.parseObject(result6,JsonUploadPictureResult.class);
						if (jsonUploadPictureResult.getErrorCode()==0) {
								Boolean flag = dataBaseUtils.deletePicturesByPath(pictureInfo.getPicturePath());
								if (flag) {
									File deleteFile = new File(pictureInfo.getPicturePath());
									deleteFile.delete();
								}
								refreshProgress(succeed_msg);
						}else{
								refreshProgress(failed_msg);
								errorLogBuffer.append("第 "+position+" 张异常，路径："+pictureInfo.getPicturePath()+",详细:"+result6+"\n");
						}
					} catch (Exception e) {
						Log.i("pictrue_f", "failed:" + position + " Exception " + e.getMessage());
						errorLogBuffer.append("第 "+position+" 张异常，路径："+pictureInfo.getPicturePath()+",详细:"+e.getMessage()+"\n");
						refreshProgress(failed_msg);
					}
					mSemaphoreThreadPool.release();
				}
			});
		}
	}
	@SuppressLint("NewApi") 
	private void begainBackThread()
	{  
		mPoolThread = new Thread(){
			@Override
			public void run()
			{
				while(mTaskQueue.size()>0){
					Log.i("pictrue", "mTaskQueue.size():"+mTaskQueue.size());
					try {
						mSemaphoreThreadPool.acquire();
						Log.i("pictrue", "mSemaphoreThreadPool.acquire:");
						Runnable runnable=getTask();
						mThreadPool.execute(runnable);	
					} catch (InterruptedException e) {
						e.printStackTrace();
						refreshProgress(failed_msg);
					}
				}
//				timer.cancel();
				mThreadPool.shutdown();	
			};
		};
		mPoolThread.start();
	}
	
	/**
	 * get the task from the queue 
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	@SuppressLint("NewApi") 
	private Runnable getTask() throws InterruptedException
	{
		if (mType == Type.FIFO){
			return mTaskQueue.takeFirst();
		} else {
			return mTaskQueue.takeLast();
		}
	}
	/**
	 * add a task to the queue
	 * @param runnable
	 * @throws InterruptedException
	 */
	@SuppressLint("NewApi") 
	private  void addTask(Runnable runnable)
	{
			try {
				mTaskQueue.put(runnable);
			} catch (InterruptedException e) {
				//adding task exception means uploading this picture failed
				refreshProgress(failed_msg);
				e.printStackTrace();
			}
	}
	
	private void IniteRefreshUI(){
			mUIHandler = new Handler(mContext.getMainLooper()){
				public void handleMessage(Message msg){
						int msgType = msg.what;
						switch (msgType) {
						case begain_msg:
						    	m_Dialog = new ProgressDialog(mContext);
								m_Dialog.setTitle("提示");
								m_Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								m_Dialog.setMessage("共"+totalCount+"张图片\n正在上传第1张....");
								m_Dialog.setIndeterminate(false);
								m_Dialog.setCancelable(true);
								m_Dialog.setMax(totalCount);
								m_Dialog.setCanceledOnTouchOutside(false);
								m_Dialog.incrementProgressBy(0);
								m_Dialog.show();
								uploadFlag =true;
								break;
                         case succeed_msg:
								succedCount++;
								break;
                          case failed_msg:
	                            failedCount++;
	                           break;
	                       case net_speed:
	                    	   String speedString = (String) msg.obj;
	                    	   if (m_Dialog!=null) {
	                    		   m_Dialog.setTitle("提示"+"  当前网速："+speedString);
	                    		  
							     }
	                    	   return;
							default:
								break;
							}
							if (succedCount+failedCount==totalCount) {
								
								if (m_Dialog!=null&&m_Dialog.isShowing()) {
									m_Dialog.dismiss();
								}
								showResultUpload();
							}else {
								if (msgType!=begain_msg) {
									if (m_Dialog!=null&&m_Dialog.isShowing()) {
										m_Dialog.setMessage("共"+totalCount+"张图片\n已传"+succedCount+"张,失败"+failedCount+"张\n正在上传第"+String.valueOf(succedCount+failedCount+1)+"张....");
										m_Dialog.setProgress(succedCount+failedCount);
									}
					  	}
					};
				};
			};
		}
	private void showResultUpload(){
		String showMessage;
		String negativeButton;
		if (MainActivity.mainActivity==null|| MainActivity.mainActivity.isFinishing()) {
			return;
		}
		if (failedCount>0) {
			showMessage="总共"+totalCount+"张, " + succedCount+ "张成功," + failedCount+"张失败，请将失败的重新上传";
			negativeButton ="取消";
		}else {
			showMessage=totalCount+" 张照片全部上传成功 ";
			negativeButton ="确定";
		}
		final Builder builder = new Builder(mContext);
		builder.setMessage(showMessage);
		builder.setCancelable(false);
		if (failedCount>0) {
		builder.setPositiveButton("重新上传",new DialogInterface.OnClickListener() {

					@SuppressLint("NewApi") 
					@Override
					public void onClick(DialogInterface dialog,int which) {
						dialog.dismiss();
						//重新上传
						execute();
					}
			 });
			builder.setNeutralButton("详细信息",new DialogInterface.OnClickListener() {

				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog,int which) {
					builder.setMessage(errorLogBuffer.toString());
					builder.show();
					//Toast.makeText(mContext,errorLogBuffer.toString(),Toast.LENGTH_LONG).show();
				}
			});
		}
			builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int which) {
					dialog.dismiss();
				}
			});

		builder.create().show();
	}
	private void refreshProgress(int meg){
		Message message = new Message();
		message.what=meg;
		mUIHandler.sendMessage(message);
	}
}
