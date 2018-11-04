package com.sebastiaanyn.requesthandler;

import com.google.gson.*;
import com.sebastiaanyn.managers.SubscriptionManager;
import com.sebastiaanyn.schemas.*;
import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Handler {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private final SubscriptionManager<Channel> channels;

    public Handler(SubscriptionManager<Channel> channels) {
        this.channels = channels;
    }

    public void handleSubscribe(TextMessageEvent e, JsonElement json) {
        SubscribeSchema subscribe = GSON.fromJson(json, Schemas.getSchema("subscribe"));
        channels.subscribe(e.ctx.channel(), subscribe.id);
    }

    public void handleUnsubscribe(TextMessageEvent e, JsonElement json) {
        UnsubscribeSchema unsubscribe = GSON.fromJson(json, Schemas.getSchema("unsubscribe"));
        channels.unsubscribe(e.ctx.channel(), unsubscribe.id);
    }

    public void handleMessage(JsonElement json) {
        MessageSchema message = GSON.fromJson(json, Schemas.getSchema("message"));

        JsonObject object = new JsonObject();
        object.addProperty("type", "message");
        object.addProperty("author", Utils.escapeHtml(message.author));
        object.addProperty("content", Utils.escapeHtml(message.content));

        TextWebSocketFrame frame = new TextWebSocketFrame(GSON.toJson(object));
        channels.getChannels(message.id).forEach(c -> c.writeAndFlush(frame.copy()));
    }

    public void handleTyping(TextMessageEvent e, JsonElement json, String type) {
        TypingSchema typing = GSON.fromJson(json, Schemas.getSchema("typing"));

        JsonObject object = new JsonObject();
        object.addProperty("type", type);
        object.addProperty("author", Utils.escapeHtml(typing.author));

        TextWebSocketFrame frame = new TextWebSocketFrame(GSON.toJson(object));
        channels.getChannels(typing.id).stream()
                .filter(c -> c != e.ctx.channel())
                .forEach(c -> c.writeAndFlush(frame.copy()));
    }
}
