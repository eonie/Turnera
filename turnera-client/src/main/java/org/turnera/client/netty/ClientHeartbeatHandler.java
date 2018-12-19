package org.turnera.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.turnera.core.model.protobuf.PacketProto;

import static org.turnera.core.model.protobuf.PacketProto.Packet.newBuilder;

public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("--- Server is active ---");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("--- Server is inactive ---");

		// 10s 之后尝试重新连接服务器
		System.out.println("10s 之后尝试重新连接服务器...");
		Thread.sleep(10 * 1000);
		TurneraClient.doConnect();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
			sendHeartbeatPacket(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("连接出现异常");
	}

	/**
	 * 发送心跳包
	 *
	 * @param ctx
	 */
	private void sendHeartbeatPacket(ChannelHandlerContext ctx) {
		PacketProto.Packet.Builder builder = newBuilder();
		builder.setPacketType(PacketProto.Packet.PacketType.HEARTBEAT);
		PacketProto.Packet packet = builder.build();
		ctx.writeAndFlush(packet);
	}
}
