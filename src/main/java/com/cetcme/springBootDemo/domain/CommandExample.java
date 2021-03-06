package com.cetcme.springBootDemo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_command
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_command
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_command
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public CommandExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_command
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCommandIdIsNull() {
            addCriterion("`COMMAND_ID` is null");
            return (Criteria) this;
        }

        public Criteria andCommandIdIsNotNull() {
            addCriterion("`COMMAND_ID` is not null");
            return (Criteria) this;
        }

        public Criteria andCommandIdEqualTo(Long value) {
            addCriterion("`COMMAND_ID` =", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdNotEqualTo(Long value) {
            addCriterion("`COMMAND_ID` <>", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdGreaterThan(Long value) {
            addCriterion("`COMMAND_ID` >", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`COMMAND_ID` >=", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdLessThan(Long value) {
            addCriterion("`COMMAND_ID` <", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdLessThanOrEqualTo(Long value) {
            addCriterion("`COMMAND_ID` <=", value, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdIn(List<Long> values) {
            addCriterion("`COMMAND_ID` in", values, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdNotIn(List<Long> values) {
            addCriterion("`COMMAND_ID` not in", values, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdBetween(Long value1, Long value2) {
            addCriterion("`COMMAND_ID` between", value1, value2, "commandId");
            return (Criteria) this;
        }

        public Criteria andCommandIdNotBetween(Long value1, Long value2) {
            addCriterion("`COMMAND_ID` not between", value1, value2, "commandId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNull() {
            addCriterion("`DEVICE_ID` is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("`DEVICE_ID` is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(Long value) {
            addCriterion("`DEVICE_ID` =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(Long value) {
            addCriterion("`DEVICE_ID` <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(Long value) {
            addCriterion("`DEVICE_ID` >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`DEVICE_ID` >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(Long value) {
            addCriterion("`DEVICE_ID` <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(Long value) {
            addCriterion("`DEVICE_ID` <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<Long> values) {
            addCriterion("`DEVICE_ID` in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<Long> values) {
            addCriterion("`DEVICE_ID` not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(Long value1, Long value2) {
            addCriterion("`DEVICE_ID` between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(Long value1, Long value2) {
            addCriterion("`DEVICE_ID` not between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIsNull() {
            addCriterion("`DEVICE_NO` is null");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIsNotNull() {
            addCriterion("`DEVICE_NO` is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceNoEqualTo(String value) {
            addCriterion("`DEVICE_NO` =", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotEqualTo(String value) {
            addCriterion("`DEVICE_NO` <>", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoGreaterThan(String value) {
            addCriterion("`DEVICE_NO` >", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoGreaterThanOrEqualTo(String value) {
            addCriterion("`DEVICE_NO` >=", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLessThan(String value) {
            addCriterion("`DEVICE_NO` <", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLessThanOrEqualTo(String value) {
            addCriterion("`DEVICE_NO` <=", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLike(String value) {
            addCriterion("`DEVICE_NO` like", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotLike(String value) {
            addCriterion("`DEVICE_NO` not like", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIn(List<String> values) {
            addCriterion("`DEVICE_NO` in", values, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotIn(List<String> values) {
            addCriterion("`DEVICE_NO` not in", values, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoBetween(String value1, String value2) {
            addCriterion("`DEVICE_NO` between", value1, value2, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotBetween(String value1, String value2) {
            addCriterion("`DEVICE_NO` not between", value1, value2, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andCommandTypeIsNull() {
            addCriterion("`COMMAND_TYPE` is null");
            return (Criteria) this;
        }

        public Criteria andCommandTypeIsNotNull() {
            addCriterion("`COMMAND_TYPE` is not null");
            return (Criteria) this;
        }

        public Criteria andCommandTypeEqualTo(Integer value) {
            addCriterion("`COMMAND_TYPE` =", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeNotEqualTo(Integer value) {
            addCriterion("`COMMAND_TYPE` <>", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeGreaterThan(Integer value) {
            addCriterion("`COMMAND_TYPE` >", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`COMMAND_TYPE` >=", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeLessThan(Integer value) {
            addCriterion("`COMMAND_TYPE` <", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeLessThanOrEqualTo(Integer value) {
            addCriterion("`COMMAND_TYPE` <=", value, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeIn(List<Integer> values) {
            addCriterion("`COMMAND_TYPE` in", values, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeNotIn(List<Integer> values) {
            addCriterion("`COMMAND_TYPE` not in", values, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeBetween(Integer value1, Integer value2) {
            addCriterion("`COMMAND_TYPE` between", value1, value2, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("`COMMAND_TYPE` not between", value1, value2, "commandType");
            return (Criteria) this;
        }

        public Criteria andCommandContentIsNull() {
            addCriterion("`COMMAND_CONTENT` is null");
            return (Criteria) this;
        }

        public Criteria andCommandContentIsNotNull() {
            addCriterion("`COMMAND_CONTENT` is not null");
            return (Criteria) this;
        }

        public Criteria andCommandContentEqualTo(String value) {
            addCriterion("`COMMAND_CONTENT` =", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentNotEqualTo(String value) {
            addCriterion("`COMMAND_CONTENT` <>", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentGreaterThan(String value) {
            addCriterion("`COMMAND_CONTENT` >", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentGreaterThanOrEqualTo(String value) {
            addCriterion("`COMMAND_CONTENT` >=", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentLessThan(String value) {
            addCriterion("`COMMAND_CONTENT` <", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentLessThanOrEqualTo(String value) {
            addCriterion("`COMMAND_CONTENT` <=", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentLike(String value) {
            addCriterion("`COMMAND_CONTENT` like", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentNotLike(String value) {
            addCriterion("`COMMAND_CONTENT` not like", value, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentIn(List<String> values) {
            addCriterion("`COMMAND_CONTENT` in", values, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentNotIn(List<String> values) {
            addCriterion("`COMMAND_CONTENT` not in", values, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentBetween(String value1, String value2) {
            addCriterion("`COMMAND_CONTENT` between", value1, value2, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandContentNotBetween(String value1, String value2) {
            addCriterion("`COMMAND_CONTENT` not between", value1, value2, "commandContent");
            return (Criteria) this;
        }

        public Criteria andCommandStatusIsNull() {
            addCriterion("`COMMAND_STATUS` is null");
            return (Criteria) this;
        }

        public Criteria andCommandStatusIsNotNull() {
            addCriterion("`COMMAND_STATUS` is not null");
            return (Criteria) this;
        }

        public Criteria andCommandStatusEqualTo(Integer value) {
            addCriterion("`COMMAND_STATUS` =", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusNotEqualTo(Integer value) {
            addCriterion("`COMMAND_STATUS` <>", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusGreaterThan(Integer value) {
            addCriterion("`COMMAND_STATUS` >", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`COMMAND_STATUS` >=", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusLessThan(Integer value) {
            addCriterion("`COMMAND_STATUS` <", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`COMMAND_STATUS` <=", value, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusIn(List<Integer> values) {
            addCriterion("`COMMAND_STATUS` in", values, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusNotIn(List<Integer> values) {
            addCriterion("`COMMAND_STATUS` not in", values, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusBetween(Integer value1, Integer value2) {
            addCriterion("`COMMAND_STATUS` between", value1, value2, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andCommandStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`COMMAND_STATUS` not between", value1, value2, "commandStatus");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeIsNull() {
            addCriterion("`SUCCEED_TIME` is null");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeIsNotNull() {
            addCriterion("`SUCCEED_TIME` is not null");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeEqualTo(Date value) {
            addCriterion("`SUCCEED_TIME` =", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeNotEqualTo(Date value) {
            addCriterion("`SUCCEED_TIME` <>", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeGreaterThan(Date value) {
            addCriterion("`SUCCEED_TIME` >", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`SUCCEED_TIME` >=", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeLessThan(Date value) {
            addCriterion("`SUCCEED_TIME` <", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeLessThanOrEqualTo(Date value) {
            addCriterion("`SUCCEED_TIME` <=", value, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeIn(List<Date> values) {
            addCriterion("`SUCCEED_TIME` in", values, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeNotIn(List<Date> values) {
            addCriterion("`SUCCEED_TIME` not in", values, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeBetween(Date value1, Date value2) {
            addCriterion("`SUCCEED_TIME` between", value1, value2, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andSucceedTimeNotBetween(Date value1, Date value2) {
            addCriterion("`SUCCEED_TIME` not between", value1, value2, "succeedTime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNull() {
            addCriterion("`CREATE_USER_ID` is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("`CREATE_USER_ID` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(Long value) {
            addCriterion("`CREATE_USER_ID` =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(Long value) {
            addCriterion("`CREATE_USER_ID` <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(Long value) {
            addCriterion("`CREATE_USER_ID` >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`CREATE_USER_ID` >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(Long value) {
            addCriterion("`CREATE_USER_ID` <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(Long value) {
            addCriterion("`CREATE_USER_ID` <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<Long> values) {
            addCriterion("`CREATE_USER_ID` in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<Long> values) {
            addCriterion("`CREATE_USER_ID` not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(Long value1, Long value2) {
            addCriterion("`CREATE_USER_ID` between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(Long value1, Long value2) {
            addCriterion("`CREATE_USER_ID` not between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("`CREATE_TIME` is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("`CREATE_TIME` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("`CREATE_TIME` =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("`CREATE_TIME` <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("`CREATE_TIME` >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`CREATE_TIME` >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("`CREATE_TIME` <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("`CREATE_TIME` <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("`CREATE_TIME` in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("`CREATE_TIME` not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("`CREATE_TIME` between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("`CREATE_TIME` not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIsNull() {
            addCriterion("`UPDATE_USER_ID` is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIsNotNull() {
            addCriterion("`UPDATE_USER_ID` is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdEqualTo(Long value) {
            addCriterion("`UPDATE_USER_ID` =", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotEqualTo(Long value) {
            addCriterion("`UPDATE_USER_ID` <>", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThan(Long value) {
            addCriterion("`UPDATE_USER_ID` >", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`UPDATE_USER_ID` >=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThan(Long value) {
            addCriterion("`UPDATE_USER_ID` <", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdLessThanOrEqualTo(Long value) {
            addCriterion("`UPDATE_USER_ID` <=", value, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdIn(List<Long> values) {
            addCriterion("`UPDATE_USER_ID` in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotIn(List<Long> values) {
            addCriterion("`UPDATE_USER_ID` not in", values, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdBetween(Long value1, Long value2) {
            addCriterion("`UPDATE_USER_ID` between", value1, value2, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIdNotBetween(Long value1, Long value2) {
            addCriterion("`UPDATE_USER_ID` not between", value1, value2, "updateUserId");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("`UPDATE_TIME` is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("`UPDATE_TIME` is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("`UPDATE_TIME` =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("`UPDATE_TIME` <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("`UPDATE_TIME` >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`UPDATE_TIME` >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("`UPDATE_TIME` <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("`UPDATE_TIME` <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("`UPDATE_TIME` in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("`UPDATE_TIME` not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("`UPDATE_TIME` between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("`UPDATE_TIME` not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNull() {
            addCriterion("`DEL_FLAG` is null");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNotNull() {
            addCriterion("`DEL_FLAG` is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlagEqualTo(Boolean value) {
            addCriterion("`DEL_FLAG` =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Boolean value) {
            addCriterion("`DEL_FLAG` <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Boolean value) {
            addCriterion("`DEL_FLAG` >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Boolean value) {
            addCriterion("`DEL_FLAG` >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Boolean value) {
            addCriterion("`DEL_FLAG` <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Boolean value) {
            addCriterion("`DEL_FLAG` <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Boolean> values) {
            addCriterion("`DEL_FLAG` in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Boolean> values) {
            addCriterion("`DEL_FLAG` not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Boolean value1, Boolean value2) {
            addCriterion("`DEL_FLAG` between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Boolean value1, Boolean value2) {
            addCriterion("`DEL_FLAG` not between", value1, value2, "delFlag");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_command
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_command
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}