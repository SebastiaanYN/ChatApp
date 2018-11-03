package com.sebastiaanyn.server.eventhandler.events;

import com.sebastiaanyn.server.eventhandler.Event;
import io.netty.channel.ChannelHandlerContext;

public class ChannelDeleteEvent extends Event {

    public final ChannelHandlerContext ctx;

    public ChannelDeleteEvent(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
