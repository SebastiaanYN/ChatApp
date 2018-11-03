package com.sebastiaanyn.server.eventhandler.events;

import com.sebastiaanyn.server.eventhandler.Event;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class TextMessageEvent extends Event {

    public final ChannelHandlerContext ctx;
    public final TextWebSocketFrame frame;
    public final List<Channel> channels;

    public TextMessageEvent(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Channel> channels) {
        this.ctx = ctx;
        this.frame = frame;
        this.channels = channels;
    }
}
