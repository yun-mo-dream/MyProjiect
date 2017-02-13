package com.essence.dbmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.com.essence.selfclass.DynamicTinbaoDB;
import com.com.essence.selfclass.KaoqinDb;
import com.com.essence.selfclass.PictureInfo;
import com.com.essence.selfclass.StoreInfo;
import com.com.essence.selfclass.TBBiaodan;
import com.com.essence.selfclass.TBProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataBaseUtils {
	private MyDBHelper helper;
	private SQLiteDatabase db;

	public DataBaseUtils(Context context) {
		helper = MyDBHelper.getInstance(context);
		db = helper.getWritableDatabase();
	}
/*************************************************************************************************/
	private String createStoresInfo="create table if not exist StoresInfo ("
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
		+ "  primary key(ID,ProjectID));";

	public Boolean insertOrReplaceStores(List<StoreInfo> storeInfos){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				db.beginTransaction();
			    for (StoreInfo storeInfo:storeInfos){
				    String sql="insert or replace into StoresInfo values('"
					        +storeInfo.getID()+"','"
					        +storeInfo.getName()+"','"
					        +storeInfo.getProjectID()+"','"
				        	+storeInfo.getProjectName()+"','"
				        	+storeInfo.getLng() +"','"
				        	+storeInfo.getLat() +"','"
				        	+storeInfo.getAddress() +"','"
				        	+storeInfo.getProvince() +"','"
				        	+storeInfo.getCity() +"','"
							+storeInfo.getArea() +"','"
				        	+""+"','"
				        	+""+"');";
				   db.execSQL(sql);
		    	}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e("dbexception", "insertOrReplaceStores:"+e.getMessage());
				e.printStackTrace();
				return false;
			}finally {
				db.endTransaction();
				db.close();
			}
		}
		return true;
	}
	public Boolean delecteAllStores(){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
					String sql="delete   from StoresInfo";
					db.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}finally {
				db.close();
			}
		}
		return true;
	}
	public Map<String,Integer> queryStoresInfoForAllProjects(){
		Map<String,Integer> projectMap= new HashMap<String,Integer>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="select ProjectID,ProjectName from StoresInfo;";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					int projectId=cursor.getInt(cursor.getColumnIndex("ProjectID"));
					String projectName=cursor.getString(cursor.getColumnIndex("ProjectName"));
					projectMap.put(projectName,projectId);
				}
			} catch (Exception e) {
				Log.e("dbexception", "queryStoresInfoForAllProjects:"+e.getMessage());
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
		return projectMap;
	}
	public Map<String,Integer> queryStoresInfoStoresByProjectId(int projectId){
		Map<String,Integer> storeMap= new HashMap<String,Integer>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="select StoreID,StoreName from StoresInfo where ProjectID="+projectId+";";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					int StoreID=cursor.getInt(cursor.getColumnIndex("StoreID"));
					String StoreName=cursor.getString(cursor.getColumnIndex("StoreName"));
					storeMap.put(StoreName,StoreID);
				}
			} catch (Exception e) {
				Log.e("dbexception", "queryStoresInfoStoresByProjectId:"+e.getMessage());
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
		return storeMap;
	}
	/********************************************填报表单表*****************************************************/
	private String createTianbaoTables="create table if not exists TianbaoTables ("
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
	public HashMap<String,Integer>  queryTianbaoTablesForProjects(){
		HashMap<String,Integer> projectMap=new HashMap<String,Integer>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="select distinct ProjectName,ProjectId from TianbaoTables;";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					String projectName=cursor.getString(cursor.getColumnIndex("ProjectName"));
					int ProjectId=cursor.getInt(cursor.getColumnIndex("ProjectId"));
					projectMap.put(projectName,ProjectId);
				}
			} catch (Exception e) {
				Log.e("dbexception", "queryTableJsonByProjectIdTableId:"+e.getMessage());
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
		return projectMap;
	}

	public Boolean delecteAllTianbaoTables(){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="delete from TianbaoTables";
				db.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}finally {
				db.close();
			}
		}
		return true;
	}
	public Boolean insertOrReplaceTBBiaoDans(List<TBProject> tbProjects){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				db.beginTransaction();

				for (TBProject tbProject:tbProjects){
					List<TBBiaodan>tbBiaodanList=tbProject.getTables();
					int i=0;
					for (TBBiaodan tbBiaodan:tbBiaodanList){
						String sql="insert or replace into TianbaoTables values('"
							    	+i+"','"
									+tbProject.getID()+"','"
									+tbProject.getName()+"','"
									+tbProject.getLogo()+"','"
									+tbBiaodan.getID()+"','"
									+tbBiaodan.getName() +"','"
									+tbBiaodan.getReportingFrequency() +"','"
									+tbBiaodan.getRows() +"','"
									+""+"','"
									+""+"');";
					//	Log.i("test", "保存的Json: "+tbBiaodan.getRows());

						db.execSQL(sql);
						i++;
						Log.i("test", "projectName:"+tbProject.getName()+"  projectId:"+tbProject.getID()+" tableName"+tbBiaodan.getName());
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e("dbexception", "insertOrReplaceTBBiaoDans:"+e.getMessage());
				e.printStackTrace();
				return false;
			}finally {
				db.endTransaction();
				db.close();
			}
		}
		return true;
	}
	public String  queryTableJsonByProjectIdTableId(int projectId,int tableId){
		String tableJson=null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="select TableJson from TianbaoTables where ProjectId="+projectId+" and TableId="+tableId+";";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					tableJson=cursor.getString(cursor.getColumnIndex("TableJson"));
				}
			} catch (Exception e) {
				Log.e("dbexception", "queryTableJsonByProjectIdTableId:"+e.getMessage());
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
		return tableJson;
	}

	public LinkedHashMap<String,Integer>  queryTableIdByProjectId(int projectId){
			LinkedHashMap<String,Integer> tableMap=new LinkedHashMap<String,Integer>();
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				try {
					String sql="select TableName,TableId from TianbaoTables where ProjectId="+projectId+" order by orderId;";
					Cursor cursor = db.rawQuery(sql, null);
					while (cursor.moveToNext()) {
						int tableId=cursor.getInt(cursor.getColumnIndex("TableId"));
						String tableName=cursor.getString(cursor.getColumnIndex("TableName"));
						Log.i("test", "tableName:"+tableName);
						tableMap.put(tableName,tableId);
					}
				} catch (Exception e) {
					Log.e("dbexception", "queryTableJsonByProjectIdTableId:"+e.getMessage());
					e.printStackTrace();
				}finally {
					db.close();
				}
			}
			return tableMap;
	}
	/*******************************************考勤表******************************************************/

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
	public List<KaoqinDb>  queryKaoqinByUserIdDate(int UserId,String Date){
		List<KaoqinDb>  kaoqinDbList= new ArrayList<KaoqinDb>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="select * from Kaoqin where UserId="+UserId+" and Date='"+Date+"';";
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					KaoqinDb kaoqinDb=new KaoqinDb();
					kaoqinDb.setUserId(UserId);
					kaoqinDb.setDate(Date);
					kaoqinDb.setTime(cursor.getString(cursor.getColumnIndex("Time")));
					kaoqinDb.setProjectId(cursor.getInt(cursor.getColumnIndex("ProjectId")));
					kaoqinDb.setStoreID(cursor.getInt(cursor.getColumnIndex("StoreID")));
					kaoqinDb.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
					kaoqinDb.setStoreName(cursor.getString(cursor.getColumnIndex("StoreName")));
					kaoqinDb.setPhoneImei(cursor.getString(cursor.getColumnIndex("PhoneImei")));
					kaoqinDb.setSignInLat(cursor.getString(cursor.getColumnIndex("signInLat")));
					kaoqinDb.setSignInLng(cursor.getString(cursor.getColumnIndex("signInLng")));
					kaoqinDb.setSignInTime(cursor.getString(cursor.getColumnIndex("signInTime")));
					kaoqinDb.setSignOutLat(cursor.getString(cursor.getColumnIndex("signOutLat")));
					kaoqinDb.setSignOutLng(cursor.getString(cursor.getColumnIndex("signOutLng")));
					kaoqinDb.setSignOutTime(cursor.getString(cursor.getColumnIndex("signOutTime")));
					kaoqinDb.setIsUpload(cursor.getInt(cursor.getColumnIndex("IsUpload")));
					kaoqinDb.setExpand1(cursor.getString(cursor.getColumnIndex("Expand1")));
					kaoqinDb.setExpand2(cursor.getString(cursor.getColumnIndex("Expand2")));
					kaoqinDbList.add(kaoqinDb);
				}
			} catch (Exception e) {
				Log.e("dbexception", "queryTableJsonByProjectIdTableId:"+e.getMessage());
				e.printStackTrace();
			}finally {
				db.close();
			}
		}
		return kaoqinDbList;
	}
	public Boolean insertKaoQin(KaoqinDb kaoqinDb){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="insert or replace into Kaoqin values('"
						+kaoqinDb.getUserId()+"','"
						+kaoqinDb.getProjectId()+"','"
						+kaoqinDb.getProjectName()+"','"
						+kaoqinDb.getStoreID()+"','"
						+kaoqinDb.getStoreName()+"','"
						+kaoqinDb.getDate()+"','"
						+kaoqinDb.getTime()+"','"
						+kaoqinDb.getPhoneImei()+"','"
						+kaoqinDb.getSignInLat()+"','"
						+kaoqinDb.getSignInLng()+"','"
						+kaoqinDb.getSignInTime()+"','"
						+kaoqinDb.getSignOutLat()+"','"
						+kaoqinDb.getSignOutLng()+"','"
						+kaoqinDb.getSignOutTime()+"','"
						+kaoqinDb.getIsUpload()+"','"
						+""+"','"
						+""+"');";
						db.execSQL(sql);
			} catch (Exception e) {
				Log.e("dbexception", "insertKaoQin:"+e.getMessage());
				e.printStackTrace();
				return false;
			}finally {
				db.close();
			}
		}
		return true;
	}
  public boolean updateKaoqinSignout(String signOutLat,String signOutLng,String signOutTime,String time){
	  synchronized (helper) {
		  if (!db.isOpen()) {
			  db = helper.getWritableDatabase();
		  }
		  try {
			  String sql="update  Kaoqin  set signOutLat='"+signOutLat+"',signOutLng='"+signOutLng+"',signOutTime='"+signOutTime+"' where Time='"+time+"';";
			  db.execSQL(sql);
		  } catch (Exception e) {
			  Log.e("dbexception", "insertKaoQin:"+e.getMessage());
			  e.printStackTrace();
			  return false;
		  }finally {
			  db.close();
		  }
	  }
	  return true;
  }
	public boolean updateKaoqinSignin(String signInLat,String signInLng,String signInTime,String time){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				String sql="update  Kaoqin  set signInLat='"+signInLat+"',signInLng='"+signInLng+"',signInTime='"+signInTime+"' where Time='"+time+"';";
				db.execSQL(sql);
			} catch (Exception e) {
				Log.e("dbexception", "insertKaoQin:"+e.getMessage());
				e.printStackTrace();
				return false;
			}finally {
				db.close();
			}
		}
		return true;
	}
	//**********************************************动态填报**********************************************************************


	public boolean insertDynamicTianBao(List<DynamicTinbaoDB> dTinbaoDBs){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();

			for (DynamicTinbaoDB dynamicTinbaoDB : dTinbaoDBs) {
				String spl = "insert or replace into DynamicTianbao values('"
						+dynamicTinbaoDB.getUserId()+"','"
						+dynamicTinbaoDB.getProejctId()+"','"
						+dynamicTinbaoDB.getStoreId()+"','"
						+dynamicTinbaoDB.getTableId()+"','"
						+dynamicTinbaoDB.getCreateDate()+"','"
						+dynamicTinbaoDB.getJsonString()+"','"
						+dynamicTinbaoDB.getControlID()+"','"
						+dynamicTinbaoDB.getControlValue()+"')";
				try {
					db.execSQL(spl);
				} catch (Exception e) {
					Log.e("error", "insert failed:"+e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			return true;
		}
	}
	String createDynamicTianbao = "CREATE TABLE IF NOT EXISTS DynamicTianbao ( "
			+ "    UserId     INTEGER,"
			+ "    ProejctId     TEXT,"
			+ "    StoreId      INTEGER,"
			+ "    TableId        TEXT,"
			+ "    CreateDate    TEXT,"
			+ "    JsonString    TEXT,"
			+ "    ControlID    TEXT,"
			+ "    ControlValue    TEXT" +
			");";
	public DynamicTinbaoDB queryDynamicTianBao(int userId,int ProejctId,int storeId,int TableId,String date){
		DynamicTinbaoDB dynamicTinbaoDB=null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			db.beginTransaction();
			String sql = "select * from DynamicTianbao where UserId = "+ userId+ " and ProejctId ="+ProejctId+ " and StoreId ="+storeId+ " and TableId ="+TableId+" and CreateDate='"+date+"';";
			try {
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					 dynamicTinbaoDB = new DynamicTinbaoDB();
					dynamicTinbaoDB.setUserId(userId);
					dynamicTinbaoDB.setStoreId(storeId);
					dynamicTinbaoDB.setCreateDate(date);
					dynamicTinbaoDB.setProejctId(ProejctId);
					dynamicTinbaoDB.setTableId(TableId);
					dynamicTinbaoDB.setJsonString(cursor.getString(cursor.getColumnIndex("JsonString")));
					dynamicTinbaoDB.setControlID(cursor.getString(cursor.getColumnIndex("ControlID")));
					dynamicTinbaoDB.setControlValue(cursor.getString(cursor.getColumnIndex("ControlValue")));
				}
			} catch (Exception e) {
				Log.e("err", "insert failed");
				e.printStackTrace();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return dynamicTinbaoDB;
	}

	public List<DynamicTinbaoDB> queryDynamicTianBaoByUserId(int userId){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			List<DynamicTinbaoDB> uploadList=new ArrayList<DynamicTinbaoDB>();
			String sql = "select * from DynamicTianbao where UserId = "+ userId+";";
			try {
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					DynamicTinbaoDB dynamicTinbaoDB = new DynamicTinbaoDB();
					dynamicTinbaoDB.setUserId(userId);
					dynamicTinbaoDB.setStoreId(cursor.getInt(cursor.getColumnIndex("StoreId")));
					dynamicTinbaoDB.setCreateDate(cursor.getString(cursor.getColumnIndex("CreateDate")));
					dynamicTinbaoDB.setProejctId(cursor.getInt(cursor.getColumnIndex("ProejctId")));
					dynamicTinbaoDB.setTableId(cursor.getInt(cursor.getColumnIndex("TableId")));
					dynamicTinbaoDB.setJsonString(cursor.getString(cursor.getColumnIndex("JsonString")));
					dynamicTinbaoDB.setControlID(cursor.getString(cursor.getColumnIndex("ControlID")));
					dynamicTinbaoDB.setControlValue(cursor.getString(cursor.getColumnIndex("ControlValue")));
					uploadList.add(dynamicTinbaoDB);
				}
			} catch (Exception e) {
				Log.e("err", "queryDynamicTianBaoByUserId failed");
				e.printStackTrace();
			}
			db.close();
			return uploadList;
		}
	}

	public boolean deleteDynamicTianbao(int userId,int ProejctId,int storeId,int TableId,String date){
		boolean flag=false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			String sql = "delete from DynamicTianbao where UserId = "+ userId+ " and ProejctId ="+ProejctId+ " and StoreId ="+storeId+ " and TableId ="+TableId+" and CreateDate='"+date+"';";
			try {
				db.execSQL(sql);
				db.setTransactionSuccessful();
				flag = true;
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return flag;
	}


	//**********************************************照片**********************************************************************
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

	public boolean insertPictures(PictureInfo pictureInfo){
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			String spl = "insert into Pictures values('"
						+pictureInfo.getUserId()+"','"
						+pictureInfo.getStoreId()+"','"
						+pictureInfo.getControlId()+"','"
						+pictureInfo.getLat()+"','"
						+pictureInfo.getLng()+"','"
						+pictureInfo.getCreateDate()+"','"
					    +pictureInfo.getPicturePath()+"','"
						+""+"','"
						+""+"')";
				try {
					db.execSQL(spl);
				} catch (Exception e) {
					Log.e("error", "insert failed:"+e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
			db.close();
			return true;
		}
	public List<PictureInfo> queryPicturesByUserIdControlId(int StoreId,int ControlId){
		List<PictureInfo> pictureInfos = new ArrayList<PictureInfo>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			db.beginTransaction();
			String sql = "select * from Pictures where StoreId = "+ StoreId+" and ControlID='"+ControlId+"';";
			try {
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					PictureInfo	pictureInfo = new PictureInfo();
					pictureInfo.setStoreId(StoreId);
					pictureInfo.setControlId(ControlId);
					pictureInfo.setUserId(cursor.getInt(cursor.getColumnIndex("UserId")));
					pictureInfo.setLat(Double.valueOf(cursor.getString(cursor.getColumnIndex("Lat"))));
					pictureInfo.setLng(Double.valueOf(cursor.getString(cursor.getColumnIndex("Lng"))));
                    pictureInfo.setCreateDate(cursor.getString(cursor.getColumnIndex("CreateDate")));
					pictureInfo.setPicturePath(cursor.getString(cursor.getColumnIndex("PicturePath")));
					pictureInfos.add(pictureInfo);
				}
			} catch (Exception e) {
				Log.e("err", "insert failed");
				e.printStackTrace();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return pictureInfos;
	}

	public List<PictureInfo> queryALLPicturesByUserId(int UserId){
		List<PictureInfo> pictureInfos = new ArrayList<PictureInfo>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getReadableDatabase();
			}
			db.beginTransaction();
			String sql = "select * from Pictures where UserId = "+ UserId+";";
			try {
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					PictureInfo	pictureInfo = new PictureInfo();
					pictureInfo.setUserId(UserId);
					pictureInfo.setStoreId(cursor.getInt(cursor.getColumnIndex("StoreId")));
					pictureInfo.setControlId(cursor.getInt(cursor.getColumnIndex("ControlID")));

					pictureInfo.setLat(Double.valueOf(cursor.getString(cursor.getColumnIndex("Lat"))));
					pictureInfo.setLng(Double.valueOf(cursor.getString(cursor.getColumnIndex("Lng"))));
					pictureInfo.setCreateDate(cursor.getString(cursor.getColumnIndex("CreateDate")));
					pictureInfo.setPicturePath(cursor.getString(cursor.getColumnIndex("PicturePath")));
					pictureInfos.add(pictureInfo);
				}
			} catch (Exception e) {
				Log.e("err", "insert failed");
				e.printStackTrace();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return pictureInfos;
	}

	public boolean deletePicturesByPath(String path){
		boolean flag=false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			String sql = "delete from Pictures where PicturePath='"+path+"';";
			try {
				db.execSQL(sql);
				db.setTransactionSuccessful();
				flag = true;
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return flag;
	}
}
