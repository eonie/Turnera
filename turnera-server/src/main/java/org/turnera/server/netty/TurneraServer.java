package org.turnera.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.turnera.core.model.protobuf.PacketProto;


public class TurneraServer {

	private final int port;

	public TurneraServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		NioEventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
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
			Channel ch = bootstrap.bind(port).sync().channel();
			System.out.println("Server has started...");
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			acceptorGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		TurneraServer turneraServer = new TurneraServer(20000);
		turneraServer.start();
	}
}
