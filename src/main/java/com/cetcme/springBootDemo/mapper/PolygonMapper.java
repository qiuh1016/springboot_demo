package com.cetcme.springBootDemo.mapper;

import com.cetcme.springBootDemo.domain.Polygon;

public interface PolygonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long polygonId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    int insert(Polygon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    int insertSelective(Polygon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    Polygon selectByPrimaryKey(Long polygonId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Polygon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_polygon
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Polygon record);
}