package com.cetcme.springBootDemo.domain;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CircleFence extends Fence {

	private static final long serialVersionUID = 1L;

	private Double longitude;

	private Double latitude;

	private Double radius;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
