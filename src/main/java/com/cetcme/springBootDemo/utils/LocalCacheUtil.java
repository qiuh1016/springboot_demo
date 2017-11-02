package com.cetcme.springBootDemo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.Alarm;
import com.cetcme.springBootDemo.domain.Command;

public class LocalCacheUtil {

	private static ConcurrentHashMap<String, AcqData> acqDataMap = new ConcurrentHashMap<String, AcqData>();

	private static List<Alarm> alarmList = new ArrayList<Alarm>();

	private static List<Command> waitToSendCommandList = new ArrayList<Command>();

	public static ConcurrentHashMap<String, AcqData> getAcqDataMap() {
		return acqDataMap;
	}

	public static List<Alarm> getAlarmList() {
		return alarmList;
	}

	public static List<Command> getWaitToSendCommandList() {
		return waitToSendCommandList;
	}

	public static void addAcqData(AcqData acqData) {
		acqDataMap.put(acqData.getDeviceNo(), acqData);
	}

	public static void addAlarm(Alarm alarm) {
		synchronized (alarmList) {
			alarmList.add(alarm);
		}
	}

	public static void addWaitToSendCommand(Command command) {
		synchronized (waitToSendCommandList) {
			waitToSendCommandList.add(command);
		}
	}

	public static void removeWaitToSendCommand(Command command) {
		synchronized (waitToSendCommandList) {
			waitToSendCommandList.remove(command);
		}
	}

	public static void clearAlarmList() {
		synchronized (alarmList) {
			alarmList.clear();
		}
	}

	public static void clearAcqDataMap() {
		acqDataMap.clear();
	}
}
