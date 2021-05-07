package cn.edu.hfut.pe.dcu.service;

import cn.edu.hfut.pe.dcu.comm.msg._02YxResp;
import cn.edu.hfut.pe.dcu.comm.msg._03YcResp;
import cn.edu.hfut.pe.dcu.comm.msg._42LoginReq;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;

public interface IdcuMsgDealService {

	/**
	 * 检查并更新 位置，登录时间
	 * @param req
	 * @return
	 */
	MON_DCU check(_42LoginReq req);

	void deal03YcData(Long dcuid, _03YcResp msg);

	void deal02YxData(Long dcuid, _02YxResp msg);

}
