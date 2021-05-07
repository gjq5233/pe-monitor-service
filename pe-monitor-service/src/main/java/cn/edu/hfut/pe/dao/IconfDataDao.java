///**
// * Project Name:DataSource-ConfDataService
// * File Name:IconfDataDao.java
// * Package Name:com.itcloud.monitor.dao
// * Date:2016年4月25日下午4:42:42
// * Copyright (c) 2016 All Rights Reserved.
// *
// */
//package cn.edu.hfut.pe.dao;
//
//import java.net.SocketAddress;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import cn.edu.hfut.pe.model.ModuleEnable;
//import cn.edu.hfut.pe.model.ZabbixMoid;
//import cn.edu.hfut.pe.model.bo.MONITORS_THRESHOLD;
//import cn.edu.hfut.pe.model.bo.MONITORS_TYPE_THRESHOLD;
//import cn.edu.hfut.pe.model.dbo.*;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_OP_LOG_LAST;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_RESOURCE;
//import cn.edu.hfut.pe.model.dbo.daemon.MON_APP_TYPE;
//import cn.edu.hfut.pe.model.dbo.netbackup.MON_NETBACKUP_MANAGER;
//import cn.edu.hfut.pe.model.dbo.sys.MON_MONITOR;
//import cn.edu.hfut.pe.model.dbo.sys.MON_PROXY;
//import cn.edu.hfut.pe.model.dbo.sys.MON_ROOM_KEYS;
//import cn.edu.hfut.pe.model.dbo.vc.MON_VM_METRIC;
//import cn.edu.hfut.pe.model.dbo.vc.MON_VM_RELATIONS;
//import cn.edu.hfut.pe.model.dbo.vc.MON_VM_TYPES;
//import cn.edu.hfut.pe.model.dbo.vc.MON_VSPHERE_MANAGER;
//import cn.edu.hfut.pe.model.dbo.zabbix.MON_ZABBIX_MANAGER;
//import cn.edu.hfut.pe.model.dto.pe.model.ItemInfo;
//import cn.edu.hfut.pe.model.dto.pe.model.MON_PE_MANAGEDOBJECT;
//
///**
// * 配置数据
// *
// * Date: 2016年4月25日 下午4:42:42 <br/>
// *
// * @author GJQ
// * @version
// * @since JDK 1.7
// * @see
// */
//public interface IconfDataDao {
//
//	/**
//	 * 获取MON_TOPOOBJECT(设备表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_TOPOOBJECT> getMON_TOPOOBJECTlist(Connection conn) throws SQLException;
//
//
//	/**
//	 * 获取监控点信息列表，表MON_MONITOR
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_MONITOR> getMonitorList(Connection conn) throws SQLException;
//
//	/**
//	 * 凭证key管理 MON_ROOM_KEYS表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_ROOM_KEYS> getRoomKeyList(Connection conn) throws SQLException;
//
//	Set<SocketAddress> getWebAddrList(Connection conn) throws SQLException;
//
//	/**
//	 * 管理 MON_APP_TYPE表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_TYPE> getAppTypeList(Connection conn) throws SQLException;
//
//	/**
//	 * 管理 MON_APP_RESOURCE表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_RESOURCE> getAppResourceList(Connection conn) throws SQLException;
//
//	/**
//	 * 管理 MON_APP_RESOURCE表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_OP_LOG_LAST> getAppOpLastList(Connection conn) throws SQLException;
//
//	List<MON_PROXY> getPROXYlist(Connection conn) throws SQLException;
//
//    List<ModuleEnable> getModuleEnableList(Connection conn) throws SQLException;
//
//	List<MON_NETBACKUP_MANAGER> getNetBackUpList(Connection conn) throws SQLException;
//
//
//	Set<SocketAddress> getHcServerAddrList(Connection conn) throws SQLException;
//}
