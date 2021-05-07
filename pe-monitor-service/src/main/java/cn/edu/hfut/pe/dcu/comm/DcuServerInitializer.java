package cn.edu.hfut.pe.dcu.comm;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import base.util.BytesUtil;

/**
 * @author GJQ5233
 *
 */
public class DcuServerInitializer extends ChannelInitializer<SocketChannel> {
//	private final static Logger LOG = LoggerFactory.getLogger(DcuServerInitializer.class);

	private int readIdleTimeSeconds = 300;

	private int writerIdleTimeSeconds = 0;

	/**
	 * handler必须是可共享的@Sharable，成员变量必须是线程安全的
	 */
	private ChannelHandler userHandler;

	private final static DcuServerEncoder DcuServerEncoder = new DcuServerEncoder();
//	private final static ProtostuffMonitorDecoder protostuffMonitorDecoder = new ProtostuffMonitorDecoder();

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline p = ch.pipeline();
		// readIdleTimeSeconds秒关闭无数据连接，释放资源
		p.addFirst("idle", new IdleStateHandler(readIdleTimeSeconds, writerIdleTimeSeconds, 0));


		p.addLast("LengthFieldD", new LengthFieldBasedFrameDecoder(32 * 1024, 4, 2, 0, 0));

//		p.addLast("protostuffDecoder", protostuffMonitorDecoder);
//
//		if (logStrLen > 100) {
//			intLenFieldMonitorEncoder.setLogStrLen(logStrLen);
//			LOG.info("logStrLen value is:{}", logStrLen);
//		}
		p.addLast("dcuServerEncoder", DcuServerEncoder);

		// p.addLast("metric_encoder", new MetricEncoder());
		// p.addLast("log", LOG_HANDLER);

		p.addLast("userHandler", userHandler);
	}

	// private final static ChannelHandler LOG_HANDLER = new LoggingHandler(
	// LogLevel.DEBUG);

	public void setUserHandler(ChannelHandler userHandler) {
		this.userHandler = userHandler;
	}

	public void setReadIdleTimeSeconds(int readIdleTimeSeconds) {
		this.readIdleTimeSeconds = readIdleTimeSeconds;
	}

	public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
	}



	public static void main(String[] args) throws Throwable {
	System.out.println(BytesUtil.long2Str16(System.currentTimeMillis()));
	}
}