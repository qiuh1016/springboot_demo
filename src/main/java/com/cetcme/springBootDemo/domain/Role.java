package com.cetcme.springBootDemo.domain;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.ROLE_ID
     *
     * @mbggenerated
     */
    private Long roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.ROLE_NAME
     *
     * @mbggenerated
     */
    private String roleName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.CREATE_USER_ID
     *
     * @mbggenerated
     */
    private Long createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    private Long updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.DEL_FLAG
     *
     * @mbggenerated
     */
    private Boolean delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_role
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.ROLE_ID
     *
     * @return the value of t_role.ROLE_ID
     *
     * @mbggenerated
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.ROLE_ID
     *
     * @param roleId the value for t_role.ROLE_ID
     *
     * @mbggenerated
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.ROLE_NAME
     *
     * @return the value of t_role.ROLE_NAME
     *
     * @mbggenerated
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.ROLE_NAME
     *
     * @param roleName the value for t_role.ROLE_NAME
     *
     * @mbggenerated
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.CREATE_USER_ID
     *
     * @return the value of t_role.CREATE_USER_ID
     *
     * @mbggenerated
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.CREATE_USER_ID
     *
     * @param createUserId the value for t_role.CREATE_USER_ID
     *
     * @mbggenerated
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.CREATE_TIME
     *
     * @return the value of t_role.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.CREATE_TIME
     *
     * @param createTime the value for t_role.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.UPDATE_USER_ID
     *
     * @return the value of t_role.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.UPDATE_USER_ID
     *
     * @param updateUserId the value for t_role.UPDATE_USER_ID
     *
     * @mbggenerated
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.UPDATE_TIME
     *
     * @return the value of t_role.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.UPDATE_TIME
     *
     * @param updateTime the value for t_role.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.DEL_FLAG
     *
     * @return the value of t_role.DEL_FLAG
     *
     * @mbggenerated
     */
    public Boolean getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.DEL_FLAG
     *
     * @param delFlag the value for t_role.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
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