package org.turnera.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turnera.core.model.protobuf.PacketProto;

import java.net.InetSocketAddress;

public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(ServerHeartbeatHandler.class);
	// 心跳丢失计数器
	private int counter;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("--- Client is active ---");
		InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		System.out.println(inetSocketAddress.getPort());
		System.out.println(inetSocketAddress.getAddress());
		System.out.println(inetSocketAddress.getHostName());

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("--- Client is inactive ---");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		// 判断接收到的包类型
		if (msg instanceof PacketProto.Packet) {
			PacketProto.Packet packet = (PacketProto.Packet) msg;

			switch (packet.getPacketType()) {
				case HEARTBEAT:
					handleHeartbreat(ctx, packet);
					break;

				case DATA:
					handleData(ctx, packet);
					break;

				default:
					break;
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// 空闲6s之后触发 (心跳包丢失)
			if (counter >= 3) {
				// 连续丢失3个心跳包 (断开连接)
				ctx.channel().close().sync();
				logger.info("已与Client断开连接");
			} else {
				counter++;
				logger.info("丢失了第 " + counter + " 个心跳包");
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("连接出现异常");
	}

	/**
	 * 处理心跳包
	 *
	 * @param ctx
	 * @param packet
	 */
	private void handleHeartbreat(ChannelHandlerContext ctx, PacketProto.Packet packet) {
		// 将心跳丢失计数器置为0
		counter = 0;
		InetSocketAddress inetSocketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		logger.debug("收到" + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() + "心跳包");
		ReferenceCountUtil.release(packet);
	}

	/**
	 * 处理数据包
	 *
	 * @param ctx
	 * @param packet
	 */
	private void handleData(ChannelHandlerContext ctx, PacketProto.Packet packet) {
		// 将心跳丢失计数器置为0
		counter = 0;
		String data = packet.getData();
		logger.info(data);
		ReferenceCountUtil.release(packet);
	}
}
