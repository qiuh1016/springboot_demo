package com.cetcme.springBootDemo.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cetcme.springBootDemo.domain.HexRecord;
import com.cetcme.springBootDemo.utils.Constants.HexRecordType;

public class BootloaderDataParser {

	/** HEX文件每行的数据长度 */
	private static final int HEX_PER_LINE_DATA_LENGTH = 16;

	/** 每个存储单元的所能存储的最大字节数(5BLOCKS*8WORDS*2) */
	private static final int STORAGE_UNIT_BYTE_COUNT = 80;

	private static final int MAX_HEX_STR_LENGTH = STORAGE_UNIT_BYTE_COUNT * 2;

	/** 每个 Intel HEX 记录都由冒号开头 */
	private static final String HEX_START_CODE = ":";

	private static BootloaderDataParser instance = null;

	private List<byte[]> writeFlashData = new ArrayList<byte[]>();;

	private BootloaderDataParser() {
		Properties proper = PropertiesUtil.getResources(Constants.CONFIG_FILE_NAME);
		String filePath = proper.getProperty(Constants.BOOTLOADER_FILEPATH_KEY);
		this.setWriteFlashData(loadHexFile(filePath));
	}

	public List<byte[]> getWriteFlashData() {
		return writeFlashData;
	}

	public void setWriteFlashData(List<byte[]> writeFlashData) {
		this.writeFlashData = writeFlashData;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new BootloaderDataParser();
		}
	}

	public static BootloaderDataParser getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private List<byte[]> loadHexFile(String fileName) {
		List<byte[]> result = new ArrayList<byte[]>();
		BufferedReader reader = null;
		// 记录数据的起始地址(单位：word)
		int address = 0;

		// 当前行记录
		HexRecord prevRecord = null;

		// 前一行记录
		HexRecord curRecord = null;

		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(fileName));

			String readLine = "";
			while ((readLine = reader.readLine()) != null) {

				// 判断当前行是否是有效HEX数据
				if (isInvalidHexRecord(readLine)) {
					continue;
				}

				curRecord = new HexRecord(readLine);

				// 如果是文件结束记录,则结束读取文件
				if (curRecord.getRecordType() == HexRecordType.EOF) {
					break;
				}

				// 如果是第二次读到扩展线性地址记录,则处理剩余的数据并结束读取文件
				if (prevRecord != null && curRecord.getRecordType() == HexRecordType.EXT_LIN) {
					if (sb.length() > 0) {
						byte[] bytes = setUpWriteFlashMsg(sb.substring(0), address);
						result.add(bytes);
						sb.delete(0, sb.length());
					}
					break;
				}

				// 如果是数据记录,则构建写Flash消息数据
				if (curRecord.getRecordType() == HexRecordType.DATA) {

					// 如果当前行记录与前一行记录不是相邻地址，则更新地址域
					if (prevRecord != null
							&& curRecord.getAddress() - prevRecord.getAddress() > HEX_PER_LINE_DATA_LENGTH) {

						// 处理剩余的数据
						if (sb.length() > 0) {
							byte[] bytes = setUpWriteFlashMsg(sb.substring(0), address);
							result.add(bytes);
							sb.delete(0, sb.length());
						}

						// Hex文件中的地址是以byte为单位的,但flash中的地址是以word为单位的,
						// 1word = 2byte,以下同理
						address = curRecord.getAddress() / 2;
					}

					sb.append(curRecord.getData());
					while (sb.length() > MAX_HEX_STR_LENGTH) {
						byte[] bytes = setUpWriteFlashMsg(sb.substring(0, MAX_HEX_STR_LENGTH), address);
						address += STORAGE_UNIT_BYTE_COUNT / 2;
						result.add(bytes);
						sb.delete(0, MAX_HEX_STR_LENGTH);
					}  

					// 将当前行记录赋给前一行记录
					prevRecord = new HexRecord(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * 如果是空行或者不以":"开头的数据或者数据的长度为偶数,则认为是无效的HEX数据
	 */
	public boolean isInvalidHexRecord(String record) {
		return StringUtils.isBlank(StringUtils.trim(record)) || !StringUtils.startsWith(record, HEX_START_CODE)
				|| (record.length() % 2 == 0);
	}

	private byte[] setUpWriteFlashMsg(String data, int address) {
		if (StringUtils.isBlank(data)) {
			return null;
		}

		byte[] result = new byte[82];
		int index = 0;

		// 地址高位
		result[index++] = getHighByte(address);
		// 地址低位
		result[index++] = getLowByte(address);

		// 位数不满，则补0
		if (data.length() < STORAGE_UNIT_BYTE_COUNT * 2) {
			data = StringUtils.rightPad(data, STORAGE_UNIT_BYTE_COUNT * 2, "0");
		}

		for (int i = 0; i < data.length(); i += 2) {
			String hexStr = StringUtils.substring(data, i, i + 2);
			byte byteValue = (byte) (Integer.parseInt(hexStr, Constants.HEX));
			result[index++] = byteValue;
		}
		return result;
	}

	private byte getLowByte(int value) {
		return (byte) (value & 0xff);
	}

	private byte getHighByte(int value) {
		return (byte) (value >> 8 & 0xff);
	}
}
