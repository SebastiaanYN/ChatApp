package com.sebastiaanyn.server;

import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final List<Channel> channels = new ArrayList<>();
    private final WebSocketServer server;

    WebSocketHandler(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            server.emit(new TextMessageEvent(ctx, (TextWebSocketFrame) frame, channels));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
    }
}
