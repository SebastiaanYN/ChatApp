package com.sebastiaanyn.managers;

import com.sebastiaanyn.eventhandler.EventHandler;
import com.sebastiaanyn.eventhandler.events.RedisMessageEvent;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.ChannelName;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RedisManager extends EventHandler {

    private final Map<String, RTopic> topics = new HashMap<>();
    private final RedissonClient redis;

    public RedisManager(String path) {
        Config config = null;
        try {
            config = Config.fromJSON(new File(path));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        redis = Redisson.create(config);
    }

    @SuppressWarnings("unchecked")
    <T> void sub(String channel) {
        RTopic topic = redis.getTopic(channel);
        topics.put(channel, topic);
        topic.addListener(RedisMessage.class, (c, s) -> emit(new RedisMessageEvent<>((ChannelName) c, (T) s.obj)));
    }

    void unsub(String channel) {
        RTopic topic = topics.remove(channel);
        if (topic != null) {
            topic.removeAllListeners();
        }
    }

    public long publish(String channel, Object o) {
        RTopic topic = topics.get(channel);
        if (topic == null) {
            topic = redis.getTopic(channel);
        }
        return topic.publish(new RedisMessage<>(o));
    }

    void unsubIfAbsent(Collection<String> keys) {
        topics.keySet().forEach(key -> {
            if (!keys.contains(key)) {
                unsub(key);
            }
        });
    }
}