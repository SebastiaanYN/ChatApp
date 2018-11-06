package com.sebastiaanyn.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

public class SubscriptionManager<T> {

    private final Multimap<String, T> channels = ArrayListMultimap.create();

    private final RedisManager redis;

    public SubscriptionManager(RedisManager redis) {
        this.redis = redis;
    }

    public RedisManager redis() {
        return redis;
    }

    public void subscribe(T channel, String id) {
        if (!channels.containsKey(id)) {
            redis.sub(id);
        }

        if (!channels.containsEntry(id, channel)) {
            channels.put(id, channel);
        }
    }

    public void unsubscribe(T channel, String id) {
        channels.remove(id, channel);

        if (!channels.containsKey(id)) {
            redis.unsub(id);
        }
    }

    public void unsubscribe(T channel) {
        channels.values().removeIf(channel::equals);

        redis.unsubIfAbsent(channels.keys());
    }

    public Collection<T> getChannels(String id) {
        return channels.get(id);
    }
}
