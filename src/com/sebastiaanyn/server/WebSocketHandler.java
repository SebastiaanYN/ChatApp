package com.sebastiaanyn.server;

import com.sebastiaanyn.server.eventhandler.events.ChannelCreateEvent;
import com.sebastiaanyn.server.eventhandler.events.ChannelDeleteEvent;
import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final WebSocketServer server;

    WebSocketHandler(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            server.emit(new TextMessageEvent(ctx, (TextWebSocketFrame) frame));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        server.emit(new ChannelCreateEvent(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        server.emit(new ChannelDeleteEvent(ctx));
    }
}
