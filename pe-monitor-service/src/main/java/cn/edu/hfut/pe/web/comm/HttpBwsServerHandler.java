//
//package cn.edu.hfut.pe.web.comm;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandler.Sharable;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.DefaultFullHttpResponse;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.FullHttpResponse;
//import io.netty.handler.codec.http.HttpHeaderNames;
//import io.netty.handler.codec.http.HttpUtil;
//import io.netty.handler.codec.http.HttpHeaderValues;
//import io.netty.handler.codec.http.HttpMessage;
//import io.netty.handler.timeout.IdleState;
//import io.netty.handler.timeout.IdleStateEvent;
//import io.netty.util.CharsetUtil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.kdgz.monitor.pe2.B.dto.PKTypeCons;
//import com.kdgz.monitor.pe2.B.service.IlscService;
//import com.kdgz.monitor.pe2.B.util.SoapCons;
//import com.kdgz.monitor.pe2.B.util.XmlUtil;
//
//import static io.netty.handler.codec.http.HttpResponseStatus.*;
//import static io.netty.handler.codec.http.HttpVersion.*;
//
//@Sharable
//public class HttpBwsServerHandler extends SimpleChannelInboundHandler<Object> {
//	private final static Logger LOG = LoggerFactory.getLogger(HttpBwsServerHandler.class);
//
//	private IlscService lscService;
//
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
//		if (msg instanceof FullHttpRequest) {
//			FullHttpRequest httpContent = (FullHttpRequest) msg;
//			ByteBuf content = httpContent.content();
//			String soapXml = (String) httpContent.content().readCharSequence(content.readableBytes(),
//					CharsetUtil.UTF_8);
//
//			LOG.trace("{} rec:{}", ctx.channel(), soapXml);
//			String reqestXml = SoapCons.extractSoapXml(soapXml);
//			LOG.warn("{} rec:{}", ctx.channel(), reqestXml);
//			String ack = getXmlAck(reqestXml);
////			String ack = SoapCons.getWrapLoginAck();
//			LOG.warn("{} ack:{}", ctx.channel(), ack);
//			ack = SoapCons.wrapAckSoapXml(ack);
//			writeResponse(httpContent, ack, ctx);
//		}
//	}
//
//	public String getXmlAck(String xmlReqest) {
//
//		if (xmlReqest == null) {
//			return null;// xml实现
//		}
//		String pktype = XmlUtil.getPkStr(xmlReqest);
//		if (pktype == null) {
//			return null;// xml实现
//		}
//		switch (pktype) {
//		case PKTypeCons.PK_LOGIN:
//			return lscService.ackLogin(xmlReqest);
//		case PKTypeCons.PK_SEND_ALARM:
//			LOG.info("alarm:{}", xmlReqest);
//			return lscService.ackAlarm(xmlReqest);
//		case PKTypeCons.PK_SEND_DEV_CONF_DATA:
//			return lscService.ackDevConf(xmlReqest);
//		default:
//			break;
//		}
//		LOG.warn("Protocol not supported, req:{}", xmlReqest);
//		return "<Response><PK_Type><Name>" + pktype + "_ACK</Name>"
//				+ "</PK_Type><Info><Result>0</Result><FailureCause>不支持的指令</FailureCause></Info></Response>";
//	}
//
//	private boolean writeResponse(HttpMessage request, String ack, ChannelHandlerContext ctx) {
//		// Build the response object.
//		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
//				Unpooled.copiedBuffer(ack, CharsetUtil.UTF_8));
//
//		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//
//		// Decide whether to close the connection or not.
//		boolean keepAlive = HttpUtil.isKeepAlive(request);
//		if (keepAlive) {
//			// Add 'Content-Length' header only for a keep-alive connection.
//			response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
//			// Add keep alive header as per:
//			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//		}
//
//		// Write the response.
//		ctx.writeAndFlush(response);
//
//		return keepAlive;
//	}
//
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		final Channel channel = ctx.channel();
//		LOG.info("{} active", channel);
//	}
//
//	@Override
//	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
//		LOG.info("{} inactive", ctx.channel());
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		LOG.warn("{} exception:{}", ctx.channel(), cause);
//		ctx.close();
//	}
//
//	@Override
//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//		LOG.info("{} event:{}", ctx.channel(), evt);
//		if (!(evt instanceof IdleStateEvent)) {
//			return;
//		}
//		IdleStateEvent e = (IdleStateEvent) evt;
//		if (e.state() == IdleState.READER_IDLE) {// 超时未收到数据
//			// The connection was OK but there was no traffic for last period.
//			LOG.info("{} READER_IDLE", ctx.channel());
//			ctx.close();
//		} else {
//			LOG.warn("{} event:{}", ctx.channel(), evt);
//		}
//	}
//
//	public void setLscService(IlscService lscService) {
//		this.lscService = lscService;
//	}
//
//}
