package com.sebastiaanyn;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sebastiaanyn.eventhandler.events.ChannelDeleteEvent;
import com.sebastiaanyn.eventhandler.events.TextMessageEvent;
import com.sebastiaanyn.managers.RedisManager;
import com.sebastiaanyn.managers.SubscriptionManager;
import com.sebastiaanyn.requesthandler.Handler;
import com.sebastiaanyn.server.WebSocketServer;
import io.netty.channel.Channel;

public class Main {

    public static void main(String[] args) {
        RedisManager redis = new RedisManager("redisConfig.json");
        SubscriptionManager<Channel> channels = new SubscriptionManager<>(redis);
        Handler handler = new Handler(channels);

        WebSocketServer server = new WebSocketServer();

        server.on(TextMessageEvent.class, e -> {
            JsonElement json = new JsonParser().parse(e.frame.text());
            JsonElement type = json.getAsJsonObject().get("type");
            if (type == null) {
                return;
            }

            switch (type.getAsString()) {
                case "subscribe":
                    handler.handleSubscribe(e, json);
                    break;

                case "unsubscribe":
                    handler.handleUnsubscribe(e, json);
                    break;

                case "message":
                    handler.handleMessage(json);
                    break;

                case "typing_start":
                case "typing_end":
                    handler.handleTyping(json);
                    break;
            }
        });

        server.on(ChannelDeleteEvent.class, e -> channels.unsubscribe(e.ctx.channel()));

        server.listen(4000);
    }
}
