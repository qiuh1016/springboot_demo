package com.cetcme.springBootDemo.domain;

import java.util.Date;

public class PushLog {
	
	private Integer logId;
	
	private String boatCode;
	
	private String alarmCode;
	
	private String alarmName;
	
	private Integer alarmFlag;
	
	private Date alarmTime;
	
	private Integer pushFlag;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getBoatCode() {
		return boatCode;
	}

	public void setBoatCode(String boatCode) {
		this.boatCode = boatCode;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public Integer getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(Integer alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	public Integer getPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(Integer pushFlag) {
		this.pushFlag = pushFlag;
	}
}