package com.cetcme.springBootDemo.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PolygonFence extends Fence {

	private static final long serialVersionUID = 1L;

	private List<GpsPosition> vertices;

	public List<GpsPosition> getVertices() {
		return vertices;
	}

	public void setVertices(List<GpsPosition> vertices) {
		this.vertices = vertices;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}