package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Fence;
import com.cetcme.springBootDemo.domain.FenceExtend;
import com.cetcme.springBootDemo.mapper.FenceMapper;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class FenceDao {

	Logger logger = LoggerFactory.getLogger(FenceDao.class);

	/**
	 * 根据港口ID取bean
	 */
	public Fence getByFenceId(Long FenceId) {
		Fence result = null;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			FenceMapper mapper = session.getMapper(FenceMapper.class);
			result = mapper.selectByPrimaryKey(FenceId);
			session.commit(true);
		} catch (Exception e) {} finally {
			session.close();
		}
		return result;
	}
	
	/**
	 * 获取所有港口信息
	 */
	public List<FenceExtend> getAll() {
		List<FenceExtend> result = new ArrayList<FenceExtend>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select t1.fence_id, t2.CIRCLE_ID POLYGON_ID, t1.fence_type, t2.longitude, t2.latitude, "
					+ " t2.radius from t_fence t1 inner join t_circle t2 on t1.fence_id = t2.fence_id "
					+ " where del_flag = 0 union select t1.fence_id, t3.POLYGON_ID, t1.fence_type, t3.longitude, t3.latitude, 0 "
					+ " from t_fence t1 inner join t_polygon t3 on t1.fence_id = t3.fence_id where del_flag = 0 "
					+ " order by fence_id,POLYGON_ID ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FenceExtend fenceExtend = new FenceExtend();
				fenceExtend.setFenceId(rs.getLong(1));
				fenceExtend.setFenceType(rs.getInt(3));
				fenceExtend.setLongitude(rs.getDouble(4));
				fenceExtend.setLatitude(rs.getDouble(5));
				fenceExtend.setRadius(rs.getDouble(6));
				result.add(fenceExtend);
			}
			rs.close();
			ps.close();
			logger.info("获取所有港口信息成功");
		} catch (SQLException e) {
			logger.error("SQL(获取所有港口信息)执行异常: {}", e.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
}
