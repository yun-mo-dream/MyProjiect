package com.OtherUtils.ShowPictureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * �ļ��������߰�
 */
public class FileUtils {
	/**
	 * д�ı��ļ� ��Androidϵͳ�У��ļ������� /data/data/PACKAGE_NAME/files Ŀ¼��
	 * 
	 * @param context
	 */
	List<Bitmap> list = null;
	List<String> lu = null;

	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);

			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(Context context, String fileName, byte[] data) {
		if (data == null) {// ����Ϊ��
			return;
		}
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(data);

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteAll(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteAll(childFiles[i]);
			}
			file.delete();
		}
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		Log.i(folderPath, fileName);
		return new File(folderPath, fileName);
	}

	public static boolean TxtFile(String txtPath, String txtName, String bytes)
			throws Exception {
		boolean type = false;
		// �ж�SDcard �Ƿ����?
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			saveToSDCard(txtPath, txtName, bytes);
			type = true;

		}
		return type;
	}

	public static void saveToSDCard(String txtPath, String name, String bytes)
			throws Exception {
		// ��ȡ��洢�豸·��Environment.getExternalStorageDirectory()
		File file = new File(Environment.getExternalStorageDirectory()
				+ txtPath, name + ".txt");
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(bytes.getBytes());
		outStream.close();
	}

	/**
	 * ���ֻ�д�ļ� folderΪsd�µ�Ŀ¼�����ذ���sdĿ¼
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeSDFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * ����Ŀ¼���ļ������ֻ�д�ļ�
	 * 
	 * @param buffer
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, File file) {
		boolean writeSucc = false;

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * �����ļ�����·����ȡ�ļ���
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * �����ļ��ľ���·����ȡ�ļ�������������չ��
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 * ��ȡ�ļ���չ��
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * ��ȡ�ļ���С
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * ��ȡ�ļ���С
	 * 
	 * @param size
	 *            �ֽ�
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * ת���ļ���С
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * ��ȡĿ¼�ļ���С
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // �ݹ���ü���ͳ��?
			}
		}
		return dirSize;
	}

	/**
	 * ��ȡĿ¼�ļ�����
	 * 
	 * @param
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// �ݹ�
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * ����ļ��Ƿ����
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * ���·���Ƿ����
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * ����SD����ʣ��ռ�?
	 * 
	 * @return ����-1��˵��û�а�װsd��
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * �½�Ŀ¼
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status = false;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			if (!newPath.exists()) {
				status = newPath.mkdir();
			}
		} else
			status = false;
		return status;
	}

	/**
	 * ����Ƿ�װSD��
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * ����Ƿ�װ���õ�SD��
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {

		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * ɾ��Ŀ¼(������Ŀ¼��������ļ�?)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.i("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					Log.i("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * ɾ����Ŀ¼
	 * <p/>
	 * ���� 0����ɹ�? ,1 ����û��ɾ��Ȩ��, 2�����ǿ�Ŀ¼,3 ����δ֪����
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if (!f.canWrite()) {
			return 1;
		}
		if (f.list() != null && f.list().length > 0) {
			return 2;
		}
		if (f.delete()) {
			return 0;
		}
		return 3;
	}

	/**
	 * ������
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			Log.i("DirectoryManager deleteFile", filePath);
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * ���һ���ļ���?
	 */
	public static void clearFileWithPath(String filePath) {
		Log.i("aa", filePath);
		if (new File(filePath).exists()) {
			List<File> files = FileUtils.listPathFiles(filePath);
			if (files.isEmpty()) {
				return;
			}
			for (File f : files) {
				if (f.isDirectory()) {
					clearFileWithPath(f.getAbsolutePath());
				} else {
					f.delete();
				}
			}
		}
	}

	/**
	 * ��ȡSD���ĸ�Ŀ¼
	 * 
	 * @return
	 */
	public static String getSDRoot() {

		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * ��ȡ�ֻ�����SD���ĸ�Ŀ¼
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {

		Map<String, String> evn = System.getenv();

		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * �г�rootĿ¼��������Ŀ¼
	 * 
	 * @return ����·��
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// ���˵���.��ʼ���ļ���
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * ��ȡһ���ļ����µ������ļ�
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}

	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * ����Ŀ¼
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if (path.exists()) {
			return PathStatus.EXITS;
		}
		if (path.mkdir()) {
			return PathStatus.SUCCESS;
		} else {
			return PathStatus.ERROR;
		}
	}

	/**
	 * ��ȡ·����
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator) + 1;
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}

	/**
	 * ��ȡӦ�ó��򻺴��ļ����µ�ָ��Ŀ¼
	 * 
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir
				+ "/";
		File savedir = new File(savePath);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		savedir = null;
		return savePath;
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
		int roateDegree = readPictureDegree(imagePath);
		bitmap =rotaingImageView(roateDegree, bitmap);
		return bitmap;
	}
	
	
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
	  // ��תͼƬ ����
	  Matrix matrix = new Matrix();
	  matrix.postRotate(angle);
	  System.out.println("angle=" + angle);
	  // �����µ�ͼƬ
	  Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
	    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	  return resizedBitmap;
	 }
	/**
	 * �����ļ��е�·����ȡ���ļ����е�ͼƬ
	 * 
	 * @param path
	 * @return
	 */
	public List<Bitmap> listBitmap(String path,List<String> pathList) {
		lu = GetFiles(path, "jpg");
		List<Bitmap> list = new ArrayList<Bitmap>();
		for (int i = 0; i < lu.size(); i++) {
			Bitmap bitmap = getImageThumbnail(lu.get(i), 200, 200);
			pathList.add(lu.get(i));
			Log.i("�����ļ�����", "" + lu.size());
			if (bitmap != null) {
				Log.i("��������-�ļ�·����", lu.get(i));
				list.add(bitmap);
			} else {
				Log.i("��ӡbitmap", "bitamp��null");
			}
		}
		return list;
	}

	// ����Ŀ¼����չ�����Ƿ�������ļ���?
	public List<String> GetFiles(String Path, String Extension) {
		List<String> listb = new ArrayList<String>();
		File file = new File(Path);
		if (file.exists()) {
			if (file.canRead()) {
				File[] filess = file.listFiles();
				for (File f : filess) {
					if (!f.isDirectory()) {
						String filename = f.getName();
						String filesuff = filename.substring(filename
								.lastIndexOf(".") + 1);
						if (Extension.equals(filesuff)) {
							listb.add(Path +File.separator+ filename);
							Log.i("��ӡͼƬ��ַ", "" + Path + filename);
						}
					}
				}
			}
		}
		return listb;
	}
}