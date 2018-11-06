package com.sebastiaanyn.eventhandler.events;

import com.sebastiaanyn.eventhandler.Event;
import org.redisson.client.ChannelName;

public class RedisMessageEvent<T> extends Event {

    public final ChannelName channel;
    public final T obj;

    public RedisMessageEvent(ChannelName channel, T obj) {
        this.channel = channel;
        this.obj = obj;
    }
}
