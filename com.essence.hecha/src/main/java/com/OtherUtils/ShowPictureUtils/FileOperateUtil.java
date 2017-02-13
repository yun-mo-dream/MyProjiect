package com.OtherUtils.ShowPictureUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.content.Context;

import com.essence.hechaSystem.R;


/** 
 * @ClassName: FileOperateUtil 
 * @Description:  锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
 * @author LinJ
 * @date 2014-12-31 锟斤拷锟斤拷9:44:38 
 *  
 */
public class FileOperateUtil {
	public final static String TAG="FileOperateUtil";

	public final static int ROOT=0;//锟斤拷目录
	public final static int TYPE_IMAGE=1;//图片
	public final static int TYPE_THUMBNAIL=2;//锟斤拷锟斤拷图
	public final static int TYPE_VIDEO=3;//锟斤拷频

	/**
	 *锟斤拷取锟侥硷拷锟斤拷路锟斤拷
	 * @param type 锟侥硷拷锟斤拷锟斤拷锟�
	 * @param rootPath 锟斤拷目录锟侥硷拷锟斤拷锟斤拷锟斤拷 为业锟斤拷锟斤拷水锟斤拷
	 * @return
	 */
	public static String getFolderPath(Context context,int type,String rootPath) {
		//锟斤拷业锟斤拷锟侥硷拷锟斤拷目录
		StringBuilder pathBuilder=new StringBuilder();
		//锟斤拷锟接︼拷么娲⒙凤拷锟�
		pathBuilder.append(context.getExternalFilesDir(null).getAbsolutePath());
		pathBuilder.append(File.separator);
		//锟斤拷锟斤拷募锟斤拷锟侥柯�
		pathBuilder.append(context.getString(R.string.Files));
		pathBuilder.append(File.separator);
		//锟斤拷拥锟饺伙拷募锟斤拷锟斤拷锟铰凤拷锟�
		pathBuilder.append(rootPath);
		pathBuilder.append(File.separator);
		switch (type) {
		case TYPE_IMAGE:
			pathBuilder.append(context.getString(R.string.Image));
			break;
		case TYPE_VIDEO:
			pathBuilder.append(context.getString(R.string.Video));
			break;
		case TYPE_THUMBNAIL:
			pathBuilder.append(context.getString(R.string.Thumbnail));
			break;
		default:
			break;
		}
		return pathBuilder.toString();
		
	}

	/**
	 * 锟斤拷取目锟斤拷锟侥硷拷锟斤拷锟斤拷指锟斤拷锟斤拷缀锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷,锟斤拷锟斤拷锟睫革拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * @param file 目锟斤拷锟侥硷拷锟斤拷路锟斤拷
	 * @param content 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷,锟斤拷锟皆诧拷锟斤拷锟斤拷频锟斤拷锟斤拷图
	 * @return
	 */
	public static List<File> listFiles(String file,final String format,String content){
		return listFiles(new File(file), format,content);
	}
	public static List<File> listFiles(String file,final String format){
		return listFiles(new File(file), format,null);
	}
	/**
	 * 锟斤拷取目锟斤拷锟侥硷拷锟斤拷锟斤拷指锟斤拷锟斤拷缀锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷,锟斤拷锟斤拷锟睫革拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * @param file 目锟斤拷锟侥硷拷锟斤拷
	 * @param extension 指锟斤拷锟斤拷缀锟斤拷
	 * @param content 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷,锟斤拷锟皆诧拷锟斤拷锟斤拷频锟斤拷锟斤拷图
	 * @return
	 */
	public static List<File> listFiles(File file,final String extension,final String content){
		File[] files=null;
		if(file==null||!file.exists()||!file.isDirectory())
			return null;
		files=file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				// TODO Auto-generated method stub
				if(content==null||content.equals(""))
					return arg1.endsWith(extension);
				else {
                    return  arg1.contains(content)&&arg1.endsWith(extension);           		
				}
			}
		});
		if(files!=null){
			List<File> list=new ArrayList<File>(Arrays.asList(files));
			sortList(list, false);
			return list;
		}
		return null;
	}

	/**  
	 *  锟斤拷锟斤拷锟睫革拷时锟斤拷为锟侥硷拷锟叫憋拷锟斤拷锟斤拷
	 *  @param list 锟斤拷锟斤拷锟斤拷募锟斤拷斜锟�
	 *  @param asc  锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷 true为锟斤拷锟斤拷 false为锟斤拷锟斤拷 
	 */
	public static void sortList(List<File> list,final boolean asc){
		//锟斤拷锟睫革拷锟斤拷锟斤拷锟斤拷锟斤拷
		Collections.sort(list, new Comparator<File>() {
			public int compare(File file, File newFile) {
				if (file.lastModified() > newFile.lastModified()) {
					if(asc){
						return 1;
					}else {
						return -1;
					}
				} else if (file.lastModified() == newFile.lastModified()) {
					return 0;
				} else {
					if(asc){
						return -1;
					}else {
						return 1;
					}
				}

			}
		});
	}

	/**
	 * 
	 * @param extension 锟斤拷缀锟斤拷 锟斤拷".jpg"
	 * @return
	 */
	public static String createFileNmae(String extension){
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
		// 转锟斤拷为锟街凤拷锟斤拷
		String formatDate = format.format(new Date());
		//锟介看锟角凤拷锟�"."
		if(!extension.startsWith("."))
			extension="."+extension;
		return formatDate+extension;
	}

	/**  
	 *  删锟斤拷锟斤拷锟斤拷图 同时删锟斤拷源图锟斤拷源锟斤拷频
	 *  @param thumbPath 锟斤拷锟斤拷图路锟斤拷
	 *  @return   
	 */
	public static boolean deleteThumbFile(String thumbPath,Context context) {
		boolean flag = false;

		File file = new File(thumbPath);
		if (!file.exists()) { // 锟侥硷拷锟斤拷锟斤拷锟斤拷直锟接凤拷锟斤拷
			return flag;
		}

		flag = file.delete();
		//源锟侥硷拷路锟斤拷
		String sourcePath=thumbPath.replace(context.getString(R.string.Thumbnail),
				context.getString(R.string.Image));
		file = new File(sourcePath);
		if (!file.exists()) { // 锟侥硷拷锟斤拷锟斤拷锟斤拷直锟接凤拷锟斤拷
			return flag;
		}
		flag = file.delete();
		return flag;
	}
	/**  
	 *  删锟斤拷源图锟斤拷源锟斤拷频 同时删锟斤拷锟斤拷锟斤拷图
	 *  @param sourcePath 锟斤拷锟斤拷图路锟斤拷
	 *  @return   
	 */
	public static boolean deleteSourceFile(String sourcePath,Context context) {
		boolean flag = false;

		File file = new File(sourcePath);
		if (!file.exists()) { // 锟侥硷拷锟斤拷锟斤拷锟斤拷直锟接凤拷锟斤拷
			return flag;
		}

		flag = file.delete();
		//锟斤拷锟斤拷图锟侥硷拷路锟斤拷
		String thumbPath=sourcePath.replace(context.getString(R.string.Image),
				context.getString(R.string.Thumbnail));
		file = new File(thumbPath);
		if (!file.exists()) { // 锟侥硷拷锟斤拷锟斤拷锟斤拷直锟接凤拷锟斤拷
			return flag;
		}
		flag = file.delete();
		return flag;
	}
}