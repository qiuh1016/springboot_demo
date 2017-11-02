package com.cetcme.springBootDemo.domain;


public class FenceExtend extends Fence {

	private static final long serialVersionUID = 1L;

	private Double longitude;

	private Double latitude;

	private Double radius;

	private Long shipAmount;

	private Long inShipAmount;

	private Long outShipAmount;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Long getShipAmount() {
		return shipAmount;
	}

	public void setShipAmount(Long shipAmount) {
		this.shipAmount = shipAmount;
	}

	public Long getInShipAmount() {
		return inShipAmount;
	}

	public void setInShipAmount(Long inShipAmount) {
		this.inShipAmount = inShipAmount;
	}

	public Long getOutShipAmount() {
		return outShipAmount;
	}

	public void setOutShipAmount(Long outShipAmount) {
		this.outShipAmount = outShipAmount;
	}
}
