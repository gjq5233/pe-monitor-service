//package cn.edu.hfut.pe.service;
//
//import java.net.SocketAddress;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.kdgz.monitor.model.ModuleEnable;
//import com.kdgz.monitor.model.ZabbixMoid;
//import com.kdgz.monitor.model.bo.MONITORS_THRESHOLD;
//import com.kdgz.monitor.model.bo.MONITORS_TYPE_THRESHOLD;
//import com.kdgz.monitor.model.dbo.*;
//import com.kdgz.monitor.model.dbo.daemon.MON_APP_OP_LOG_LAST;
//import com.kdgz.monitor.model.dbo.daemon.MON_APP_RESOURCE;
//import com.kdgz.monitor.model.dbo.daemon.MON_APP_TYPE;
//import com.kdgz.monitor.model.dbo.netbackup.MON_NETBACKUP_MANAGER;
//import com.kdgz.monitor.model.dbo.sys.MON_MONITOR;
//import com.kdgz.monitor.model.dbo.sys.MON_PROXY;
//import com.kdgz.monitor.model.dbo.sys.MON_ROOM_KEYS;
//import com.kdgz.monitor.model.dbo.vc.MON_VM_METRIC;
//import com.kdgz.monitor.model.dbo.vc.MON_VM_RELATIONS;
//import com.kdgz.monitor.model.dbo.vc.MON_VM_TYPES;
//import com.kdgz.monitor.model.dbo.vc.MON_VSPHERE_MANAGER;
//import com.kdgz.monitor.model.dbo.zabbix.MON_ZABBIX_MANAGER;
//import com.kdgz.monitor.model.dto.pe.model.ItemInfo;
//import com.kdgz.monitor.model.dto.pe.model.MON_PE_MANAGEDOBJECT;
//import com.kdgz.monitor.model.ipmi.IpmiConf;
//
///**
// *
// * @author GJQ5233
// *
// */
//public interface IconfService {
//	/**
//	 * 获取MON_PROXY(前置代理服务登录验证)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
////	List<MON_PROXY> getPROXYlist();
//	
//	/**
//	 * 获取MON_TOPOOBJECT(设备表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_TOPOOBJECT> getMON_TOPOOBJECTlist();
//
//	/**
//	 * 获取MON_SNMPCREDENTIAL(snmpv12凭证)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_SNMPCREDENTIAL> getMON_SNMPCREDENTIALlist();
//
//	/**
//	 * 获取MON_CREDENTIALMAP(设备凭证)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_CREDENTIALMAP> getMON_CREDENTIALMAPlist();
//
//	
//
//	/**
//	 * 获取设备监视器and阀值
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MONITORS_THRESHOLD> getMONITORS_THRESHOLDlist();
//	
//	/**
//	 * 获取设备监视器and阀值, 监视器新增c_type字段，用于处理不规则oid值
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MONITORS_TYPE_THRESHOLD> getMONITORS_TYPE_THRESHOLDlist();
//
//	/**
//	 * 获取MON_INTERFACETHRESHOLD(接口阀值)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_INTERFACETHRESHOLD> getMON_INTERFACETHRESHOLDlist();
//
//	
//	/**
//	 * 获取MON_MANAGED_PING_CONFIG(ping管理)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_MANAGED_PING_CONFIG> getPING_CONFIGlist();
//
//	/**
//	 * 
//	 * 
//	 * @return
//	 */
//	List<MON_MONITOR> getMonitorList();
//
//	List<MON_ROOM_KEYS> getRoomKeyList();
//
//	Set<SocketAddress> getWebAddrList();
//
//	
//	/**
//	 * 管理 MON_APP_TYPE表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_TYPE> getAppTypeList();
//	
//	/**
//	 * 管理 MON_APP_RESOURCE表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_RESOURCE> getAppResourceList();
//	
//	/**
//	 * 管理 MON_APP_OP_LOG_LAST表
//	 * 
//	 * @param conn
//	 * @return
//	 * @throws SQLException
//	 */
//	List<MON_APP_OP_LOG_LAST> getAppOpLastList();
//
//	/**
//	 * 采集点zabbix配置查询
//	 * @return
//	 */
//	List<MON_ZABBIX_MANAGER> getMON_ZABBIX_MANAGERlist();
//
//	/**
//	 * zabbix 设备监控，应用监控 Moid查询, 用于指标数据保存过滤，无用数据
//	 * @return
//	 */
//	List<ZabbixMoid> getZabbixMoidList();
//
//	List<MON_PROXY> getPROXYlist();
//
//	List<MON_PE_MANAGEDOBJECT> getMON_PE_MANAGEDOBJECTlist();
//
//	List<ItemInfo> getItemInfoList();
//
//	List<MON_UPS_DISCOVERY> getUpsDiscovery();
//
//	List<ModuleEnable> getModuleEnableList();
//
//	List<MON_NETBACKUP_MANAGER> getNetBackUpList();
//
//	Set<SocketAddress> getHcServerAddrList();
//
//	/**
//	 * 查询ipmi配置相关表记录
//	 * 
//	 * @return
//	 */
//	List<IpmiConf> getIpmiConfList();
//}
