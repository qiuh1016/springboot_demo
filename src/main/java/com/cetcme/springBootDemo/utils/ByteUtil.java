package com.cetcme.springBootDemo.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

public class ByteUtil {

	static Logger logger = LoggerFactory.getLogger(ByteUtil.class);

	public static int indexOf(byte[] array, byte c) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == c) {
				return i;
			}
		}
		return -1;
	}

	public static byte[] subBytes(byte[] array, int from, int to) {
		if (from < 0 || to < 0 || to > array.length || from >= to) {
			throw new IllegalArgumentException("Invalid from, to");
		}

		byte[] bytes = new byte[to - from];
		for (int c = 0, i = from; i < to; ++c, ++i) {
			bytes[c] = array[i];
		}
		return bytes;
	}

	public static byte[] subBytes(byte[] array, int from) {
		return subBytes(array, from, array.length);
	}

	public static byte[] getByte(String strValue) {
		try {
			return StringUtils.getBytes(strValue, "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			logger.error("字符串转换成BYTE数组出错:", e.getCause());
		}
		return null;
	}

	public static byte[] byteMerger(byte[] data1, byte[] data2) {
		byte[] result = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, result, 0, data1.length);
		System.arraycopy(data2, 0, result, data1.length, data2.length);
		return result;
	}
}
