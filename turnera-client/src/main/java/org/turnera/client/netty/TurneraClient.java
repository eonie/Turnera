package org.turnera.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.turnera.core.model.protobuf.PacketProto;

import java.util.Random;

import static org.turnera.core.model.protobuf.PacketProto.Packet.newBuilder;

public class TurneraClient {
	private static Channel ch;
	private static Bootstrap bootstrap;
	private static NioEventLoopGroup workGroup;

	private static String serverHost;
	private static int serverPort;
	public static boolean isActive;

	TurneraClient (String serverHost, int serverPort){
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

	public static void start() {
		workGroup = new NioEventLoopGroup();
		try {
			bootstrap = new Bootstrap();
			bootstrap
					.group(workGroup)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
							pipeline.addLast(new ProtobufEncoder());
							pipeline.addLast(new IdleStateHandler(0, 5, 0));
							pipeline.addLast(new ClientHeartbeatHandler());
						}
					});

			// 连接服务器
			doConnect();

			// 模拟不定时发送向服务器发送数据的过程
			Random random = new Random();
			while (true) {
				int num = random.nextInt(21);
				Thread.sleep(num * 1000);
				PacketProto.Packet.Builder builder = newBuilder();
				builder.setPacketType(PacketProto.Packet.PacketType.DATA);
				builder.setData("我是数据包(非心跳包)" + num);
				PacketProto.Packet packet = builder.build();
				ch.writeAndFlush(packet);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
		}
	}

	/**
	 * 抽取出该方法 (断线重连时使用)
	 *
	 * @throws InterruptedException
	 */
	public static void doConnect() throws InterruptedException {
		ch = bootstrap.connect(serverHost, serverPort).sync().channel();
		isActive = true;
	}

	public static Channel getChannel(){
		return ch;
	}

	public static void main(String[] args) {
		TurneraClient turneraClient = new TurneraClient("localhost", 20000);
		turneraClient.start();
	}
}
