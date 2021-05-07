package cn.edu.hfut.pe.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

public interface IpeDao {

	List<MON_DEVICE> getMON_DEVICElist(Connection conn) throws SQLException;

	List<MON_DCU> getMON_DCUlist(Connection conn) throws SQLException;

	List<MON_METRIC_CONFIG> getMON_METRIC_CONFIGlist(Connection conn) throws SQLException;

}
