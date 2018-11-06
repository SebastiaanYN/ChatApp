package com.sebastiaanyn.eventhandler.events;

import com.sebastiaanyn.eventhandler.Event;
import io.netty.channel.ChannelHandlerContext;

public class ChannelDeleteEvent extends Event {

    public final ChannelHandlerContext ctx;

    public ChannelDeleteEvent(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
