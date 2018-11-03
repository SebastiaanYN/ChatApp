package com.sebastiaanyn;

import com.google.gson.*;
import com.sebastiaanyn.server.WebSocketServer;
import com.sebastiaanyn.server.eventhandler.events.TextMessageEvent;

public class Main {

    public static void main(String[] args) {
        WebSocketServer server = new WebSocketServer();

        server.on(TextMessageEvent.class, e -> {
            JsonElement json = new JsonParser().parse(e.frame.text());
            JsonElement type = json.getAsJsonObject().get("type");
            if (type == null) {
                return;
            }

            switch(type.getAsString()) {
                case "message":
                    Handler.handleMessage(e, json);
                    break;
            }
        });

        server.listen(4000);
    }
}
