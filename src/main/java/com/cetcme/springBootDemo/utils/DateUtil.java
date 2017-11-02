package com.cetcme.springBootDemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.cetcme.springBootDemo.utils.Constants.DatePattern;
import com.cetcme.springBootDemo.utils.Constants.DateUnitType;

public class DateUtil {

	// 一年以365天计算
	private static final long MILLIS_PER_YEAR = 365 * DateUtils.MILLIS_PER_DAY;
	// 一月以30天计算
	private static final long MILLIS_PER_MONTH = 30 * DateUtils.MILLIS_PER_DAY;

	protected DateUtil() {
	}

	private static final DateUtil dateUtil = new DateUtil();

	public static DateUtil getInstance() {
		return dateUtil;
	}

	public static Date parseStringToDate(String arg, DatePattern pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern.toString()).parse(arg);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}

	public static String parseDateToString(Date arg, DatePattern pattern) {
		return arg == null ? "" : new SimpleDateFormat(pattern.toString()).format(arg);
	}

	public static String parseDateToString(Date arg) {
		return parseDateToString(arg, DatePattern.YYYYMMDDHHMMSS);
	}

	public static Date parseStringToDate(String arg) {
		return parseStringToDate(arg, DatePattern.YYYYMMDDHHMMSS);
	}

	public static Double convertToMiliSecond(final DateUnitType unit, final String strValue) {
		Double doubleValue = Double.valueOf(strValue);
		long ratio = 1;
		switch (unit) {
		case YEAR:
			ratio = MILLIS_PER_YEAR;
			break;
		case MONTH:
			ratio = MILLIS_PER_MONTH;
			break;
		case DAY:
			ratio = DateUtils.MILLIS_PER_DAY;
			break;
		case HOUR:
			ratio = DateUtils.MILLIS_PER_HOUR;
			break;
		case MINUTE:
			ratio = DateUtils.MILLIS_PER_MINUTE;
			break;
		case SECOND:
			ratio = DateUtils.MILLIS_PER_SECOND;
			break;
		default:
			break;
		}
		return doubleValue * ratio;
	}
}
