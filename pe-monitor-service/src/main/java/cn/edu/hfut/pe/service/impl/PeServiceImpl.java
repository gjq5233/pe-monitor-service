package cn.edu.hfut.pe.service.impl;

import java.sql.Connection;
import java.util.*;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.util.CloseUtil;
import base.util.netty.ChannelUtil;
import cn.edu.hfut.pe.dao.IpeDao;
import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import cn.edu.hfut.pe.service.IpeConfService;
import cn.edu.hfut.pe.service.IpeService;
import io.netty.channel.Channel;

@Service
public class PeServiceImpl implements IpeConfService, IpeService {
	private final static Logger LOG = LoggerFactory.getLogger(PeServiceImpl.class);

	@Autowired
	private IpeDao peDao;

	@Autowired
	private DataSource dataSource; // 数据源

	@Override
	public List<MON_DEVICE> getMON_DEVICElist() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(true);
			return peDao.getMON_DEVICElist(conn);
		} catch (Throwable e) {
			LOG.warn("{}", e);
		} finally {
			CloseUtil.closeConnection(conn);
		}
		LOG.warn("MON_DEVICE data is null");
		return null;
	}

	@Override
	public List<MON_DCU> getMON_DCUlist() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(true);
			return peDao.getMON_DCUlist(conn);
		} catch (Throwable e) {
			LOG.warn("{}", e);
		} finally {
			CloseUtil.closeConnection(conn);
		}
		LOG.warn("MON_DCU data is null");
		return null;
	}

	@Override
	public List<MON_METRIC_CONFIG> getMON_METRIC_CONFIGlist() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(true);
			return peDao.getMON_METRIC_CONFIGlist(conn);
		} catch (Throwable e) {
			LOG.warn("{}", e);
		} finally {
			CloseUtil.closeConnection(conn);
		}
		LOG.warn("MON_METRIC_CONFIG data is null");
		return null;
	}

	@Override
	public Object dealMoGraph(Channel ch, MON_DEVICE device, MON_METRIC_CONFIG graph) {
		LOG.debug("dealMoGraph, ch:{}, device:{}, graph:{}", ch, device, graph);

		Msg req = Msg.gentReqByGraph(graph);
		if (req == null) {
			LOG.warn("vs format error, graph:{}", graph);
			return null;
		}
		req.setSeq((int) graph.getMETRIC_CONFIG_ID());// 02 03返回的数据序号对应graphid，只要能找到graphid就能对应到指标
		req.setUnitId((byte) device.getDEVICE_UNIT_IDENTFIER());
		ChannelUtil.write(ch, req);
		return null;
	}
}
