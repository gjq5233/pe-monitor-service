//package cn.edu.hfut.pe.dao.impl;
//
//import base.util.CloseUtil;
//import cn.edu.hfut.pe.dao.IpeBDao;
//import cn.edu.hfut.pe.pe2.B.dto.*;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE2_TEMPLATE_GRAPH;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE_MANAGEDOBJECT_TMP;
//import cn.edu.hfut.pe.util.BusiIdMixUtil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PeBDaoImpl implements IpeBDao {
//
//	private final static Logger LOG = LoggerFactory.getLogger(PeBDaoImpl.class);
//
//	/**
//	 * 查询超时时间
//	 */
//	private final int sqlQueryTime = 15;
//
//	@Override
//	public List<MON_PE2_B_MO> getMON_PE2_B_MANAGEDOBJECTlist(Connection conn) throws SQLException {
//		LOG.debug("getMON_PE2_B_MANAGEDOBJECTlist");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT MOID,MONITOR_ID, FSUID, DEVICE_ID, DEVICE_NAME,");
//		sb.append("	SITE_ID, ROOM_ID, SITE_NAME, ROOM_NAME,");
//		sb.append("	DEVICE_TYPE, DEVICE_SUBTYPE, MODEL, ");
//		sb.append("	BRAND, RATED_CAPACITY, VERSION, ");
//		sb.append("	BEGIN_RUNTIME, DEV_DESCRIBE, CONF_REMARK,IP_ADDRESS");
//		sb.append("	from MON_PE2_B_MANAGEDOBJECT ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_PE2_B_MO> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_PE2_B_MO p = new MON_PE2_B_MO(rs.getLong(i++),rs.getLong(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getInt(i++),rs.getInt(i++),rs.getString(i++),rs.getString(i++),rs.getFloat(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++));
//				list.add(p);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//	@Override
//	public List<MON_PE2_TEMPLATE_GRAPH> getMON_PE2_TEMPLATE_GRAPHlist(Connection conn) throws SQLException {
//		LOG.debug("getMON_PE2_MON_PE2_TEMPLATE_GRAPHlist");
//		StringBuilder sb = new StringBuilder();
//		sb.append(" SELECT GRAPH_ID, TEMPLATE_ID, METRIC_NAME, METRIC_BUSI_TYPE,  ");
//		sb.append(" 	METRIC_VALUE_TYPE, DEFAULT_V_NO_DATA, VALUE_EXPS, MULTIPLIER, ");
//		sb.append(" 	THRESHOLD_WARN_LEFT, THRESHOLD_WARN_RIGHT, THRESHOLD_ERROR_LEFT, ");
//		sb.append(" 	THRESHOLD_ERROR_RIGHT, PARENT_GRAPH_ID, COLLECTION_CYCLE ");
//		sb.append(" FROM MON_PE2_TEMPLATE_GRAPH");
//		sb.append(" WHERE IS_ENABLE = 1 ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_PE2_TEMPLATE_GRAPH> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_PE2_TEMPLATE_GRAPH p = new MON_PE2_TEMPLATE_GRAPH(rs.getLong(i++), rs.getLong(i++),
//						rs.getString(i++), rs.getInt(i++), rs.getInt(i++), rs.getString(i++), rs.getString(i++),
//						rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++), rs.getDouble(i++),
//						rs.getLong(i++), rs.getInt(i++));
//				list.add(p);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//
//	@Override
//	public void insertPeBDataList(Connection conn, List<MON_PE2_B_METRIC_DATA> dataList) throws SQLException {
//		LOG.debug("insertPeBDataList");
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(
//					"insert into MON_PE2_DATA_CUR (moid,GRAPH_ID ,COLL_TIME ,COLL_VALUE,TAGS ) values(?, ?, ?, ?,?)");
//
//			for (MON_PE2_B_METRIC_DATA data : dataList) {
//				int i = 1;
//				ps.setLong(i++,data.getMoid());
//				ps.setLong(i++,data.getGraph_id());
//				ps.setTimestamp(i++,Timestamp.valueOf(data.getTime()));
//				ps.setDouble(i++,data.getMeasuredVal());
//				ps.setString(i++,data.getSignalNumber());
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	public void mergePeBAlarmList(Connection conn, List<MON_PE2_B_ALARM> list,boolean isStart) throws SQLException {
//		LOG.debug("insertPeBAlarmList");
//		StringBuilder sb = new StringBuilder();
//		sb.append(" insert into MON_PE2_B_ALARM(ALARM_ID,GRAPH_ID,FSUID,MOID,MONITOR_ID,SERIAL_NO,ID,DEVICEID,NMALARM_ID,ALARM_TIME,ALARM_LEVEL,ALARM_DESC,EVENT_VALUE,SIGNAL_NUMBER,ALARM_REMARK, BORGID)");
//		sb.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		sb.append(" ON DUPLICATE KEY UPDATE ");
//		if(isStart){
//			sb.append(" ALARM_TIME = ?");
//		}else {
//			sb.append(" ALARM_END_TIME = ?");
//		}
//
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//
//			for(MON_PE2_B_ALARM alarm:list){
//				int i = 1;
//				ps.setLong(i++,alarm.getAlarmId());
//				ps.setLong(i++,alarm.getGraphId());
//				ps.setString(i++,alarm.getFsuid());
//				ps.setLong(i++,alarm.getMoid());
//				ps.setLong(i++,alarm.getMonitorId());
//				ps.setString(i++,alarm.getSerialNo());
//				ps.setString(i++,alarm.getId());
//				ps.setString(i++,alarm.getDeviceID());
//				ps.setString(i++,alarm.getNmAlarmID());
//				ps.setTimestamp(i++,Timestamp.valueOf(alarm.getAlarmTime()));
//				ps.setInt(i++,alarm.getAlarmLevel());
//				ps.setString(i++,alarm.getAlarmDesc());
//				ps.setDouble(i++,alarm.getEventValue());
//				ps.setString(i++,alarm.getSignalNumber());
//				ps.setString(i++,alarm.getAlarmRemark());
//				
//				ps.setInt(i++, BusiIdMixUtil.getOrgid(alarm.getAlarmId()));
//				
//				ps.setTimestamp(i++,Timestamp.valueOf(alarm.getAlarmTime()));
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	/***
//	 * @Author zero
//	 * @Description merge最新数据，索引是moid、graph_id、tags
//	 * @InitDate 11:42 2021/1/20
//	 * @Param [conn, list]
//	 **/
//	public void mergePeBDataLast(Connection conn, List<MON_PE2_B_METRIC_DATA> list) throws SQLException {
//		LOG.debug("mergePeBDataLast");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_PE2_DATA_LAST (MOID,MONITOR_ID,GRAPH_ID ,COLL_TIME ,COLL_VALUE,TAGS,UPDATE_TIME )");
//		sb.append("VALUES (?,?,?,?,?,?,?)");
//		sb.append(" ON DUPLICATE KEY UPDATE");
//		sb.append(" moid = values(moid),");
//		sb.append(" graph_id = values(graph_id),");
//		sb.append(" coll_time = values(coll_time),");
//		sb.append(" coll_value = values(coll_value),");
//		sb.append(" update_time = values(update_time)");
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_B_METRIC_DATA data : list) {
//				int i = 1;
//				ps.setLong(i++, data.getMoid());
//				ps.setLong(i++, data.getMonitorId());
//				ps.setLong(i++, data.getGraph_id());
//				ps.setTimestamp(i++, Timestamp.valueOf(data.getTime()));
//				ps.setDouble(i++, data.getMeasuredVal());
//				ps.setString(i++, data.getSignalNumber());
//				ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	/***
//	 * @Author zero
//	 * @Description merge动环B接口设备，索引是fsuid、device_id
//	 * @InitDate 11:43 2021/1/20
//	 * @Param [conn, list]
//	 **/
//	public void mergePeBMO(Connection conn, List<MON_PE2_B_MO> list) throws SQLException {
//		LOG.debug("mergePeBMO");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_PE2_B_MANAGEDOBJECT (monitor_id,fsuid,device_id,device_name,SITE_ID,room_id,site_name,room_name,device_type,device_subtype,model,brand,rated_capacity,version,begin_runtime,dev_describe,conf_remark,ip_address )  values ");
//		sb.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		sb.append(" ON DUPLICATE key UPDATE ");
//		sb.append(" device_name = values(device_name),");
//		sb.append(" SITE_ID = values(SITE_ID),");
//		sb.append(" room_id = values(room_id),");
//		sb.append(" site_name = values(site_name),");
//		sb.append(" room_name = values(room_name),");
//		sb.append(" device_type = values(device_type),");
//		sb.append(" device_subtype = values(device_subtype),");
//		sb.append(" model = values(model),");
//		sb.append(" brand = values(brand),");
//		sb.append(" rated_capacity = values(rated_capacity),");
//		sb.append(" version = values(version),");
//		sb.append(" begin_runtime = values(begin_runtime),");
//		sb.append(" dev_describe = values(dev_describe),");
//		sb.append(" conf_remark = values(conf_remark),");
//		sb.append(" ip_address = values(ip_address)");
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_B_MO mo: list) {
//				int i = 1;
//				ps.setLong(i++,mo.getMonitorId());
//				ps.setString(i++,mo.getFsuid());
//				ps.setString(i++,mo.getDeviceID());
//				ps.setString(i++,mo.getDeviceName());
//				ps.setString(i++,mo.getSiteID());
//				ps.setString(i++,mo.getRoomID());
//				ps.setString(i++,mo.getSiteName());
//				ps.setString(i++,mo.getRoomName());
//				ps.setInt(i++,mo.getDeviceType());
//				ps.setInt(i++,mo.getDeviceSubType());
//				ps.setString(i++,mo.getModel());
//				ps.setString(i++,mo.getBrand());
//				ps.setFloat(i++,mo.getRatedCapacity());
//				ps.setString(i++,mo.getVersion());
//				ps.setString(i++,mo.getBeginRunTime());
//				ps.setString(i++,mo.getDevDescribe());
//				ps.setString(i++,mo.getConfRemark());
//				ps.setString(i++,mo.getIp());
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 查询 MON_PE2_B_MANAGEDOBJECT 刚刚插入的moid为空的值
//	 * @InitDate 13:55 2021/1/15
//	 * @Param [conn]
//	 **/
//	@Override
//	public List<MON_PE2_B_MO> getMON_PE2_B_MANAGEDOBJECTListWithNoMoid(Connection conn) throws SQLException {
//		LOG.debug("getMON_PE2_B_MANAGEDOBJECTlist");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT MOID, MONITOR_ID,FSUID, DEVICE_ID, DEVICE_NAME,");
//		sb.append("	SITE_ID, ROOM_ID, SITE_NAME, ROOM_NAME,");
//		sb.append("	DEVICE_TYPE, DEVICE_SUBTYPE, MODEL, ");
//		sb.append("	BRAND, RATED_CAPACITY, VERSION, ");
//		sb.append("	BEGIN_RUNTIME, DEV_DESCRIBE, CONF_REMARK,IP_ADDRESS");
//		sb.append("	from MON_PE2_B_MANAGEDOBJECT ");
//		sb.append("	where moid = -1");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_PE2_B_MO> list;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_PE2_B_MO p = new MON_PE2_B_MO(rs.getLong(i++),rs.getLong(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getInt(i++),rs.getInt(i++),rs.getString(i++),rs.getString(i++),rs.getFloat(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++),rs.getString(i++));
//				list.add(p);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//
//	/***
//	 * @Author zero
//	 * @Description 更新 MON_PE2_MANAGEDOBJECT_B，set moid
//	 * @InitDate 14:01 2021/1/15
//	 * @Param [conn, moList]
//	 **/
//	@Override
//	public void updateMON_PE2_B_MANAGEDOBJECT(Connection conn, List<MON_PE2_B_MO> moList) throws SQLException {
//		LOG.debug("mergePeBMO");
//		StringBuilder sb = new StringBuilder();
//		sb.append("update MON_PE2_B_MANAGEDOBJECT set moid = ? where MONITOR_ID = ? and FSUID = ? and DEVICE_ID = ?");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_B_MO mo: moList) {
//				int i = 1;
//				ps.setLong(i++,mo.getMoid());
//				ps.setLong(i++,mo.getMonitorId());
//				ps.setString(i++,mo.getFsuid());
//				ps.setString(i++,mo.getDeviceID());
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	/***
//	 * @Author zero
//	 * @Description merge MON_PE_MANAGEDOBJECT_TMP，唯一索引是主键moid
//	 * @InitDate 13:30 2021/1/15
//	 * @Param [conn, list]
//	 **/
//	@Override
//	public void mergePeMO(Connection conn, List<MON_PE_MANAGEDOBJECT_TMP> list) throws SQLException {
//		LOG.debug("mergePeBMO");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_PE_MANAGEDOBJECT_TMP (MOID,DEVICE_NAME,ROOM_ID,IP_ADDRESS,DEVICE_TYPE");
//		sb.append(" ,PROTOCOL_TYPE ,MANAGED ,MONITOR_ID,SEAT_CUST_ID,DEVICE_TYPE_ID,SUB_DEVICE_TYPE,UPDATE_TIME)");
//		sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?)");
//		sb.append(" on duplicate key update");
//		sb.append(" DEVICE_NAME = values(DEVICE_NAME),");
//		sb.append(" ROOM_ID = values(ROOM_ID),");
//		sb.append(" IP_ADDRESS = values(IP_ADDRESS),");
//		sb.append(" DEVICE_TYPE = values(DEVICE_TYPE),");
//		sb.append(" PROTOCOL_TYPE  = values(PROTOCOL_TYPE ),");
////		sb.append(" MANAGED  = values(MANAGED ),");  //初始化默认监控，后续不修改该字段，由web控制
//		sb.append(" MONITOR_ID = values(MONITOR_ID),");
//		sb.append(" SEAT_CUST_ID = values(SEAT_CUST_ID),");
//		sb.append(" DEVICE_TYPE_ID = values(DEVICE_TYPE_ID),");
//		sb.append(" SUB_DEVICE_TYPE = values(SUB_DEVICE_TYPE),");
//		sb.append(" UPDATE_TIME = values(UPDATE_TIME)");
//
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE_MANAGEDOBJECT_TMP mo: list) {
//				int i = 1;
//				ps.setLong(i++,mo.getMOID());//moid
//				ps.setString(i++,mo.getDEVICE_NAME());//device_name
//				ps.setLong(i++,mo.getROOM_ID());
//				ps.setString(i++,mo.getIP_ADDRESS());
//				ps.setString(i++,mo.getDEVICE_TYPE());
//				ps.setString(i++,mo.getPROTOCOL_TYPE());//protocol_type
//				ps.setInt(i++,1);//managed
//				ps.setLong(i++,mo.getMonitorId());
//				ps.setLong(i++,mo.getSEAT_CUST_ID());//seat_cust_id
//				ps.setInt(i++,mo.getDEVICE_TYPE_ID());
//				ps.setInt(i++,mo.getSUB_DEVICE_TYPE());
//				ps.setTimestamp(i++,new Timestamp(System.currentTimeMillis()));
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	/***
//	 * @Author zero
//	 * @Description 更新阈值配置
//	 * @Description 索引是moid、graph_id、signal_number
//	 * @InitDate 13:56 2021/1/19
//	 * @Param [conn, list]
//	 **/
//	public void mergePeThreshold(Connection conn, List<MON_PE2_B_THRESHOLD> list) throws SQLException {
//		LOG.debug("mergePeBMO");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_PE2_B_THRESHOLD (monitor_id,moid,fsuid,device_id,graph_id,type,id,SIGNAL_NUMBER ,THRESHOLD ,ALARM_LEVEL ,NMALARM_ID )  values ");
//		sb.append(" (?,?,?,?,?,?,?,?,?,?,?) ");
//		sb.append(" ON DUPLICATE key UPDATE ");
//		sb.append(" type = values(type),");
//		sb.append(" id = values(id),");
//		sb.append(" SIGNAL_NUMBER  = values(SIGNAL_NUMBER),");
//		sb.append(" THRESHOLD  = values(THRESHOLD),");
//		sb.append(" ALARM_LEVEL  = values(ALARM_LEVEL),");
//		sb.append(" NMALARM_ID = values(NMALARM_ID)");
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (MON_PE2_B_THRESHOLD mo: list) {
//				int i = 1;
//				ps.setLong(i++,mo.getMonitorId());
//				ps.setLong(i++,mo.getMoid());
//				ps.setString(i++,mo.getFsuid());
//				ps.setString(i++,mo.getDeviceID());
//				ps.setLong(i++,mo.getGraph_id());
//				ps.setInt(i++,mo.getType());
//				ps.setString(i++,mo.getId());
//				ps.setString(i++,mo.getSignalNumber());
//				ps.setFloat(i++,mo.getThreshold());
//				ps.setInt(i++,mo.getAlarmLevel());
//				ps.setString(i++,mo.getNmAlarmID());
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//
//	@Override
//	public long getMoidSeq(Connection conn) throws SQLException {
//		LOG.debug("getMoidSeq");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT nextval('MON_PE_MANAGEDOBJECT_TMP_SEQ') FROM DUAL");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			rs = ps.executeQuery();
//			if (rs.next()) {
//				return rs.getInt(1);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return -1;
//	}
//}
