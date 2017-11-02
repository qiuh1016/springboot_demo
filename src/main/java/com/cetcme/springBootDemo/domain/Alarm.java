package com.cetcme.springBootDemo.domain;

import java.io.Serializable;
import java.util.Date;

public class Alarm implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.ALARM_ID
     *
     * @mbggenerated
     */
    private Long alarmId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.ALARM_NO
     *
     * @mbggenerated
     */
    private String alarmNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.DEVICE_ID
     *
     * @mbggenerated
     */
    private Long deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.DEVICE_NO
     *
     * @mbggenerated
     */
    private String deviceNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.SHIP_ID
     *
     * @mbggenerated
     */
    private Long shipId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.ALARM_TYPE
     *
     * @mbggenerated
     */
    private Integer alarmType;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.ALARM_MARK
     *
     * @mbggenerated
     */
    private String alarmMark;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.ALARM_USER_ID
     *
     * @mbggenerated
     */
    private Integer alarmUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.REPORT_TIME
     *
     * @mbggenerated
     */
    private Date reportTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.LONGITUDE
     *
     * @mbggenerated
     */
    private Double longitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.LATITUDE
     *
     * @mbggenerated
     */
    private Double latitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.CONFIRM_FLAG
     *
     * @mbggenerated
     */
    private Boolean confirmFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.CONFIRM_USER_ID
     *
     * @mbggenerated
     */
    private Long confirmUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.CONFIRM_TIME
     *
     * @mbggenerated
     */
    private Date confirmTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.SOLVE_FLAG
     *
     * @mbggenerated
     */
    private Boolean solveFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.SOLVE_TIME
     *
     * @mbggenerated
     */
    private Date solveTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_alarm.DEL_FLAG
     *
     * @mbggenerated
     */
    private Boolean delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_alarm
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.ALARM_ID
     *
     * @return the value of t_alarm.ALARM_ID
     *
     * @mbggenerated
     */
    public Long getAlarmId() {
        return alarmId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.ALARM_ID
     *
     * @param alarmId the value for t_alarm.ALARM_ID
     *
     * @mbggenerated
     */
    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.ALARM_NO
     *
     * @return the value of t_alarm.ALARM_NO
     *
     * @mbggenerated
     */
    public String getAlarmNo() {
        return alarmNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.ALARM_NO
     *
     * @param alarmNo the value for t_alarm.ALARM_NO
     *
     * @mbggenerated
     */
    public void setAlarmNo(String alarmNo) {
        this.alarmNo = alarmNo == null ? null : alarmNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.DEVICE_ID
     *
     * @return the value of t_alarm.DEVICE_ID
     *
     * @mbggenerated
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.DEVICE_ID
     *
     * @param deviceId the value for t_alarm.DEVICE_ID
     *
     * @mbggenerated
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.DEVICE_NO
     *
     * @return the value of t_alarm.DEVICE_NO
     *
     * @mbggenerated
     */
    public String getDeviceNo() {
        return deviceNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.DEVICE_NO
     *
     * @param deviceNo the value for t_alarm.DEVICE_NO
     *
     * @mbggenerated
     */
    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo == null ? null : deviceNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.SHIP_ID
     *
     * @return the value of t_alarm.SHIP_ID
     *
     * @mbggenerated
     */
    public Long getShipId() {
        return shipId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.SHIP_ID
     *
     * @param shipId the value for t_alarm.SHIP_ID
     *
     * @mbggenerated
     */
    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.ALARM_TYPE
     *
     * @return the value of t_alarm.ALARM_TYPE
     *
     * @mbggenerated
     */
    public Integer getAlarmType() {
        return alarmType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.ALARM_TYPE
     *
     * @param alarmType the value for t_alarm.ALARM_TYPE
     *
     * @mbggenerated
     */
    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.ALARM_MARK
     *
     * @param alarmType the value for t_alarm.ALARM_MARK
     *
     * @mbggenerated
     */
    public void setAlarmMark(String alarmMark) {
        this.alarmMark = alarmMark;
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.ALARM_MARK
     *
     * @return the value of t_alarm.ALARM_MARK
     *
     * @mbggenerated
     */
    public String getAlarmMark() {
        return alarmMark;
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.ALARM_MARK
     *
     * @return the value of t_alarm.ALARM_MARK
     *
     * @mbggenerated
     */
    public Integer getAlarmUserId() {
        return alarmUserId;
    }
    
    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.ALARM_MARK
     *
     * @param alarmType the value for t_alarm.ALARM_MARK
     *
     * @mbggenerated
     */
    public void setAlarmUserId(Integer alarmUserId) {
        this.alarmUserId = alarmUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.REPORT_TIME
     *
     * @return the value of t_alarm.REPORT_TIME
     *
     * @mbggenerated
     */
    public Date getReportTime() {
        return reportTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.REPORT_TIME
     *
     * @param reportTime the value for t_alarm.REPORT_TIME
     *
     * @mbggenerated
     */
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.LONGITUDE
     *
     * @return the value of t_alarm.LONGITUDE
     *
     * @mbggenerated
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.LONGITUDE
     *
     * @param longitude the value for t_alarm.LONGITUDE
     *
     * @mbggenerated
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.LATITUDE
     *
     * @return the value of t_alarm.LATITUDE
     *
     * @mbggenerated
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.LATITUDE
     *
     * @param latitude the value for t_alarm.LATITUDE
     *
     * @mbggenerated
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.CONFIRM_FLAG
     *
     * @return the value of t_alarm.CONFIRM_FLAG
     *
     * @mbggenerated
     */
    public Boolean getConfirmFlag() {
        return confirmFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.CONFIRM_FLAG
     *
     * @param confirmFlag the value for t_alarm.CONFIRM_FLAG
     *
     * @mbggenerated
     */
    public void setConfirmFlag(Boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.CONFIRM_USER_ID
     *
     * @return the value of t_alarm.CONFIRM_USER_ID
     *
     * @mbggenerated
     */
    public Long getConfirmUserId() {
        return confirmUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.CONFIRM_USER_ID
     *
     * @param confirmUserId the value for t_alarm.CONFIRM_USER_ID
     *
     * @mbggenerated
     */
    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.CONFIRM_TIME
     *
     * @return the value of t_alarm.CONFIRM_TIME
     *
     * @mbggenerated
     */
    public Date getConfirmTime() {
        return confirmTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.CONFIRM_TIME
     *
     * @param confirmTime the value for t_alarm.CONFIRM_TIME
     *
     * @mbggenerated
     */
    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.SOLVE_FLAG
     *
     * @return the value of t_alarm.SOLVE_FLAG
     *
     * @mbggenerated
     */
    public Boolean getSolveFlag() {
        return solveFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.SOLVE_FLAG
     *
     * @param solveFlag the value for t_alarm.SOLVE_FLAG
     *
     * @mbggenerated
     */
    public void setSolveFlag(Boolean solveFlag) {
        this.solveFlag = solveFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.SOLVE_TIME
     *
     * @return the value of t_alarm.SOLVE_TIME
     *
     * @mbggenerated
     */
    public Date getSolveTime() {
        return solveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.SOLVE_TIME
     *
     * @param solveTime the value for t_alarm.SOLVE_TIME
     *
     * @mbggenerated
     */
    public void setSolveTime(Date solveTime) {
        this.solveTime = solveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.CREATE_TIME
     *
     * @return the value of t_alarm.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.CREATE_TIME
     *
     * @param createTime the value for t_alarm.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.UPDATE_TIME
     *
     * @return the value of t_alarm.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.UPDATE_TIME
     *
     * @param updateTime the value for t_alarm.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_alarm.DEL_FLAG
     *
     * @return the value of t_alarm.DEL_FLAG
     *
     * @mbggenerated
     */
    public Boolean getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_alarm.DEL_FLAG
     *
     * @param delFlag the value for t_alarm.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_alarm
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", alarmId=").append(alarmId);
        sb.append(", alarmNo=").append(alarmNo);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", shipId=").append(shipId);
        sb.append(", alarmType=").append(alarmType);
        sb.append(", reportTime=").append(reportTime);
        sb.append(", longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append(", confirmFlag=").append(confirmFlag);
        sb.append(", confirmUserId=").append(confirmUserId);
        sb.append(", confirmTime=").append(confirmTime);
        sb.append(", solveFlag=").append(solveFlag);
        sb.append(", solveTime=").append(solveTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}