package cn.edu.hfut.pe.dcu.comm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import base.comm.Dispatcher;
import base.util.BytesUtil;
import cn.edu.hfut.pe.dcu.comm.msg.ExcepCode;
import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.dcu.comm.msg.MsgResp;
import cn.edu.hfut.pe.dcu.cons.DcuAttrCons;
import cn.edu.hfut.pe.dcu.util.LogDcuidUtil;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;

/**
 * 处理数据同步请求
 *
 * @author GJQ
 * @version
 * @since JDK 1.8
 */
@Sharable
public class DcuDataTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private final static Logger LOG = LoggerFactory.getLogger(DcuDataTcpServerHandler.class);

	/**
	 * 消息分发
	 */
	@Autowired
	private Dispatcher<Msg> dispatcher;

	/**
	 * 消息分发
	 */
	public final static AtomicInteger MSG_COUNT = new AtomicInteger();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.warn("{} active", ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.warn("{} inactive", ctx.channel());
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, ByteBuf bf) throws Exception {

		Channel ch = ctx.channel();
		MON_DCU dcu = ch.attr(DcuAttrCons.DCU).get();
		Long dcuid = null;
		if (dcu != null) {
			dcuid = dcu.getDCU_ID();
		}

		if (LogDcuidUtil.trace) {
			LOG.trace("rec, ch:{}, dcuid:{}, msg:{}", ch, dcuid, BytesUtil.hexDump(bf));
		}

		try {
			/**
			 * 心跳处理
			 */
			if (Msg.isHeartbeat(bf)) {// 心跳
				ctx.writeAndFlush(bf.retain());
				return;
			}

			/**
			 * 打印日志， 原始消息
			 */
			if (Msg.isNeedLog(bf)) {
				LOG.info("rec, ch:{}, dcuid:{}, bf:{}", ch, dcuid, BytesUtil.hexDump(bf));
			} else {
				if (LogDcuidUtil.isLogDcu(dcuid)) {
					LogDcuidUtil.log(LOG, "rec, ch:{}, dcuid:{}, bf:{}", ch, dcuid, BytesUtil.hexDump(bf));
				}
			}

			/**
			 * 消息解析
			 */
			byte cmd = Msg.getCmd(bf);
			Msg mm = Msg.parseMsg(bf);
			if (mm == null) {// 不支持的指令
				LOG.warn("不支持的指令，rec, ch:{}, dcuid:{}, bf:{}", ch, dcuid, BytesUtil.hexDump(bf));
				if (Msg.isNeedAck(cmd)) {
					mm = new Msg();
					mm.parse(bf);
					MsgResp resp = new MsgResp(mm);
					resp.setExcepCode(ExcepCode.ERROR_FUNC);
					ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
				}
				return;
			}

			/**
			 * 打印日志， 消息解析后
			 */
			if (Msg.isNeedLog(cmd)) {
				LOG.info("rec, ch:{}, dcuid:{}, msg:{}", ch, dcuid, mm);
			} else {
				if (LogDcuidUtil.isLogDcu(dcuid)) {
					LogDcuidUtil.log(LOG, "rec, ch:{}, dcuid:{}, msg:{}", ch, dcuid, mm);
				}
			}

			dispatcher.process(ctx, mm);

		} catch (Throwable e) {
			LOG.warn("rec, ch:{}, dcuid:{}, msg:{}, e:{}", ch, dcuid, BytesUtil.hexDump(bf), e);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		LOG.warn("rec, {} event:{}", ctx.channel(), evt);
		if (evt instanceof IdleStateEvent) {
			if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {// 读超时，关闭连接
				LOG.info("{} read idle.", ctx.channel());
				ctx.close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.warn("{} exception:{}", ctx.channel(), cause);
		ctx.close();
	}

	public void setDispatcher(Dispatcher<Msg> dispatcher) {
		this.dispatcher = dispatcher;
	}

	public static void main(String[] args) {
		System.setProperty("fastjson.compatibleWithJavaBean", "true");

	}
}
