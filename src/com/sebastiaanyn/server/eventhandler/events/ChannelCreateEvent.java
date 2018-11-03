package com.sebastiaanyn.server.eventhandler.events;

import com.sebastiaanyn.server.eventhandler.Event;
import io.netty.channel.ChannelHandlerContext;

public class ChannelCreateEvent extends Event {

    public final ChannelHandlerContext ctx;

    public ChannelCreateEvent(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
