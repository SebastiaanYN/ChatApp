package com.sebastiaanyn.requesthandler;

import com.google.gson.*;
import com.sebastiaanyn.eventhandler.events.RedisMessageEvent;
import com.sebastiaanyn.eventhandler.events.TextMessageEvent;
import com.sebastiaanyn.managers.SubscriptionManager;
import com.sebastiaanyn.schemas.*;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.redisson.client.ChannelName;

public class Handler {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private final SubscriptionManager<Channel> channels;

    public Handler(SubscriptionManager<Channel> channels) {
        this.channels = channels;

        channels.redis().on(RedisMessageEvent.class, e -> {
            if (e.obj instanceof MessageSchema) {
                broadcastMessage(e.channel, (MessageSchema) e.obj);
            } else if (e.obj instanceof TypingSchema) {
                broadcastTyping(e.channel, (TypingSchema) e.obj);
            }
        });
    }

    public void handleSubscribe(TextMessageEvent e, JsonElement json) {
        SubscribeSchema subscribe = GSON.fromJson(json, Schemas.getSchema("subscribe"));
        channels.subscribe(e.ctx.channel(), "chat:" + subscribe.id);
    }

    public void handleUnsubscribe(TextMessageEvent e, JsonElement json) {
        UnsubscribeSchema unsubscribe = GSON.fromJson(json, Schemas.getSchema("unsubscribe"));
        channels.unsubscribe(e.ctx.channel(), "chat:" + unsubscribe.id);
    }

    public void handleMessage(JsonElement json) {
        MessageSchema message = GSON.fromJson(json, Schemas.getSchema("message"));
        channels.redis().publish("chat:" + message.id, message);
    }

    public void handleTyping(JsonElement json) {
        TypingSchema typing = GSON.fromJson(json, Schemas.getSchema("typing"));
        channels.redis().publish("chat:" + typing.id, typing);
    }

    private void broadcastMessage(ChannelName channel, MessageSchema message) {
        JsonObject object = new JsonObject();
        object.addProperty("type", message.type);
        object.addProperty("author", Utils.escapeHtml(message.author));
        object.addProperty("content", Utils.escapeHtml(message.content));

        TextWebSocketFrame frame = new TextWebSocketFrame(GSON.toJson(object));
        channels.getChannels(channel.toString()).forEach(c -> c.writeAndFlush(frame.copy()));
    }

    private void broadcastTyping(ChannelName channel, TypingSchema typing) {
        JsonObject object = new JsonObject();
        object.addProperty("type", typing.type);
        object.addProperty("author", Utils.escapeHtml(typing.author));

        TextWebSocketFrame frame = new TextWebSocketFrame(GSON.toJson(object));
        channels.getChannels(channel.toString()).forEach(c -> c.writeAndFlush(frame.copy()));
    }
}
