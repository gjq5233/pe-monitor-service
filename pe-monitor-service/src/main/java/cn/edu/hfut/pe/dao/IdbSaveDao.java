package cn.edu.hfut.pe.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.edu.hfut.pe.model.entity.config.MON_DCU;


public interface IdbSaveDao {

	void saveDcuAddrList(Connection conn, List<MON_DCU> list) throws SQLException;

	void saveDeviceLastDataTimeList(Connection conn, Map<Long, Long> deviceLastTimeMap) throws SQLException;
}
