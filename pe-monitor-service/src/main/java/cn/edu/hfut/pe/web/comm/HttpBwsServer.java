
package cn.edu.hfut.pe.web.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * An HTTP server that sends back the content of the received HTTP request in a
 * pretty plaintext form.
 */
public final class HttpBwsServer extends Thread {
	private final static Logger LOG = LoggerFactory.getLogger(HttpBwsServer.class);
	static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

	private ChannelHandler initializerHandler;
	private int port = 8080;

	@Override
	public void run() {
		this.setName("CMB_LSC_SERVER");
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(initializerHandler);

			Channel ch = null;
			try {
				LOG.warn("LSC Server Start on 8080 port");
				ch = b.bind(port).sync().channel();
				ch.closeFuture().sync();
			} catch (Throwable e) {
				LOG.error("LSC Server Start Error. {}", e);
			}
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void setInitializerHandler(ChannelHandler initializerHandler) {
		this.initializerHandler = initializerHandler;
	}

	public static void main(String[] args) throws Exception {

		new HttpBwsServer().start();
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
