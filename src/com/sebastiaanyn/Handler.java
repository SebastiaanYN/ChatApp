package com.sebastiaanyn;

import com.google.gson.*;
import com.sebastiaanyn.schemas.Message;
import com.sebastiaanyn.schemas.Schemas;
import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

class Handler {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    static void handleMessage(TextMessageEvent e, JsonElement json) {
        Message message = GSON.fromJson(json, Schemas.getSchema("message"));

        JsonObject object = new JsonObject();
        object.addProperty("author", message.author);
        object.addProperty("content", message.content);

        TextWebSocketFrame frame = new TextWebSocketFrame(GSON.toJson(object));
        e.channels.forEach(c -> c.writeAndFlush(frame.copy()));
    }
}
