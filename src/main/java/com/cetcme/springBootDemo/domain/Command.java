package com.cetcme.springBootDemo.domain;

import java.io.Serializable;
import java.util.Date;

public class Command implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.COMMAND_ID
     *
     * @mbggenerated
     */
    private Long commandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.DEVICE_ID
     *
     * @mbggenerated
     */
    private Long deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.DEVICE_NO
     *
     * @mbggenerated
     */
    private String deviceNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.COMMAND_TYPE
     *
     * @mbggenerated
     */
    private Integer commandType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.COMMAND_CONTENT
     *
     * @mbggenerated
     */
    private String commandContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.COMMAND_STATUS
     *
     * @mbggenerated
     */
    private Integer commandStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.SUCCEED_TIME
     *
     * @mbggenerated
     */
    private Date succeedTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.CREATE_USER_ID
     *
     * @mbggenerated
     */
    private Long createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    private Long updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_command.DEL_FLAG
     *
     * @mbggenerated
     */
    private Boolean delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_command
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.COMMAND_ID
     *
     * @return the value of t_command.COMMAND_ID
     *
     * @mbggenerated
     */
    public Long getCommandId() {
        return commandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.COMMAND_ID
     *
     * @param commandId the value for t_command.COMMAND_ID
     *
     * @mbggenerated
     */
    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.DEVICE_ID
     *
     * @return the value of t_command.DEVICE_ID
     *
     * @mbggenerated
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.DEVICE_ID
     *
     * @param deviceId the value for t_command.DEVICE_ID
     *
     * @mbggenerated
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.DEVICE_NO
     *
     * @return the value of t_command.DEVICE_NO
     *
     * @mbggenerated
     */
    public String getDeviceNo() {
        return deviceNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.DEVICE_NO
     *
     * @param deviceNo the value for t_command.DEVICE_NO
     *
     * @mbggenerated
     */
    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo == null ? null : deviceNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.COMMAND_TYPE
     *
     * @return the value of t_command.COMMAND_TYPE
     *
     * @mbggenerated
     */
    public Integer getCommandType() {
        return commandType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.COMMAND_TYPE
     *
     * @param commandType the value for t_command.COMMAND_TYPE
     *
     * @mbggenerated
     */
    public void setCommandType(Integer commandType) {
        this.commandType = commandType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.COMMAND_CONTENT
     *
     * @return the value of t_command.COMMAND_CONTENT
     *
     * @mbggenerated
     */
    public String getCommandContent() {
        return commandContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.COMMAND_CONTENT
     *
     * @param commandContent the value for t_command.COMMAND_CONTENT
     *
     * @mbggenerated
     */
    public void setCommandContent(String commandContent) {
        this.commandContent = commandContent == null ? null : commandContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.COMMAND_STATUS
     *
     * @return the value of t_command.COMMAND_STATUS
     *
     * @mbggenerated
     */
    public Integer getCommandStatus() {
        return commandStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.COMMAND_STATUS
     *
     * @param commandStatus the value for t_command.COMMAND_STATUS
     *
     * @mbggenerated
     */
    public void setCommandStatus(Integer commandStatus) {
        this.commandStatus = commandStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.SUCCEED_TIME
     *
     * @return the value of t_command.SUCCEED_TIME
     *
     * @mbggenerated
     */
    public Date getSucceedTime() {
        return succeedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.SUCCEED_TIME
     *
     * @param succeedTime the value for t_command.SUCCEED_TIME
     *
     * @mbggenerated
     */
    public void setSucceedTime(Date succeedTime) {
        this.succeedTime = succeedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.CREATE_USER_ID
     *
     * @return the value of t_command.CREATE_USER_ID
     *
     * @mbggenerated
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.CREATE_USER_ID
     *
     * @param createUserId the value for t_command.CREATE_USER_ID
     *
     * @mbggenerated
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.CREATE_TIME
     *
     * @return the value of t_command.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.CREATE_TIME
     *
     * @param createTime the value for t_command.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.UPDATE_USER_ID
     *
     * @return the value of t_command.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.UPDATE_USER_ID
     *
     * @param updateUserId the value for t_command.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.UPDATE_TIME
     *
     * @return the value of t_command.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.UPDATE_TIME
     *
     * @param updateTime the value for t_command.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_command.DEL_FLAG
     *
     * @return the value of t_command.DEL_FLAG
     *
     * @mbggenerated
     */
    public Boolean getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_command.DEL_FLAG
     *
     * @param delFlag the value for t_command.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", commandId=").append(commandId);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", commandType=").append(commandType);
        sb.append(", commandContent=").append(commandContent);
        sb.append(", commandStatus=").append(commandStatus);
        sb.append(", succeedTime=").append(succeedTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUserId=").append(updateUserId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}