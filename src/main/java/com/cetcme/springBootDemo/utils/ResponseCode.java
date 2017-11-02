package com.cetcme.springBootDemo.utils;

public class ResponseCode {

	/** 成功 */
	public static final int OK = 0;

	/** 参数错误 */
	public static final int PARAM_INCORRECT = -10001;

	/** 没有访问权限 */
	public static final int NO_ACCESS_PERMISSION = -10002;

	/** 操作失败 */
	public static final int FAILURE = -10003;

	/** 设备通信故障 */
	public static final int DEVICE_COMM_FAULT = -10004;

	/** 系统异常 */
	public static final int SYSTEM_ERROR = -99999;
}
