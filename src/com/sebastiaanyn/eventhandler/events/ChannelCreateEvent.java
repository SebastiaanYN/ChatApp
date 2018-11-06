package com.sebastiaanyn.eventhandler.events;

import com.sebastiaanyn.eventhandler.Event;
import io.netty.channel.ChannelHandlerContext;

public class ChannelCreateEvent extends Event {

    public final ChannelHandlerContext ctx;

    public ChannelCreateEvent(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
