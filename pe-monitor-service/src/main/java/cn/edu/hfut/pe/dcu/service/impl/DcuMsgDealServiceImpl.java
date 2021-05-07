package cn.edu.hfut.pe.dcu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.edu.hfut.pe.cache.ConfCache;
import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.dcu.comm.msg._02YxResp;
import cn.edu.hfut.pe.dcu.comm.msg._03YcResp;
import cn.edu.hfut.pe.dcu.comm.msg._42LoginReq;
import cn.edu.hfut.pe.dcu.service.IdcuMsgDealService;
import cn.edu.hfut.pe.dcu.util.LogDcuidUtil;
import cn.edu.hfut.pe.dcu.util.Modbus4jUtils;
import cn.edu.hfut.pe.model.DeviceAddr;
import cn.edu.hfut.pe.model.entity.MON_DATA;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

@Service
public class DcuMsgDealServiceImpl implements IdcuMsgDealService {
	private final static Logger LOG = LoggerFactory.getLogger(DcuMsgDealServiceImpl.class);

	@Override
	public MON_DCU check(_42LoginReq req) {
		LOG.info("{}", req);
		MON_DCU dcu = new MON_DCU(req.getDcuid(), 123456789L, "dcuname", "dcu addr", 123, 123);
		/**
		 * 1、dcuid是否匹配
		 * 
		 * 2、手机号 是否匹配
		 * 
		 * 3、<dcuid, channel>缓存是否有活动连接，有（之前的连接不等于现在的连接）则关闭之前连接，并打印日志
		 */
		return dcu;
	}

	@Override
	public void deal03YcData(Long dcuid, _03YcResp msg) {
		dealDcuRespData(dcuid, msg, msg.getData());
	}

	@Override
	public void deal02YxData(Long dcuid, _02YxResp msg) {
		dealDcuRespData(dcuid, msg, msg.getData());
	}

	private void dealDcuRespData(Long dcuid, Msg msg, byte[] bytesData) {
		long seq = msg.getSeq() & 0x00FFFFFFFF;
		MON_METRIC_CONFIG graph = ConfCache.getMetricConfAndSub(seq);
		if (graph == null) {
			LOG.warn("error seq:{}, msg:{}, dcuid:{} ", seq, msg, dcuid);
			return;
		}
		MON_DEVICE device = ConfCache.getDevice(new DeviceAddr(dcuid, msg.getUnitId()));
		if (device == null) {
			LOG.warn("error device, dcuid:{}, msg:{}", dcuid, msg);
			return;
		}
		List<MON_DATA> valueList = new ArrayList<>();
		Modbus4jUtils.parseGraph(device.getDEVICE_ID(), graph, bytesData);
		LogDcuidUtil.log(dcuid, LOG, "dcuid:{}, generate data:{} ", dcuid, valueList);
		LOG.info("===" + valueList);
		if (!CollectionUtils.isEmpty(valueList)) {
			saveDataList(valueList);
		}
	}

	private void saveDataList(List<MON_DATA> valueList) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
