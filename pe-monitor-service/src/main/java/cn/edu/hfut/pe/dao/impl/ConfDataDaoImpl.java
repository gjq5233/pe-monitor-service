//package cn.edu.hfut.pe.dao.impl;
//
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.sql.*;
//import java.util.*;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.Base64Utils;
//
//import base.util.CloseUtil;
//import base.util.Encrypt;
//import cn.edu.hfut.pe.dao.IconfDataDao;
//import cn.edu.hfut.pe.model.ModuleEnable;
//import cn.edu.hfut.pe.model.dbo.*;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_OP_LOG_LAST;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_RESOURCE;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_TYPE;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_MONITOR_DAEMON;
//import cn.edu.hfut.pe.model.dbo.netbackup.MON_NETBACKUP_MANAGER;
//import cn.edu.hfut.pe.model.dbo.sys.MON_MONITOR;
//import cn.edu.hfut.pe.model.dbo.sys.MON_PROXY;
//import cn.edu.hfut.pe.model.dbo.sys.MON_ROOM_KEYS;
//import cn.edu.hfut.pe.util.PasswdUtil;
//
//public class ConfDataDaoImpl implements IconfDataDao {
//	private final static Logger LOG = LoggerFactory.getLogger(ConfDataDaoImpl.class);
//
//	private final int sqlQueryTime = 15;
//
//	@Override
//	public List<MON_MONITOR> getMonitorList(Connection conn) throws SQLException {
//
//		LOG.debug("getMonitorList");
//		StringBuilder sb = new StringBuilder();
//		// sb.append("SELECT monitor_id, room_id, monitor_name, monitor_ip,
//		// serial_number, proxy_id, is_default, is_verify_host ");
//		// sb.append(" FROM mon_monitor WHERE is_enable = 1 ");
//		sb.append(
//				"SELECT monitor_id, m.room_id, monitor_name, monitor_ip, m.serial_number, proxy_id, m.is_default, m.is_verify_host, BELONG_CUST_ID, ");
//		sb.append("p.ID, p.access_ip, p.room_id, p.ip_verification, p.proxy_ip, p.proxy_name, p.serial_number ");
//		sb.append("	 FROM MON_MONITOR m ");
//		sb.append("    INNER JOIN MON_PROXY p ");
//		sb.append(" ON m.room_id=p.room_id ");
//		sb.append(" WHERE m.is_enable = 1 ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_MONITOR> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<MON_MONITOR>();
//			while (rs.next()) {
//				int i = 1;
//				MON_MONITOR m = new MON_MONITOR(rs.getLong(i++), rs.getLong(i++), rs.getString(i++), rs.getString(i++),
//						rs.getString(i++), rs.getLong(i++), rs.getBoolean(i++), rs.getBoolean(i++), rs.getLong(i++));
//
//				MON_PROXY proxy = new MON_PROXY(rs.getLong(i++), rs.getString(i++), rs.getLong(i++), rs.getBoolean(i++),
//						rs.getString(i++), rs.getString(i++), rs.getString(i++));
//				m.setProxy(proxy);
//
//				list.add(m);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//	@Override
//	public List<MON_ROOM_KEYS> getRoomKeyList(Connection conn) throws SQLException {
//		LOG.debug("getRoomKeyList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT room_id, private_key, public_key FROM MON_ROOM_KEYS");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_ROOM_KEYS> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<MON_ROOM_KEYS>();
//			while (rs.next()) {
//				int i = 1;
//				MON_ROOM_KEYS p = new MON_ROOM_KEYS(rs.getLong(i++), rs.getString(i++), rs.getString(i++));
//				String pubKey = Base64Utils.encodeToString(Encrypt.getMonitorRsaPublicKey(p.getPUBLIC_KEY()));
//				p.setPUBLIC_KEY(pubKey);// 还原des加密的密钥base字符串
//
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
//	public List<MON_PROXY> getPROXYlist(Connection conn) throws SQLException {
//		LOG.debug("getPROXYlist");
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(" SELECT id, ROOM_ID, ACCESS_IP FROM MON_PROXY ");
//
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_PROXY> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<MON_PROXY>();
//			while (rs.next()) {
//				int i = 1;
//				MON_PROXY p = new MON_PROXY();
//				p.setID(rs.getLong(i++));
//				p.setROOM_ID(rs.getLong(i++));
//				p.setACCESS_IP(rs.getString(i++));
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
//
//	@Override
//	public List<MON_TOPOOBJECT> getMON_TOPOOBJECTlist(Connection conn) throws SQLException {
//		LOG.debug("getMON_TOPOOBJECTlist");
//		StringBuilder sb = new StringBuilder();
//		sb.append(" SELECT o.MOID, o.ROOM_ID, o.NAME FROM MON_TOPOOBJECT o   ");
//		sb.append(" INNER JOIN MON_MANAGEDOBJECT mo ON o.moid = mo.moid AND mo.managed='1' ");
//		sb.append(" WHERE is_node='1' or IS_INTERFACE='1' ");
//		sb.append(" 	UNION ALL ");
//		sb.append(" SELECT R.MOID, R.ROOM_ID, R.HOST_IP FROM RES_RESOURCES R");
//		sb.append(" WHERE STATE='1' AND HOST_IP IS NOT NULL ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_TOPOOBJECT> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<MON_TOPOOBJECT>();
//			while (rs.next()) {
//				int i = 1;
//				MON_TOPOOBJECT p = new MON_TOPOOBJECT(rs.getLong(i++), rs.getLong(i++), rs.getString(i++));
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
//
//	@Override
//	public Set<SocketAddress> getWebAddrList(Connection conn) throws SQLException {
//		LOG.debug("getWebAddrList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT ip_address, port FROM MON_WEB_ADDRESS WHERE is_enable=1 ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		Set<SocketAddress> set = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			set = new HashSet<>();
//			while (rs.next()) {
//				int i = 1;
//				InetSocketAddress isa = new InetSocketAddress(rs.getString(i++), rs.getInt(i++));
//				set.add(isa);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return set;
//	}
//
//	@Override
//	public List<MON_APP_TYPE> getAppTypeList(Connection conn) throws SQLException {
//		LOG.debug("getAppTypeList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT app_id, app_name, app_op_name, app_jar_prefix, app_file_name FROM MON_APP_TYPE");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_APP_TYPE> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_APP_TYPE p = new MON_APP_TYPE(rs.getLong(i++), rs.getString(i++), rs.getString(i++),
//						rs.getString(i++), rs.getString(i++));
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
//	public List<MON_APP_RESOURCE> getAppResourceList(Connection conn) throws SQLException {
//		LOG.debug("getAppResourceList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT app_res_id, res_name, app_id, res_type, res_version, res_url, resource_md5 ");
//		sb.append(" FROM MON_APP_RESOURCE WHERE is_enable = 1");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_APP_RESOURCE> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_APP_RESOURCE p = new MON_APP_RESOURCE(rs.getLong(i++), rs.getString(i++), rs.getLong(i++),
//						rs.getInt(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++));
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
//	public List<MON_APP_OP_LOG_LAST> getAppOpLastList(Connection conn) throws SQLException {
//		LOG.debug("getAppResourceList");
//		StringBuilder sb = new StringBuilder();
//		sb.append(" select o.mon_app_id, o.op_id, o.op_app_res_id, o.fail_num, ");
//		sb.append(" r.app_res_id, r.res_name, r.app_id, r.res_type, r.res_version, r.res_url, r.resource_md5, ");
//		sb.append(" t.app_id, t.app_name, t.app_op_name, t.app_jar_prefix, t.app_file_name,  ");
//		sb.append(" m.room_id, m.is_default, m.monitor_id  ");
//		sb.append(" from MON_APP_OP_LOG_LAST o INNER JOIN MON_APP_RESOURCE r ON o.op_app_res_id = r.app_res_id");
//		sb.append(" INNER JOIN MON_APP_TYPE t ON r.app_id = t.app_id ");
//		sb.append(" INNER JOIN MON_APP_DEPLOY d ON d.mon_app_id = o.mon_app_id ");
//		sb.append(" INNER JOIN MON_MONITOR m ON m.monitor_id = d.monitor_id ");
//		sb.append(" WHERE o.op_time > DATE_SUB(NOW(),INTERVAL 30 day) AND r.is_enable = 1 ");
//		sb.append(" AND m.is_enable = 1 ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_APP_OP_LOG_LAST> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_APP_OP_LOG_LAST p = new MON_APP_OP_LOG_LAST(rs.getLong(i++), rs.getLong(i++), rs.getLong(i++),
//						rs.getInt(i++));
//				MON_APP_RESOURCE r = new MON_APP_RESOURCE(rs.getLong(i++), rs.getString(i++), rs.getLong(i++),
//						rs.getInt(i++), rs.getString(i++), rs.getString(i++), rs.getString(i++));
//				MON_APP_TYPE t = new MON_APP_TYPE(rs.getLong(i++), rs.getString(i++), rs.getString(i++),
//						rs.getString(i++), rs.getString(i++));
//				MON_MONITOR_DAEMON d = new MON_MONITOR_DAEMON(rs.getLong(i++), rs.getBoolean(i++), rs.getLong(i++));
//				r.setType(t);
//				p.setResource(r);
//				p.setMonitor(d);
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
//
//
//	@Override
//	public List<ModuleEnable> getModuleEnableList(Connection conn) throws SQLException {
//		LOG.debug("getModuleEnableMap");
//		StringBuilder sb = new StringBuilder();
//		sb.append("select * from ");
//		sb.append("(select graph.MONITOR_ID ,(select module_name from MON_MODULE_INFO where MODULE_ID =info.PARENT_MODULE_ID) parent_name, ");
//		sb.append("(select MODULE_NAME from MON_MODULE_INFO where module_id = info.module_id) child_name, ");
//		sb.append("graph.ENABLE ");
//		sb.append("from MON_MODULE_INFO info JOIN MON_MODULE_MONITORID_GRAPH graph ");
//		sb.append("on info.MODULE_ID =graph.MODULE_ID ) ret ");
//		sb.append("where ret.parent_name is not null ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<ModuleEnable> list;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				ModuleEnable moduleEnable = new ModuleEnable(rs.getLong(i++),rs.getString(i++),rs.getString(i++),rs.getBoolean(i));
//				list.add(moduleEnable);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//	@Override
//	public List<MON_NETBACKUP_MANAGER> getNetBackUpList(Connection conn) throws SQLException {
//		LOG.debug("getNetBackUpList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT a.NETBACKUP_ID AS NETBACKUP_ID, a.SEAT_CUST_ID AS SEAT_CUST_ID, a.BELONG_CUST_ID AS BELONG_CUST_ID,");
//		sb.append("a.MONITOR_ID AS MONITOR_ID,a.CENTER_ID AS CENTER_ID, a.SERVER_NAME AS SERVER_NAME, a.IP AS IP, a.PORT AS PORT,");
//		sb.append("a.DOMAIN_NAME AS DOMAIN_NAME, a.DOMAIN_TYPE AS DOMAIN_TYPE,a.CREATE_TIME AS CREATE_TIME, b.ROOM_ID AS ROOM_ID, ");
//		sb.append("a.TRUST AS TRUSTEESHIP,a.USERNAME AS USERNAME,a.PASSWORD AS PASSWORD FROM MON_NETBACKUP_MANAGER a,MON_MONITOR b WHERE b.MONITOR_ID = a.MONITOR_ID");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<MON_NETBACKUP_MANAGER> list = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			list = new ArrayList<>();
//			while (rs.next()) {
//				int i = 1;
//				MON_NETBACKUP_MANAGER mon_netbackup_manager = new MON_NETBACKUP_MANAGER(rs.getLong(i++), rs.getInt(i++),
//						rs.getInt(i++), rs.getLong(i++), rs.getLong(i++), rs.getString(i++) , rs.getString(i++), rs.getInt(i++),
//						rs.getString(i++), rs.getString(i++), rs.getTimestamp(i++).getTime(), rs.getLong(i++), rs.getInt(i++), rs.getString(i++));
//				try {
//					String passwd = new String(PasswdUtil.decryptByPrivateKey(PasswdUtil.decryptBASE64(rs.getString(i)), PasswdUtil.AKEY), "utf-8");
//					mon_netbackup_manager.setPASSWORD(passwd);
//					list.add(mon_netbackup_manager);
//				} catch (Exception e) {
//					LOG.warn("nbu passwd 解密失败");
//				}
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return list;
//	}
//
//	@Override
//	public Set<SocketAddress> getHcServerAddrList(Connection conn) throws SQLException {
//		LOG.debug("getHcServerAddrList");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT ip_address, port FROM HC_SERVER_ADDRESS WHERE is_enable=1 ");
//		LOG.debug(sb.toString());
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		Set<SocketAddress> set = null;
//		try {
//			ps = conn.prepareStatement(sb.toString());
//			ps.setQueryTimeout(sqlQueryTime);
//			rs = ps.executeQuery();
//			set = new HashSet<>();
//			while (rs.next()) {
//				int i = 1;
//				InetSocketAddress isa = new InetSocketAddress(rs.getString(i++), rs.getInt(i++));
//				set.add(isa);
//			}
//		} finally {
//			CloseUtil.closeRs(rs);
//			CloseUtil.closeSt(ps);
//		}
//		return set;
//	}
//
//}
