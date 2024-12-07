package com.shootforever.nuclear.module.modules.misc;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;

public class IRC extends Module {
    public IRC() {
        super("IRC", Category.MISC);
    }

    @Override
    protected void onEnable() {
        setEnabled(false);
    }
}

/*
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.ChatEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.functions.NotifyUtil;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Nullable;

import javax.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class IRC extends Module {
    private final List<String> users = new ArrayList<>();
    private @Nullable Session session = null;

    public IRC() {
        super("IRC", Category.MISC);
    }

    @Override
    protected void onEnable() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(new WebSocketClient(), URI.create(Nuclear.SERVER_IP));
        } catch (Exception e) {
            e.printStackTrace();
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC连接服务器失败");
        }
    }

    @Override
    protected void onDisable() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventTarget
    public void onChat(ChatEvent event) {
        if (event.getMessage().trim().charAt(0) != '#') {
            return;
        }

        event.setCancelled(true);

        if (session == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC发送消息失败，原因：暂未连接服务器");
            return;
        }

        String content = event.getMessage().trim().substring(1).trim();
        if (content.isEmpty()) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC发送消息失败，原因：内容为空");
            return;
        }

        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("func", "send_msg");
        messageJson.addProperty("msg", content);
        try {
            session.getBasicRemote().sendText(messageJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventTarget
    public void on( event) {
        if (mc.player == null || session == null) return;

        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("func", "remove_user");
        try {
            session.getBasicRemote().sendText(messageJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageJson = new JsonObject();
        messageJson.addProperty("func", "create_user");
        messageJson.addProperty("server", event.getIp());
        messageJson.addProperty("name", mc.player.getScoreboardName());
        try {
            session.getBasicRemote().sendText(messageJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @Nullable List<String> getUsers() {
        return isEnabled() ? new ArrayList<>(users) : null;
    }

    @ClientEndpoint
    private class WebSocketClient {
        @OnOpen
        public void onOpen(Session session) {
            NotifyUtil.notifyAsMessage("IRC连接服务器成功");

            JsonObject messageJson = new JsonObject();
            messageJson.addProperty("func", "get_free");
            try {
                session.getBasicRemote().sendText(messageJson.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mc.getCurrentServer() != null) {
                on(new (mc.getCurrentServer().ip));
            }
        }

        @OnMessage
        public void onMessage(Session session, String message) {
            JsonObject messageJson = JsonParser.parseString(message).getAsJsonObject();

            switch (messageJson.get("func").getAsString()) {
                case "get_free":
                    Nuclear.getInstance().setFree(messageJson.get("free").getAsBoolean());
                    break;

                case "send_msg":
                    NotifyUtil.notifyAsMessage(ChatFormatting.GREEN + "IRC"
                            + ChatFormatting.WHITE + " | "
                            + ChatFormatting.GREEN + messageJson.get("name").getAsString()
                            + ChatFormatting.WHITE + ": " + messageJson.get("msg").getAsString());
                    break;

                case "create_user":
                    users.add(messageJson.get("name").getAsString());
                    break;

                case "remove_user":
                    users.remove(messageJson.get("name").getAsString());
                    break;
            }
        }

        @OnClose
        public void onClose(Session session) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC与服务器断开连接，自动关闭IRC");
            setEnabled(false);
        }

        @OnError
        public void onError(Session session, Throwable error) {
            error.printStackTrace();
            if (session == null) {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC连接服务器失败，自动关闭IRC");
            } else {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "IRC与服务器断开连接，自动关闭IRC");
            }
            setEnabled(false);
        }
    }
}
*/