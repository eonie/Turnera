package org.turnera.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.turnera.core.model.protobuf.PacketProto;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@ConfigurationProperties(prefix = "turnera")
public class TurneraServer {
	private static Logger logger = LoggerFactory.getLogger(TurneraServer.class);
	/**
	 * boss 线程组用于处理连接工作
	 */
	private EventLoopGroup acceptorGroup = new NioEventLoopGroup();
	/**
	 * work 线程组用于数据处理
	 */
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private int port = 20000;

	public TurneraServer(int port) {
		this.port = port;
	}
	public TurneraServer() {}

	/**
	 * 启动Netty Server
	 *
	 * @throws InterruptedException
	 */
	@PostConstruct
	public void start() throws Exception {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
					.group(acceptorGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ProtobufVarint32FrameDecoder());
							pipeline.addLast(new ProtobufDecoder(PacketProto.Packet.getDefaultInstance()));
							pipeline.addLast(new IdleStateHandler(6, 0, 0));
							pipeline.addLast(new ServerHeartbeatHandler());
						}
					});
//			Channel ch = bootstrap.bind(port).sync().channel();
//			ch.closeFuture().sync();
			ChannelFuture future = bootstrap.bind(port).sync();
			if (future.isSuccess()) {
				logger.info("Netty Server started");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void destory() throws InterruptedException {
		acceptorGroup.shutdownGracefully().sync();
		workerGroup.shutdownGracefully().sync();
		logger.info("关闭Netty");
	}

	public static void main(String[] args) throws Exception {
		TurneraServer turneraServer = new TurneraServer(20000);
		turneraServer.start();
	}
}
