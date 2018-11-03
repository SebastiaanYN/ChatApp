package com.sebastiaanyn;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sebastiaanyn.managers.SubscribtionManager;
import com.sebastiaanyn.server.WebSocketServer;
import com.sebastiaanyn.server.eventhandler.events.ChannelDeleteEvent;
import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;
import io.netty.channel.Channel;

public class Main {

    public static void main(String[] args) {
        WebSocketServer server = new WebSocketServer();
        SubscribtionManager<Channel> channels = new SubscribtionManager<>();
        Handler handler = new Handler(channels);

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
            }
        });

        server.on(ChannelDeleteEvent.class, e -> channels.unsubscribe(e.ctx.channel()));

        server.listen(4000);
    }
}
