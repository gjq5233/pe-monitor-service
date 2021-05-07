package cn.edu.hfut.pe.web.comm;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

public class HttpBwsServerInitializer extends ChannelInitializer<SocketChannel> {
	private ChannelHandler handler;

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addFirst("idle", new IdleStateHandler(300, 0, 0));
        p.addLast(new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
//        p.addLast(new HttpContentCompressor());
//        p.addLast(new HttpBwsServerHandler());
        p.addLast(handler);
        
    }

	public void setHandler(ChannelHandler handler) {
		this.handler = handler;
	}
    
}
