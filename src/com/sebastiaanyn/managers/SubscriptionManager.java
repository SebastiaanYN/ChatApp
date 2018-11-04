package com.sebastiaanyn.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

public class SubscriptionManager<T> {

    private final Multimap<Long, T> channels = ArrayListMultimap.create();

    public void subscribe(T channel, List<Long> ids) {
        ids.forEach(id -> subscribe(channel, id));
    }

    public void subscribe(T channel, long id) {
        if (!channels.containsEntry(id, channel)) {
            channels.put(id, channel);
        }
    }

    public void unsubscribe(T channel, List<Long> ids) {
        ids.forEach(id -> unsubscribe(channel, id));
    }

    public void unsubscribe(T channel, long id) {
        channels.remove(id, channel);
    }

    public void unsubscribe(T channel) {
        channels.values().removeIf(channel::equals);
    }

    public Collection<T> getChannels(long id) {
        return channels.get(id);
    }
}
