package com.essence.dbmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper{
	public static String DB_NAME = "hecha_system.db"; // 数据库名称
    private static final int VSERSION = 1;// 版本号
	private static MyDBHelper mInstance;
  //存储所有门店列表
	private String createStoresInfo="create table if not exists StoresInfo ("
			+ "  StoreID            integer,"
			+ "  StoreName          text,"
			+ "  ProjectID     integer,"
			+ "  ProjectName   text,"
			+ "  Lng           text,"
			+ "  Lat           text,"
			+ "  Address       text,"
			+ "  Province      text,"
			+ "  City          text,"
			+ "  Area          text,"
			+ "  Expand1       text,"
			+ "  Expand2       text,"
	        + "  primary key(StoreID,ProjectID));";
	//存储所有项目下的表单
	private String createTianbaoTables="create table if not exists TianbaoTables ("
			+ "  orderId              integer," //项目
			+ "  ProjectId            integer," //项目id
			+ "  ProjectName          text,"//项目
			+ "  ProjectLogo          text,"//项目log
			+ "  TableId              integer,"//表单id
			+ "  TableName            text,"//表单名称
			+ "  TableReportingFrequency   text,"//表单填报频率
			+ "  TableJson            text,"//表单填报内容 暂存为json数组
			+ "  Expand1              text,"
			+ "  Expand2              text,"
			+ "  primary key(ProjectId,TableId));";
	//存储所有考勤
	private String createKaoqin="create table if not exists Kaoqin ("
			+ "  UserId                  integer," //用户id
			+ "  ProjectId               integer," //项目id
			+ "  ProjectName             text,"//项目
			+ "  StoreID                 integer,"
			+ "  StoreName               text,"
			+ "  Date                    text,"
			+ "  Time                    text,"
			+ "  PhoneImei               text,"
			+ "  signInLat                text,"
			+ "  signInLng                text,"
			+ "  signInTime               text,"
			+ "  signOutLat               text,"
			+ "  signOutLng               text,"
			+ "  signOutTime              text,"
			+ "  IsUpload                 integer,"
			+ "  Expand1                  text,"
			+ "  Expand2                   text);";
	//存储填过的表单
	String createDynamicTianbao = "CREATE TABLE IF NOT EXISTS DynamicTianbao ( "
			+ "    UserId     INTEGER,"
			+ "    ProejctId     INTEGER,"
			+ "    StoreId      INTEGER,"
			+ "    TableId        INTEGER,"
			+ "    CreateDate    TEXT,"
			+ "    JsonString    TEXT,"
			+ "    ControlID    TEXT,"
			+ "    ControlValue    TEXT,"
			+" primary key (UserId,ProejctId,StoreId,TableId,CreateDate))";
	//存储所有照片的信息
	String createPictures = "CREATE TABLE IF NOT EXISTS Pictures ( "
			+ "    UserId     INTEGER,"
			+ "    StoreId      INTEGER,"
			+ "    ControlID      INTEGER,"
			+ "    Lat            TEXT,"
			+ "    Lng              TEXT,"
			+ "    CreateDate       TEXT,"
			+ "    PicturePath       TEXT,"
			+ "  Expand1           TEXT,"
			+ "  Expand2           TEXT);";

	public  MyDBHelper(Context context) {
		super(context.getApplicationContext(), DB_NAME, null, VSERSION);
	}
	public synchronized static MyDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MyDBHelper(context);
		}
		return mInstance;
	};
	@Override
	public void onCreate(SQLiteDatabase db) {
				try {
					db.execSQL(createStoresInfo);
					db.execSQL(createTianbaoTables);
					db.execSQL(createKaoqin);
					db.execSQL(createDynamicTianbao);
					db.execSQL(createPictures);
				} catch (Exception e) {
					Log.e("dbexception", "creatTable failed:"+e.getMessage());
					e.printStackTrace();
				}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
