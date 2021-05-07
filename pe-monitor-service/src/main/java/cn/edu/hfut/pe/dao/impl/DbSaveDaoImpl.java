//package cn.edu.hfut.pe.dao.impl;
//
//import base.util.CloseUtil;
//import cn.edu.hfut.pe.cache.ConfCache;
//import cn.edu.hfut.pe.cons.DiscoveryTopoType;
//import cn.edu.hfut.pe.dao.IdbSaveDao;
//import cn.edu.hfut.pe.model.dbo.*;
//import cn.edu.hfut.pe.model.dbo.pe.D_ActiveAlarm;
//import cn.edu.hfut.pe.model.dbo.topo.*;
//import cn.edu.hfut.pe.util.BusiIdMixUtil;
//import cn.edu.hfut.pe.util.Moid;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.CollectionUtils;
//import java.sql.*;
//import java.util.*;
//
//public class DbSaveDaoImpl implements IdbSaveDao {
//	private final static Logger LOG = LoggerFactory.getLogger(DbSaveDaoImpl.class);
//
//	@Override
//	public void insertAlarmList(Connection conn, List<MON_DATA> list) throws SQLException {
//		LOG.debug("insertAlarmList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("INSERT INTO MON_ALARM_CUR(moid, room_id, ip, metric, tags, coll_time, coll_value, alarm_lv) ");
//		sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int c = 0;
//			for (MON_DATA d : list) {
//				int i = 1;
//
//				long moid = -1;
//				if (d.getMoid() != null && d.getMoid() > 0) {
//					moid = d.getMoid();
//				}
//				if (moid < 1) {
//					Long moidL = ConfCache.MAP_TOPOOBJECT_MOID.get(new MON_TOPOOBJECT(d.getROOM_ID(), d.getIP()));
//					if (moidL != null) {
//						moid = moidL.longValue();
//					} else {
//						LOG.debug("no moid, d:{}", d);
//						continue;
//					}
//				}
//
//				ps.setLong(i++, moid);
//				ps.setLong(i++, d.getROOM_ID());
//				ps.setString(i++, d.getIP());
//				ps.setString(i++, d.getMETRIC());
//				ps.setString(i++, d.getTAGS());
//				ps.setTimestamp(i++, new Timestamp(d.getCOLL_TIME()));
//				ps.setDouble(i++, d.getCOLL_VALUE());
//				ps.setInt(i++, d.getAlarm_lv());
//				ps.addBatch();
//				c++;
//			}
//			if (c > 0) {
//				ps.executeBatch();
//			} else {
//				LOG.warn("list:{}", list);
//			}
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	public void insertMetricList(Connection conn, List<MON_DATA> list) throws SQLException {
//		LOG.debug("insertMetricList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("INSERT INTO MON_DATA_CUR(moid, room_id, ip, metric, tags, coll_time, coll_value, alarm_lv) ");
//		sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int c = 0;
//			for (MON_DATA d : list) {
//				int i = 1;
//				Long moid = d.getMoid();
//				if (moid == null) {
//					Long moidL = ConfCache.MAP_TOPOOBJECT_MOID.get(new MON_TOPOOBJECT(d.getROOM_ID(), d.getIP()));
//					if (moidL != null) {
//						moid = moidL;
//					} else {
//						continue;
//					}
//				}
//				ps.setLong(i++, moid);
//				ps.setLong(i++, d.getROOM_ID());
//				ps.setString(i++, d.getIP());
//				ps.setString(i++, d.getMETRIC());
//				ps.setString(i++, d.getTAGS());
//				ps.setTimestamp(i++, new Timestamp(d.getCOLL_TIME()));
//				ps.setDouble(i++, d.getCOLL_VALUE());
//				ps.setInt(i++, d.getAlarm_lv());
//				ps.addBatch();
//				c++;
//			}
//			if (c > 0) {
//				ps.executeBatch();
//			} else {
//				LOG.warn("list:{}", list);
//			}
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	@Override
//	public void insertPeCDataAlarmList(Connection conn, List<D_ActiveAlarm> list) {
//		LOG.debug("insertPeCDataAlarmList");
//		StringBuilder sb = new StringBuilder();
//		sb.append(
//				"INSERT INTO MON_PE_ALARM(ALARM_ID, NODE_ID, NODE_NAME, ALARM_TIME, ALARM_LEVEL, ALARM_STATUS, ALARM_DESC, ALARM_VALUE, SERIAL_NO, BORGID) VALUES( ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (D_ActiveAlarm alarmData : list) {
//				int i = 1;
//				ps.setLong(i++, alarmData.getId());
//				ps.setLong(i++, alarmData.getNodeId());
//				ps.setString(i++, alarmData.getNodeName());
//				ps.setTimestamp(i++, alarmData.getAlarmTime());
//				ps.setInt(i++, alarmData.getAlarmLevel().getValue());
//				ps.setInt(i++, alarmData.getAlarmStatus());
//				ps.setString(i++, alarmData.getAlarmDesc());
//				ps.setFloat(i++, alarmData.getAlarmValue());
//				ps.setLong(i++, alarmData.getSerialNo());
//				ps.setInt(i++, BusiIdMixUtil.getOrgid(alarmData.getId()));
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	public void updateMON_DISCOVER_CMD(Connection conn, TopoCmdPercent per) throws SQLException {
//		LOG.info("update discover cmd percent");
//		StringBuilder sb = new StringBuilder();
//		sb.append("update MON_DISCOVER_CMD set ").append("update_time = ? , ").append("percent = ? ")
//				.append("where task_cmd_id = ? and task_id = ? ");
//
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int i = 1;
//			ps.setTimestamp(i++, new Timestamp(per.getUpdateTime()));
//			ps.setInt(i++, per.getPercent());// percent
//			ps.setLong(i++, per.getCmdId());
//			ps.setLong(i++, per.getTaskId());
//			ps.executeUpdate();
//
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	public void updateMON_DISCOVER_CMD(Connection conn, List<DiscoverTask> list) throws SQLException {
//		LOG.info("update discover cmd");
//		StringBuilder sb = new StringBuilder();
//		sb.append("update MON_DISCOVER_CMD set ").append("monitor_id = ? , ").append("start_time = ? , ")
//				.append("update_time = ? , ").append("cmd_status = ? , ").append("percent = ? , ")
//				.append("ip_list = ? ").append("where task_cmd_id = ? and task_id = ? ");
//
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (DiscoverTask task : list) {
//				int i = 1;
//				ps.setLong(i++, task.getMonitorId());
//				ps.setTimestamp(i++, new Timestamp(task.getStartTime()));
//				ps.setTimestamp(i++, new Timestamp(task.getFinishTime()));
//				ps.setInt(i++, 2);
//				ps.setInt(i++, 100);// percent
//				ps.setString(i++, task.getIpSegListString());
//				ps.setLong(i++, task.getCmdId());
//				ps.setLong(i++, task.getTaskId());
//				ps.addBatch();
//			}
//			ps.executeBatch();
//
//			for (DiscoverTask task : list) {
//				insertMON_TOPOLINK(conn, task.getTopoLinkList(), task.getTaskId(), task.getCmdId(), task.getRoomId());
//			}
//
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 分析所有topo连接，提取所有设备（不重复）并插入数据库
//	 * @Return Map<设备ip，设备存数据库的id>
//	 * @InitDate 13:42 2019/6/10
//	 * @Param [conn, linkList, roomId]
//	 **/
//	private Map<String, Integer> insertMON_DEVICE_INFO(Connection conn, long taskId, List<TopoLink> linkList,
//			long roomId) throws SQLException {
//		Map<String, Integer> deviceMap = new HashMap<>();
//		Set<String> ipTmpSet = new HashSet<>();
//		for (TopoLink link : linkList) {
//			LinkDevice startDevice = link.getStartDevice();
//			LinkDevice endDevice = link.getEndDevice();
//
//			if (startDevice != null) {
//				insertMON_DEVICE_INFO(startDevice, ipTmpSet, conn, taskId, deviceMap, roomId);
//			}
//
//			if (endDevice != null) {
//				insertMON_DEVICE_INFO(endDevice, ipTmpSet, conn, taskId, deviceMap, roomId);
//			}
//		}
//
//		return deviceMap;
//	}
//
//	private void insertMON_DEVICE_INFO(LinkDevice linkDevice, Set<String> ipTmpSet, Connection conn, long taskId,
//			Map<String, Integer> deviceMap, long roomId) throws SQLException {
//
//		String ip = linkDevice.getIp();
//		DeviceInfo device = linkDevice.getInfo();
//		if (ipTmpSet.add(ip)) {
//			int device_id = getNextVal(conn, "SELECT NEXTVAL('MON_DEVICE_INFO_SEQ')");
//			if (null != device) {
//				insertMON_DEVICE_INFO(conn, device, taskId, device_id, roomId);
//				deviceMap.put(ip, device_id);
//			} else {
//				DeviceInfo tmpDivice = new DeviceInfo(ip);
//				tmpDivice.setDeviceType(DiscoveryTopoType.OTHER);
//				insertMON_DEVICE_INFO(conn, tmpDivice, taskId, device_id, roomId);
//				deviceMap.put(ip, device_id);
//			}
//
//		}
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入所有topo连接
//	 * @InitDate 13:52 2019/6/10
//	 * @Param [conn, linkList, taskId, roomId]
//	 **/
//	private void insertMON_TOPOLINK(Connection conn, List<TopoLink> linkList, long taskId, long cmdId, long roomId)
//			throws SQLException {
//
//		if (CollectionUtils.isEmpty(linkList)) {
//			return;
//		}
//
//		Map<String, Integer> deviceMap = insertMON_DEVICE_INFO(conn, taskId, linkList, roomId);
//
//		LOG.info("insert topolink list");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_TOPOLINK(topo_id,dest_mac,task_id,task_cmd_id) ");
//		sb.append("values(?,?,?,?)");
//		PreparedStatement ps = null;
//		List<Long> linkIdList = new ArrayList<>(linkList.size());
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (TopoLink link : linkList) {
//				int i = 1;
//				long topoId = getNextVal(conn, "SELECT NEXTVAL('MON_TOPOLINK_SEQ')");
//				linkIdList.add(topoId);
//				ps.setLong(i++, topoId);
//				ps.setString(i++, link.getDestMac());
//				ps.setLong(i++, taskId);
//				ps.setLong(i++, cmdId);
//				ps.addBatch();
//			}
//			ps.executeBatch();
//
//			for (int i = 0; i < linkList.size(); i++) {
//				TopoLink link = linkList.get(i);
//				long linkId = linkIdList.get(i);
//				insertMON_LINK_DEVICE(conn, link.getStartDevice(), link.getEndDevice(), linkId, deviceMap);
//			}
//
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入一个连接两端的设备
//	 * @InitDate 13:52 2019/6/10
//	 * @Param [conn, startDevice, endDevice, topoId, deviceMap]
//	 **/
//	private void insertMON_LINK_DEVICE(Connection conn, LinkDevice startDevice, LinkDevice endDevice, long topoId,
//			Map<String, Integer> deviceMap) throws SQLException {
//		LOG.info("insert link device");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_LINK_DEVICE(node_id,ip,port,port_index,index_descr,node_dir,topo_id,device_id) ");
//		sb.append("values(?,?,?,?,?,?,?,?)");
//		List<LinkDevice> deviceList = new ArrayList<>();
//		if (null != startDevice) {
//			deviceList.add(startDevice);
//		}
//		if (null != endDevice) {
//			deviceList.add(endDevice);
//		}
//
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int tmp = 0;
//			for (LinkDevice device : deviceList) {
//				tmp++;
//
//				int i = 1;
//				Integer deviceId = deviceMap.get(device.getIp());
//				if (null != deviceId) {
//					long nodeId = getNextVal(conn, "SELECT NEXTVAL('MON_LINK_DEVICE_SEQ')");
//					ps.setLong(i++, nodeId);
//					ps.setString(i++, device.getIp());
//					ps.setInt(i++, device.getPort());
//					ps.setInt(i++, device.getIndex());
//					ps.setString(i++, device.getIndexDescr());
//					ps.setInt(i++, tmp);// 上联设备为1，下联设备为2
//					ps.setLong(i++, topoId);
//					ps.setLong(i++, deviceId);
//					ps.addBatch();
//				}
//
//			}
//			ps.executeBatch();
//
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入单个设备信息
//	 * @InitDate 13:52 2019/6/10
//	 * @Param [conn, info, deviceId, roomId]
//	 **/
//	private void insertMON_DEVICE_INFO(Connection conn, DeviceInfo info, long taskId, long deviceId, long roomId)
//			throws SQLException {
//		LOG.info("insert divice info");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_DEVICE_INFO(device_id,moid,room_id,ip,device_type,task_id) ");
//		sb.append("values(?,?,?,?,?,?)");
//		PreparedStatement ps = null;
//
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int i = 1;
//			ps.setLong(i++, deviceId);
//			ps.setLong(i++, Moid.getMoid(roomId, info.getDeviceIP()));
//			ps.setLong(i++, roomId);
//			ps.setString(i++, info.getDeviceIP());
//			ps.setInt(i++, info.getDeviceType());
//			ps.setLong(i++, taskId);
//			ps.executeUpdate();
//
//			if (info.getIpSet() != null) {
//				insertMON_DEVICE_IP(conn, info.getIpSet(), deviceId);
//			} else {
//				insertMON_DEVICE_IP(conn, new HashSet<String>() {
//					{
//						add(info.getDeviceIP());
//					}
//				}, deviceId);
//			}
//
//			if (info.getSysTable() != null) {
//				insertMON_DEVICE_SYSINFO(conn, info.getSysTable(), deviceId);
//			}
//
//			if (info.getIfTable() != null) {
//				insertMON_DEVICE_IFINFO(conn, info.getIfTable(), deviceId);
//			}
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入同一个设备的多个ip
//	 * @InitDate 13:53 2019/6/10
//	 * @Param [conn, ipSet, deviceId]
//	 **/
//	private void insertMON_DEVICE_IP(Connection conn, Set<String> ipSet, long deviceId) throws SQLException {
//		LOG.info("insert device ipSet");
//		StringBuilder sb = new StringBuilder();
//		sb.append("insert into MON_DEVICE_IP(ip_id,ip,device_id) ");
//		sb.append("values(NEXTVAL('MON_DEVICE_IP_SEQ'),?,?)");
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (String ip : ipSet) {
//				int i = 1;
//				ps.setString(i++, ip);
//				ps.setLong(i++, deviceId);
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入单个设备信息中的系统信息
//	 * @InitDate 13:53 2019/6/10
//	 * @Param [conn, sys, deviceId]
//	 **/
//	private void insertMON_DEVICE_SYSINFO(Connection conn, SysTable sys, long deviceId) throws SQLException {
//		LOG.info("insert device sysinfo");
//		StringBuilder sb = new StringBuilder();
//		sb.append(
//				"insert into MON_DEVICE_SYSINFO(sys_id,sys_descr,sys_objectId,sys_uptime,sys_contact,sys_name,sys_location,sys_service,ip_forwarding,device_id) ");
//		sb.append("values(NEXTVAL('MON_DEVICE_SYSINFO_SEQ'),?,?,?,?,?,?,?,?,?)");
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			int i = 1;
//			ps.setString(i++, sys.getSysDescr());
//			ps.setString(i++, sys.getSysObjectId());
//			ps.setString(i++, sys.getSysUpTime());
//			ps.setString(i++, sys.getSysContact());
//			ps.setString(i++, sys.getSysName());
//			ps.setString(i++, sys.getSysLocation());
//			ps.setInt(i++, sys.getSysService());
//			ps.setInt(i++, sys.getIpForwarding());
//			ps.setLong(i++, deviceId);
//
//			ps.executeUpdate();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 插入单个设备的多个接口信息
//	 * @InitDate 13:53 2019/6/10
//	 * @Param [conn, ifInfoList, deviceId]
//	 **/
//	public void insertMON_DEVICE_IFINFO(Connection conn, List<IfInfo> ifInfoList, long deviceId) throws SQLException {
//		LOG.info("insert device if info");
//		StringBuilder sb = new StringBuilder();
//		sb.append(
//				"insert into MON_DEVICE_IFINFO(if_id,if_index,descr,if_type,speed,phys_address,admin_status,oper_status,in_octets,out_octets,phys_port,port_ip,device_id) ");
//		sb.append("values(NEXTVAL('MON_DEVICE_IFINFO_SEQ'),?,?,?,?,?,?,?,?,?,?,?,?)");
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (IfInfo ifInfo : ifInfoList) {
//				int i = 1;
//				ps.setInt(i++, ifInfo.getIfIndex());
//				ps.setString(i++, ifInfo.getIfDescr());
//				ps.setString(i++, ifInfo.getIfType());
//				ps.setLong(i++, ifInfo.getIfSpeed());
//				ps.setString(i++, ifInfo.getIfPhysAddress());
//				ps.setInt(i++, ifInfo.getIfAdminStatus());
//				ps.setInt(i++, ifInfo.getIfOperStatus());
//				ps.setLong(i++, ifInfo.getIfInOctets());
//				ps.setLong(i++, ifInfo.getIfOutOctets());
//				ps.setInt(i++, ifInfo.getPhysPort());
//				ps.setString(i++, ifInfo.getIps());
//				ps.setLong(i++, deviceId);
//
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//
//	}
//
//	/***
//	 * @Author zero
//	 * @Description 用于取序列值，sql如select MON_TOPOLINK_SEQ.NEXTVAL FROM DUAL
//	 * @InitDate 18:25 2019/6/3
//	 * @Param [conn, sql]
//	 **/
//	private Integer getNextVal(Connection conn, String sql) throws SQLException {
//		PreparedStatement ps = null;
//		int ret;
//		try {
//			ps = conn.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			rs.next();
//			ret = rs.getInt(1);
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//		return ret;
//	}
//
//	@Override
//	public int getMoidSeq(Connection conn) throws SQLException {
//		LOG.debug("getMoidSeq");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT RES_MOID_SEQ.nextval FROM DUAL");
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
//
//	@Override
//	public void updateHcOrgStatus(Connection conn, List<Long> activeOrgidList) throws SQLException {
//		LOG.info("updateHcOrgStatus, orgidList:{}", activeOrgidList);
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE HC_ORG_DATA_STATUS SET LAST_DATA_TIME=now() WHERE ORG_ID=? ");
//
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			for (Long orgId : activeOrgidList) {
//				ps.setLong(1, orgId);
//				ps.addBatch();
//			}
//			ps.executeBatch();
//		} finally {
//			CloseUtil.closeSt(ps);
//		}
//	}
//}
