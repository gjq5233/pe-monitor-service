package cn.edu.hfut.pe.dcu.comm;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import base.comm.Dispatcher;
import base.util.NetUtil;
import cn.edu.hfut.pe.cache.RunTimeCache;
import cn.edu.hfut.pe.dcu.comm.msg.ExcepCode;
import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.dcu.comm.msg.MsgResp;
import cn.edu.hfut.pe.dcu.comm.msg._02YxReq;
import cn.edu.hfut.pe.dcu.comm.msg._02YxResp;
import cn.edu.hfut.pe.dcu.comm.msg._03YcReq;
import cn.edu.hfut.pe.dcu.comm.msg._03YcResp;
import cn.edu.hfut.pe.dcu.comm.msg._05YkMsg;
import cn.edu.hfut.pe.dcu.comm.msg._06YtMsg;
import cn.edu.hfut.pe.dcu.comm.msg._42LoginReq;
import cn.edu.hfut.pe.dcu.comm.msg._42LoginResp;
import cn.edu.hfut.pe.dcu.comm.msg._44LogReq;
import cn.edu.hfut.pe.dcu.comm.msg._45TimeReq;
import cn.edu.hfut.pe.dcu.comm.msg._45TimeResp;
import cn.edu.hfut.pe.dcu.cons.DcuAttrCons;
import cn.edu.hfut.pe.dcu.service.IdcuMsgDealService;
import cn.edu.hfut.pe.dcu.util.LogDcuidUtil;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;

/**
 * 分发处理 前置代理服务器 的消息
 * 
 * @author Administrator
 *
 */
@Component
public class DcuDataServerDispatcher implements Dispatcher<Msg> {
	private final static Logger LOG = LoggerFactory.getLogger(DcuDataServerDispatcher.class);

	@Autowired
	private IdcuMsgDealService service;

	@Override
	public void process(final ChannelHandlerContext ctx, final Msg msg) {
		final Channel ch = ctx.channel();
		MON_DCU dcu = ch.attr(DcuAttrCons.DCU).get();
		Long dcuid = null;
		if(dcu != null) {
			dcuid = dcu.getDCU_ID();
		}

		
		
		/**
		 * 非指令异常返回，打印日志，丢弃
		 */
		if(msg instanceof MsgResp) {
			MsgResp mr = (MsgResp) msg;
			if(!mr.isOk()) {
				LOG.warn("modbus返回错误，ch:{}, dcuid:{}, bf:{}", ch, dcuid, mr);
				return;
			}
		}

		/**
		 * dcu登录
		 */
		if (msg instanceof _42LoginReq) {
			_42LoginReq req = (_42LoginReq) msg;
			_42LoginResp resp = new _42LoginResp(msg);
			if(!req.isCheckOk()) {
				LOG.warn("校验错误，ch:{}, dcuid:{}, bf:{}", ch, dcuid, req);
				resp.setResult(ExcepCode.ERROR_CHECK);
				ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
				return;
			}
			MON_DCU dcu0 = service.check(req);
			if (dcu0 == null) {
				resp.setResult(ExcepCode.ERROR_ID);
			} else {
				dcuid = dcu0.getDCU_ID();
				ch.attr(DcuAttrCons.DCU).set(dcu0);
				RunTimeCache.putChannelByDcuid(dcuid, ch);
//				if(dcu == null) {
//					EventLoop exe = ch.eventLoop();
//					byte unitId = 1;
//					short offset = 0;
//					short bitCount = 32;
//					short regCount = 110;
//					short coilsValue = 10;
//					boolean on = true;
//					int period = 60;
//					TimeUnit unit = TimeUnit.SECONDS;
//					_02YxReq req02 = _02YxReq.create_02YxReq(unitId, offset, bitCount);
//					exe.scheduleAtFixedRate(()->ctx.writeAndFlush(req02), 0, period, unit);
//					_03YcReq req03 = _03YcReq.create_03YcReq(unitId, offset, regCount);
//					exe.scheduleAtFixedRate(()->ctx.writeAndFlush(req03), 10, period, unit);
//					_05YkMsg cmd05 = _05YkMsg.create_05YkMsg(unitId, coilsValue, on);
//					exe.scheduleAtFixedRate(()->ctx.writeAndFlush(cmd05), 20, period, unit);
//					_06YtMsg cmd06 = _06YtMsg.create_06YtMsg(unitId, (byte)0x4C, (byte)2000);
//					exe.scheduleAtFixedRate(()->ctx.writeAndFlush(cmd06), 30, period, unit);
//					_44LogReq req44 = _44LogReq.create__44LogReq(unitId, offset, (byte)10);
//					exe.scheduleAtFixedRate(()->ctx.writeAndFlush(req44), 40, period, unit);
//				}
				
			}
			ctx.writeAndFlush(resp);
			return;
		}
		
		/**
		 * 未登录处理
		 */
		if (dcuid == null) {
			LOG.error("no login. {}, msg:{}", ctx.channel(), msg);
			ctx.close();
			return;
		}
		
		LogDcuidUtil.log(dcuid, LOG, "rec, dcuid:{}, msg:{}", dcuid, msg);
		/**
		 * 解析消息
		 */
		if(msg.isDown()) {
			dealDownMsg(ctx, msg);
			return;
		}
		
		if  (msg instanceof _45TimeReq) {
			_45TimeResp resp = new _45TimeResp(msg);
			ctx.writeAndFlush(resp);
			return;
		}
		
		if  (msg instanceof _02YxResp) {
			service.deal02YxData(dcuid, (_02YxResp)msg);
		} 
		
		else if (msg instanceof _03YcResp) {
			service.deal03YcData(dcuid, (_03YcResp)msg);
		} 
		
		else {
			LOG.warn("不可能出现，ch:{}, dcuid:{}, bf:{}", ch, dcuid, msg);
		}
	}

	private void dealDownMsg(ChannelHandlerContext ctx, Msg msg) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		System.out.println(NetUtil.intIpv4(2130706433));
	}
}
