package cn.edu.hfut.pe.dao.impl;

import base.util.CloseUtil;
import cn.edu.hfut.pe.dao.IdbSaveDao;
import cn.edu.hfut.pe.dao.IpeDao;
import cn.edu.hfut.pe.model.ModbusValueExps;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PeDaoImpl implements IpeDao, IdbSaveDao {

	private final static Logger LOG = LoggerFactory.getLogger(PeDaoImpl.class);

	/**
	 * 查询超时时间
	 */
	private final int sqlQueryTime = 15;

	
	
//	@Override
//	public void mergePe2DataLast(Connection conn, List<MON_PE2_VALUE> list) throws SQLException {
//		LOG.debug("mergePe2DataLast");
//		StringBuilder sb = new StringBuilder();
//		sb.append(
//				"insert into MON_PE2_DATA_LAST (MOID,MONITOR_ID,GRAPH_ID ,COLL_TIME ,COLL_VALUE, TAGS, ALARM_LEVEL,UPDATE_TIME )");
//		sb.append("VALUES (?,?,?,?,?, '',?,?)");
//		sb.append(" ON DUPLICATE KEY UPDATE");
//		sb.append(" coll_time = values(coll_time),");
//		sb.append(" coll_value = values(coll_value),");
//		sb.append(" ALARM_LEVEL = values(ALARM_LEVEL),");
//		sb.append(" UPDATE_TIME = values(UPDATE_TIME)");
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_VALUE data : list) {
//				int i = 1;
//				Long moid = data.getMoid();
//				ps.setLong(i++, moid);
//				MON_PE_MANAGEDOBJECT_TMP tmp = PE2Conf.MAP_MOID_MO.get(moid);
//				long monitorid = -1;
//				if (tmp != null) {
//					monitorid = tmp.getMonitorId();
//				}
//				ps.setLong(i++, monitorid);
//
//				MON_PE2_TEMPLATE_GRAPH graph = data.getGraph();
//
//				ps.setLong(i++, graph.getGRAPH_ID());
//				ps.setTimestamp(i++, new Timestamp(data.getTime()));
//				ps.setDouble(i++, ((Number) data.getValue()).doubleValue());
//				Integer alarmLevel = data.getAlarmLevel();
//				if (alarmLevel == null) {
//					alarmLevel = 1;
//				}
//				ps.setInt(i++, alarmLevel);
//				ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
	
//	@Override
//	public void updateAlarmList(Connection conn, List<MON_PE2_ALARM> list) throws SQLException {
//		LOG.info("updateAlarmList:{}", list);
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE MON_PE2_ALARM SET  UPDATE_TIME=CURRENT_TIMESTAMP, ALARM_END_TIME=? WHERE ALARM_ID=?");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_ALARM alarm : list) {
//				int i = 1;
//				ps.setTimestamp(i++, new Timestamp(alarm.getEndTime()));
//				ps.setLong(i++, alarm.getAlarmId());
//
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}

	@Override
	public List<MON_DEVICE> getMON_DEVICElist(Connection conn) throws SQLException {
		LOG.debug("getMON_DEVICElist");
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DEVICE_ID, DCU_ID, DEVICE_UNIT_IDENTFIER, DEVICE_NAME, TEMPLATE_ID ");
		sb.append(" FROM mon_device  WHERE IS_ENABLE = 1 ");
		LOG.debug(sb.toString());
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MON_DEVICE> list = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setQueryTimeout(sqlQueryTime);
			rs = ps.executeQuery();
			list = new ArrayList<MON_DEVICE>();
			while (rs.next()) {
				int i = 1;
				MON_DEVICE p = new MON_DEVICE(rs.getLong(i++), rs.getLong(i++), rs.getInt(i++), rs.getString(i++),
						rs.getLong(i++));
				list.add(p);
			}
		} finally {
			CloseUtil.closeRs(rs);
			CloseUtil.closeSt(ps);
		}
		return list;
	}

	@Override
	public List<MON_DCU> getMON_DCUlist(Connection conn) throws SQLException {
		LOG.debug("getMON_DCUlist");
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DCU_ID, DCU_IMEI, DCU_NAME, DCU_ADDR, DCU_LAC, DCU_CI ");
		sb.append(" FROM mon_dcu where IS_ENABLE = 1 ");
		LOG.debug(sb.toString());
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MON_DCU> list = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setQueryTimeout(sqlQueryTime);
			rs = ps.executeQuery();
			list = new ArrayList<MON_DCU>();
			while (rs.next()) {
				int i = 1;
				MON_DCU p = new MON_DCU(rs.getLong(i++), rs.getLong(i++), rs.getString(i++), rs.getString(i++),
						rs.getLong(i++), rs.getLong(i++));
				list.add(p);
			}
		} finally {
			CloseUtil.closeRs(rs);
			CloseUtil.closeSt(ps);
		}
		return list;
	}

	@Override
	public List<MON_METRIC_CONFIG> getMON_METRIC_CONFIGlist(Connection conn) throws SQLException {
		LOG.debug("getMON_METRIC_CONFIGlist");
		StringBuilder sb = new StringBuilder();
		sb.append(
				" SELECT METRIC_CONFIG_ID, TEMPLATE_ID, METRIC_CONFIG_NAME, METRIC_NAME, METRIC_BUSI_TYPE, METRIC_VALUE_TYPE,  ");
		sb.append(" METRIC_VALUE_UNIT, DEFAULT_V_NO_DATA, VALUE_EXPS, MULTIPLIER, PARENT_GRAPH_ID, COLLECTION_CYCLE ");
		sb.append(" FROM mon_metric_config WHERE  IS_ENABLE = 1 ");
		LOG.debug(sb.toString());
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MON_METRIC_CONFIG> list = null;
		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setQueryTimeout(sqlQueryTime);
			rs = ps.executeQuery();
			list = new ArrayList<MON_METRIC_CONFIG>();
			while (rs.next()) {
				int i = 1;
				MON_METRIC_CONFIG p = new MON_METRIC_CONFIG(rs.getLong(i++), rs.getLong(i++), rs.getString(i++),
						rs.getString(i++), rs.getInt(i++), rs.getInt(i++), rs.getString(i++), rs.getString(i++),
						rs.getString(i++), rs.getDouble(i++), rs.getLong(i++), rs.getInt(i++));
				ModbusValueExps mve =	ModbusValueExps.getInstance(p.getVALUE_EXPS());
				if(mve == null) {
					LOG.warn("error graph:{}", p);
					continue;
				}
				p.setMve(ModbusValueExps.getInstance(p.getVALUE_EXPS()));
				list.add(p);
			}
		} finally {
			CloseUtil.closeRs(rs);
			CloseUtil.closeSt(ps);
		}
		return list;
	}

	@Override
	public void saveDcuAddrList(Connection conn, List<MON_DCU> list) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDeviceLastDataTimeList(Connection conn, Map<Long, Long> deviceLastTimeMap) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
