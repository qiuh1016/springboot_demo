package com.cetcme.springBootDemo.domain;

import java.io.Serializable;

public class RmiResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 执行结果 */
	private Boolean success;

	/** 返回码 */
	private int code;

	/** 返回信息 */
	private String msg;

	/** 查询结果 */
	private Object data;

	public Boolean getSuccess() {
		return success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		this.success = (code == 0);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
