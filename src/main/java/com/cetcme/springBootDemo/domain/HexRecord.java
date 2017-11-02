package com.cetcme.springBootDemo.domain;

import org.apache.commons.lang3.StringUtils;

import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.Constants.HexRecordType;

public class HexRecord {
	private static final int BYTE_COUNT_BEGIN = 1;
	private static final int BYTE_COUNT_END = 3;

	private static final int ADDRESS_BEGIN = 3;
	private static final int ADDRESS_END = 7;

	private static final int RECORD_TYPE_BEGIN = 7;
	private static final int RECORD_TYPE_END = 9;

	private static final int DATA_BEGIN = 9;
	private static final int DATA_END = -2;

	public HexRecord(String rawData) {

		String byteCountStr = StringUtils.substring(rawData, BYTE_COUNT_BEGIN, BYTE_COUNT_END);
		this.byteCount = Integer.parseInt(byteCountStr, Constants.HEX);
		String addressStr = StringUtils.substring(rawData, ADDRESS_BEGIN, ADDRESS_END);
		this.address = Integer.parseInt(addressStr, Constants.HEX);
		String recordTypeStr = StringUtils.substring(rawData, RECORD_TYPE_BEGIN, RECORD_TYPE_END);
		this.recordType = HexRecordType.getEnumByValue(recordTypeStr);
		this.data = StringUtils.substring(rawData, DATA_BEGIN, DATA_END);
	}

	/** 地址域, 它代表记录当中数据的起始地址 */
	private int address;

	/** 数据长度域，它代表记录当中数据字节的数量 */
	private int byteCount;

	/** 代表HEX记录类型的域 */
	private HexRecordType recordType;

	private String data;

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public HexRecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(HexRecordType recordType) {
		this.recordType = recordType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}