package com.OtherUtils.ShowPictureUtils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaixuepan on 14-6-3.
 */
public class DateUtils {

	public static String getDateStr(String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(new Date());
	}

	public static String getDateStr() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date());
	}

	public static String getDateStr2() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date()).replace(" ", "T");
	}

	// ���ַ���תΪ����
	public static Date ConverToDate(String strDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static int getWeek(String pTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static String getDay(int year, int month, int day, int num) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month, day);// �·��Ǵ�0��ʼ�ģ�����11��ʾ12��
		// Date now = c.getTime();
		c.add(Calendar.DATE, num);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(c.getTime()).substring(8, 10);
	}

	public static String getDate(int year, int month, int day, int num) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month, day);// �·��Ǵ�0��ʼ�ģ�����11��ʾ12��
		c.add(Calendar.DATE, num);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(c.getTime()).toString();
	}

	public static String getWeeks(String date) {
		Log.i("date", date);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7)) - 1;
		int day = Integer.parseInt(date.substring(8, 10));

		int a = getWeek(date);
		String startDate = null;
		String endDate = null;
		switch (a) {
		case 1:
			startDate = getDate(year, month, day, -6);
			endDate = date;
			break;
		case 2:
			startDate = date;
			endDate = getDate(year, month, day, +6);
			break;
		case 3:
			startDate = getDate(year, month, day, -1);
			endDate = getDate(year, month, day, +5);
			break;
		case 4:
			startDate = getDate(year, month, day, -2);
			endDate = getDate(year, month, day, +4);
			break;
		case 5:
			startDate = getDate(year, month, day, -3);
			endDate = getDate(year, month, day, +3);
			break;
		case 6:
			startDate = getDate(year, month, day, -4);
			endDate = getDate(year, month, day, +2);
			break;
		case 7:
			startDate = getDate(year, month, day, -5);
			endDate = getDate(year, month, day, +1);
			break;

		}
		String time = startDate + "��" + endDate;
		Log.i("date", time);
		return time;
	}

	// ���ݽ������ڻ�ȡ ����һ������ ������ĩ������
	public static List<String> getWeeks2(String date) {
		List<String> timeList = new ArrayList<String>();
		Log.i("date", date);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7)) - 1;
		int day = Integer.parseInt(date.substring(8, 10));

		int a = getWeek(date);
		String startDate = null;
		String endDate = null;
		switch (a) {
		case 1:
			startDate = getDate(year, month, day, -6);
			endDate = date;
			break;
		case 2:
			startDate = date;
			endDate = getDate(year, month, day, +6);
			break;
		case 3:
			startDate = getDate(year, month, day, -1);
			endDate = getDate(year, month, day, +5);
			break;
		case 4:
			startDate = getDate(year, month, day, -2);
			endDate = getDate(year, month, day, +4);
			break;
		case 5:
			startDate = getDate(year, month, day, -3);
			endDate = getDate(year, month, day, +3);
			break;
		case 6:
			startDate = getDate(year, month, day, -4);
			endDate = getDate(year, month, day, +2);
			break;
		case 7:
			startDate = getDate(year, month, day, -5);
			endDate = getDate(year, month, day, +1);
			break;

		}
		timeList.add(startDate);
		timeList.add(endDate);
		return timeList;
	}

	// ���ݽ������ڻ�ȡ ���ܶ������� ������һ������
	public static List<String> getWeeks3(String date) {
		List<String> timeList = new ArrayList<String>();
		Log.i("date", date);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7)) - 1;
		int day = Integer.parseInt(date.substring(8, 10));

		int a = getWeek(date);
		String startDate = null;
		String endDate = null;
		switch (a) {
		case 1:
			startDate = getDate(year, month, day, -5);
			endDate = getDate(year, month, day, +1);
			break;
		case 2:
			startDate = getDate(year, month, day, +1);
			endDate = getDate(year, month, day, +7);
			break;
		case 3:
			startDate = getDate(year, month, day, 0);
			endDate = getDate(year, month, day, +6);
			break;
		case 4:
			startDate = getDate(year, month, day, -1);
			endDate = getDate(year, month, day, +5);
			break;
		case 5:
			startDate = getDate(year, month, day, -2);
			endDate = getDate(year, month, day, +4);
			break;
		case 6:
			startDate = getDate(year, month, day, -3);
			endDate = getDate(year, month, day, +3);
			break;
		case 7:
			startDate = getDate(year, month, day, -4);
			endDate = getDate(year, month, day, +2);
			break;

		}
		timeList.add(startDate);
		timeList.add(endDate);
		return timeList;
	}
}
