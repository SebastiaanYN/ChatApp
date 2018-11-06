package com.sebastiaanyn.eventhandler.events;

import com.sebastiaanyn.eventhandler.Event;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextMessageEvent extends Event {

    public final ChannelHandlerContext ctx;
    public final TextWebSocketFrame frame;

    public TextMessageEvent(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        this.ctx = ctx;
        this.frame = frame;
    }
}
