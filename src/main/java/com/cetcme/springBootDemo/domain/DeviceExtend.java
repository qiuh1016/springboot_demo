package com.cetcme.springBootDemo.domain;

import java.util.Date;

public class DeviceExtend extends Device {

	private static final long serialVersionUID = 1L;

	private Long shipId;

	private String shipNo;

	private String shipName;

	private String picName;

	private String picTelNo;

	private Date cfsStartDate;

	private Date cfsEndDate;
	
	private Integer signalStrength;

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public String getShipNo() {
		return shipNo;
	}

	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicTelNo() {
		return picTelNo;
	}

	public void setPicTelNo(String picTelNo) {
		this.picTelNo = picTelNo;
	}

	public Date getCfsStartDate() {
		return cfsStartDate;
	}

	public void setCfsStartDate(Date cfsStartDate) {
		this.cfsStartDate = cfsStartDate;
	}

	public Date getCfsEndDate() {
		return cfsEndDate;
	}

	public void setCfsEndDate(Date cfsEndDate) {
		this.cfsEndDate = cfsEndDate;
	}

	public Integer getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(Integer signalStrength) {
		this.signalStrength = signalStrength;
	}
}
