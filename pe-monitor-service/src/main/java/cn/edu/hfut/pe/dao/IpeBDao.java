//package cn.edu.hfut.pe.dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//import cn.edu.hfut.pe.pe2.B.dto.MON_PE2_B_ALARM;
//import cn.edu.hfut.pe.pe2.B.dto.MON_PE2_B_METRIC_DATA;
//import cn.edu.hfut.pe.pe2.B.dto.MON_PE2_B_MO;
//import cn.edu.hfut.pe.pe2.B.dto.MON_PE2_B_THRESHOLD;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE2_TEMPLATE_GRAPH;
//import cn.edu.hfut.pe.pe2.model.entity.MON_PE_MANAGEDOBJECT_TMP;
//
//public interface IpeBDao {
//
//	/***
//	 * @Author zero
//	 * @Description 获取B接口动环设备列表
//	 * @Description 当前仅定制于合肥城市云大数据中心业务
//	 * @InitDate 14:01 2021/1/13
//	 * @Param [conn]
//	 **/
//	List<MON_PE2_B_MO> getMON_PE2_B_MANAGEDOBJECTlist(Connection conn) throws SQLException;
//
//	/**
//	 * 获取MON_PE2_TEMPLATE_GRAPH(指标监视器表)全表记录
//	 *
//	 * @author GJQ
//	 * @return
//	 */
//	List<MON_PE2_TEMPLATE_GRAPH> getMON_PE2_TEMPLATE_GRAPHlist(Connection conn) throws SQLException;
//
//
//	void insertPeBDataList(Connection conn, List<MON_PE2_B_METRIC_DATA> dataList) throws SQLException;
//
//	void mergePeBAlarmList(Connection conn, List<MON_PE2_B_ALARM> list,boolean isStart) throws SQLException;
//
//	void mergePeBDataLast(Connection conn, List<MON_PE2_B_METRIC_DATA> list) throws SQLException;
//
//	void mergePeBMO(Connection conn, List<MON_PE2_B_MO> list) throws SQLException;
//
//	List<MON_PE2_B_MO> getMON_PE2_B_MANAGEDOBJECTListWithNoMoid(Connection conn) throws SQLException;
//
//	void updateMON_PE2_B_MANAGEDOBJECT(Connection conn, List<MON_PE2_B_MO> moList) throws SQLException;
//
//	void mergePeMO(Connection conn, List<MON_PE_MANAGEDOBJECT_TMP> list) throws SQLException;
//
//	void mergePeThreshold(Connection conn, List<MON_PE2_B_THRESHOLD> list) throws SQLException;
//
//	long getMoidSeq(Connection conn) throws SQLException;
//}
