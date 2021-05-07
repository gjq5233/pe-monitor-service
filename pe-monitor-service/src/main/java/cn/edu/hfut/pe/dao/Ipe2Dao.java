//package cn.edu.hfut.pe.dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//import cn.edu.hfut.pe.pe2.B.dto.pt.ac.MON_PE2_B_AC_RECORD;
//import cn.edu.hfut.pe.pe2.hikvision.dto.MON_PE2_ACCESS_CONTROL_DEVICE;
//import cn.edu.hfut.pe.pe2.hikvision.dto.MON_PE2_ACCESS_CONTROL_EVENT;
//import cn.edu.hfut.pe.pe2.hikvision.dto.MON_PE2_ACCESS_CONTROL_POINT;
//import cn.edu.hfut.pe.pe2.hikvision.dto.MON_PE2_CARD_READER;
//import cn.edu.hfut.pe.pe2.model.dto.MON_PE2_VALUE;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE2_ALARM;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE2_MANAGEDOBJECT;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE2_TEMPLATE_GRAPH;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE_MANAGEDOBJECT_TMP;
//import cn.edu.hfut.pe.pe2.vertiv.entity.MON_PE2_ALARM_VERTIV;
//
//public interface Ipe2Dao {
//	/**
//	 * 获取MON_PE_MANAGEDOBJECT_TMP(设备表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_PE_MANAGEDOBJECT_TMP> getMON_PE_MANAGEDOBJECT_TMPlist(Connection conn) throws SQLException;
//	
//	/**
//	 * 获取MON_PE2_MANAGEDOBJECT(设备表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_PE2_MANAGEDOBJECT> getMON_PE2_MANAGEDOBJECTlist(Connection conn) throws SQLException;
//
//	/**
//	 * 获取MON_PE2_TEMPLATE_GRAPH(指标监视器表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_PE2_TEMPLATE_GRAPH> getMON_PE2_TEMPLATE_GRAPHlist(Connection conn) throws SQLException;
//
//	void insertPe2DataList(Connection conn, List<MON_PE2_VALUE> list) throws SQLException;
//
//	void mergePe2DataLast(Connection conn, List<MON_PE2_VALUE> list) throws SQLException;
//
//	void saveAlarmList(Connection conn, List<MON_PE2_ALARM> list) throws SQLException;
//
//	void updateAlarmList(Connection conn, List<MON_PE2_ALARM> list) throws SQLException;
//
//	void updateAlarmUpsVertivList(Connection conn, long moid) throws SQLException;
//	void mergeAlarmUpsVertivList(Connection conn, List<MON_PE2_ALARM_VERTIV> list) throws SQLException;
//
//    long getMoidSeq(Connection conn) throws SQLException;
//
//    List<MON_PE2_ACCESS_CONTROL_POINT> getMON_PE2_ACCESS_CONTROL_POINTWithNoMoid(Connection conn) throws SQLException;
//
//	void updateMON_PE2_ACCESS_CONTROL_POINT(Connection conn, List<MON_PE2_ACCESS_CONTROL_POINT> mobTmpList) throws SQLException;
//
//	void mergeDoorList(Connection conn, List<MON_PE2_ACCESS_CONTROL_POINT> list) throws SQLException;
//
//	void insertHikDoorPe2Tmp(Connection conn, List<MON_PE_MANAGEDOBJECT_TMP> mon_pe_managedobject_tmpList) throws SQLException;
//
//	void mergeAcsDeviceList(Connection conn, List<MON_PE2_ACCESS_CONTROL_DEVICE> list) throws SQLException;
//
//	void mergeCardReaderList(Connection conn, List<MON_PE2_CARD_READER> list) throws SQLException;
//
//	void mergeDoorEventList(Connection conn, List<MON_PE2_ACCESS_CONTROL_EVENT> list) throws SQLException;
//
//	void updateMON_PE2_ACCESS_CONTROL_DEVICEOnline(Connection conn, List<MON_PE2_ACCESS_CONTROL_DEVICE> moList) throws SQLException;
//
//	void updateMON_PE2_ACCESS_CONTROL_POINTOnline(Connection conn, List<MON_PE2_ACCESS_CONTROL_POINT> moList) throws SQLException;
//
//	void updateMON_PE2_CARD_READEROnline(Connection conn, List<MON_PE2_CARD_READER> moList) throws SQLException;
//
//	void updateMON_PE2_ACCESS_CONTROL_POINTState(Connection conn, List<MON_PE2_ACCESS_CONTROL_POINT> moList) throws SQLException;
//
//	void mergeJsyaDoorList(Connection conn, List<MON_PE2_B_AC_RECORD> list) throws SQLException;
//
//}
