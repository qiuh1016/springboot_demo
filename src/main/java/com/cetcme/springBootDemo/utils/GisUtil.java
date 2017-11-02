package com.cetcme.springBootDemo.utils;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cetcme.springBootDemo.domain.GpsPosition;

public class GisUtil {

	private static final int GPS_DEGREE_LENGTH = 3;
	private static final int GEO_MAX_DIGITS_LENGTH = 7;
	private static final double EARTH_RADIUS = 6378137;
	private static final double NAUTICAL_MILE = 1852;
	public static final double DISDANCE_PER_LONGITUDE = 111712.69150641055729984301412873;
	public static final double DISDANCE_PER_LATITUDE = 102834.74258026089786013677476285;

	/**
	 * 
	 * 将GPS格式：dddmm.mmmmm转换成GPRS格式：ddd.ddddddd
	 */
	public static Double gps2Gprs(final String gpsValue) {
		String value = StringUtils.leftPad(gpsValue, 11, '0');
		String degreeStr = StringUtils.left(value, GPS_DEGREE_LENGTH);
		double degree = ConvertUtil.string2Double(degreeStr);
		String minuteStr = StringUtils.right(value, value.length() - GPS_DEGREE_LENGTH);
		double minute = ConvertUtil.string2Double(minuteStr) / 60;
		return degree + minute;
	}

	/**
	 * 获取GPRS格式的经纬度
	 */
	public static Double getGprsFormatData(byte gprsFlag, String value) {
		double temp = CommonUtil.isTrue(gprsFlag) ? ConvertUtil.string2Double(value) : gps2Gprs(value);
		return format(temp, GEO_MAX_DIGITS_LENGTH);
	}

	/**
	 * 四舍五入保留小数点
	 */
	public static Double format(double value, int digits) {
		BigDecimal temp = new BigDecimal(value);
		return temp.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取两个坐标间的距离(单位：海里)
	 */
	public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
		double a, b;
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (lon1 - lon2) * Math.PI / 180.0;
		double result;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		result = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return result / NAUTICAL_MILE;
	}

	/**
	 * 判断点是否在矩形范围内
	 */
	public static boolean isPointInRect(GpsPosition point, Double lonMax, Double lonMin, Double latMax, Double latMin) {

		if (lonMax == null || lonMin == null || latMin == null || latMax == null) {
			return false;
		}

		double longitude = point.getLongitude();
		double latitude = point.getLatitude();
		if (longitude > lonMax || longitude < lonMin || latitude > latMax || latitude < latMin) {
			return true;
		}
		return false;

	}

	/**
	 * 判断点是否在多边形范围内
	 */
	public static boolean isPointInPolygon(GpsPosition point, List<GpsPosition> vertices) {
//		int verticesCount = vertices.size();
//		int nCross = 0;
//		for (int i = 0; i < verticesCount; ++i) {
//			GpsPosition p1 = vertices.get(i);
//			GpsPosition p2 = vertices.get((i + 1) % verticesCount);
//
//			// 求解 y=p.y 与 p1 p2 的交点
//			if (p1.getLatitude() == p2.getLatitude()) { // p1p2 与 y=p0.y平行
//				continue;
//			}
//			if (point.getLatitude() < Math.min(p1.getLatitude(), p2.getLatitude())) { // 交点在p1p2延长线上
//				continue;
//			}
//			if (point.getLatitude() >= Math.max(p1.getLatitude(), p2.getLatitude())) { // 交点在p1p2延长线上
//				continue;
//			}
//			// 求交点的 X 坐标
//			double x = (point.getLatitude() - p1.getLatitude()) * (p2.getLongitude() - p1.getLongitude())
//					/ (p2.getLatitude() - p1.getLatitude()) + p1.getLongitude();
//			if (x > point.getLongitude()) { // 只统计单边交点
//				nCross++;
//			}
//		}
//		// 单边交点为偶数，点在多边形之外
//		return (nCross % 2 == 1);
		
		java.awt.Polygon polygon = new java.awt.Polygon();
		// java.awt.geom.GeneralPath
		final int TIMES = 10000000;
		for (GpsPosition p : vertices) {
			int x = (int) (p.getLongitude() * TIMES);
			int y = (int) (p.getLatitude() * TIMES);
			polygon.addPoint(x, y);
		}
		int x = (int) (point.getLongitude() * TIMES);
		int y = (int) (point.getLatitude() * TIMES);
		return polygon.contains(x, y);
	}

	/**
	 * 判断点是否在圆形范围内
	 */
	public static boolean isPointInCycle(GpsPosition point, GpsPosition circlePoint, double radius) {
		BigDecimal bd1 = new BigDecimal(point.longitude - circlePoint.longitude);
		bd1 = bd1.multiply(bd1);
		bd1 = bd1.multiply(new BigDecimal(DISDANCE_PER_LONGITUDE));
		bd1 = bd1.multiply(new BigDecimal(DISDANCE_PER_LONGITUDE));
		
		BigDecimal bd2 = new BigDecimal(point.latitude - circlePoint.latitude);
		bd2 = bd2.multiply(bd2);
		bd2 = bd2.multiply(new BigDecimal(DISDANCE_PER_LATITUDE));
		bd2 = bd2.multiply(new BigDecimal(DISDANCE_PER_LATITUDE));
		
		bd1 = bd1.add(bd2);
		
		BigDecimal bd3 = new BigDecimal(radius);
		bd3 = bd3.multiply(bd3);
		
		return (bd1.compareTo(bd3) <= 0);
		
//		double distance = (point.longitude - circlePoint.longitude) * (point.longitude - circlePoint.longitude)
//				* (double) DISDANCE_PER_LONGITUDE * (double) DISDANCE_PER_LONGITUDE
//				+ (point.latitude - circlePoint.latitude) * (point.latitude - circlePoint.latitude)
//						* (double) DISDANCE_PER_LATITUDE * (double) DISDANCE_PER_LATITUDE;
//		return (distance <= radius * radius);
	}
}
