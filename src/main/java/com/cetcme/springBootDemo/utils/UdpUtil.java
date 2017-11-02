package com.cetcme.springBootDemo.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpUtil {

	static Logger logger = LoggerFactory.getLogger(UdpUtil.class);

	// 注意，由于设备通信过程中经过多层路由，socket中包含路由信息，所以发送和接受必须要用同一个DatagramSocket实例
	private static DatagramSocket socket = null;

	public static DatagramSocket getSocket() {
		return socket;
	}

	// 取得消息的头信息
	public static String getHeader(String message) {
		return StringUtils.left(message, Constants.MESSAGE_HEADER_LENGTH);
	}

	public static Boolean createSocket(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			logger.error(String.format("DatagramSocket初始化失败:%s", e.getCause()));
			return false;
		}
		return true;
	}

	public static int computeCheckSum(byte[] buf, int start, int end) {
		int sum = 0;
		for (int i = start; i < end; i++) {
			sum += (buf[i] + 256) % 256;
		}
		return (sum & 0xFF);
	}

	public static int computeCheckSum2(byte[] buf, int start, int end) {
		int sum = 0;
		for (int i = start; i < end; i++) {
			sum += (buf[i] + 256) % 256;
		}
		return (sum & 0xFFFF);
	}

	public static int computeCheckSum(String value) {
		int sum = 0;
		for (int i = 0; i < value.length(); i++) {
			sum += value.charAt(i) & 0xFF;
		}
		return (sum & 0xFF);
	}

	/*
	 * 消息校验
	 */
	public static boolean isValidMessage(byte[] data, int start, int end, int checkSum) {

		// 根据该消息的数据段计算出校验和
		int computedCheckSum = computeCheckSum(data, start, end);

		// 如果计算出的校验和与接收到的校验和相等，则说明该消息是有效的
		return computedCheckSum == checkSum;
	}

	/**
	 * 
	 * @param date
	 * @param delayTime
	 * @return
	 */
	public static boolean isInvalidTime(final Date date, final long delayTimeMs) {

		if (date == null) {
			return true;
		}

		Date current = new Date();

		// 如果时间超过丢弃时间，则为超时无效
		Date minDate;
		Date maxDate;
		if (delayTimeMs > Integer.MAX_VALUE) {
			int delayTimeDays = (int) (delayTimeMs / DateUtils.MILLIS_PER_DAY);
			minDate = DateUtils.addDays(current, -delayTimeDays);
			maxDate = DateUtils.addDays(current, delayTimeDays);
		} else {
			minDate = DateUtils.addMilliseconds(current, (int) -delayTimeMs);
			maxDate = DateUtils.addMilliseconds(current, (int) delayTimeMs);
		}

		return date.before(minDate) || date.after(maxDate);
	}

	public static void send(String message, DatagramPacket packet) {
		String logFormat = "向设备【IP:%s Port:%d】发送信息：%s";
		logger.info(String.format(logFormat, packet.getAddress().getHostAddress(), packet.getPort(), message));
		byte[] buf = message.getBytes();
		send(buf, packet);
	}

	public static void send(byte[] buf, DatagramPacket packet) {
		String msg = ConvertUtil.turnBytesToString(buf, 0, buf.length);
		logger.info("向设备【IP:{} Port:{}】发送信息：{}", packet.getAddress().getHostAddress(), packet.getPort(), msg);
		packet.setData(buf);
		try {
			socket.send(packet);
		} catch (IOException e) {
			logger.error("消息发送失败:", e.getMessage());
		}
	}
}
