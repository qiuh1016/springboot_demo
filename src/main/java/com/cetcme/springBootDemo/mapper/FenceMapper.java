package com.cetcme.springBootDemo.mapper;

import com.cetcme.springBootDemo.domain.Fence;

public interface FenceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long fenceId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    int insert(Fence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    int insertSelective(Fence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    Fence selectByPrimaryKey(Long fenceId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Fence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fence
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Fence record);
}