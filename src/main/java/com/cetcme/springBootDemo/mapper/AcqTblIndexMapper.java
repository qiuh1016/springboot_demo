package com.cetcme.springBootDemo.mapper;

import com.cetcme.springBootDemo.domain.AcqTblIndex;

public interface AcqTblIndexMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long indexId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    int insert(AcqTblIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    int insertSelective(AcqTblIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    AcqTblIndex selectByPrimaryKey(Long indexId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(AcqTblIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_acqtbl_index
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(AcqTblIndex record);
}