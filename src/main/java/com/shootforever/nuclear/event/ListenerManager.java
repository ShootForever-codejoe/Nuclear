package com.shootforever.nuclear.event;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.listener.RotationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    private final List<@NotNull Listener> listeners = new ArrayList<>();

    public ListenerManager() {
        registerListeners(
                new RotationListener()
        );
        startAllListeners();
    }

    private void registerListeners(@NotNull Listener @NotNull ... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }

    private void registerListener(@NotNull Listener listener) {
        listeners.add(listener);
    }

    public void startAllListeners() {
        for (Listener listener : listeners) {
            Nuclear.getInstance().getEventManager().register(listener);
        }
    }

    public void stopAllListeners() {
        for (Listener listener : listeners) {
            Nuclear.getInstance().getEventManager().unregister(listener);
        }
    }

    public void startListener(@NotNull String name) {
        for (Listener listener : listeners) {
            if (name.equalsIgnoreCase(listener.getName())) {
                Nuclear.getInstance().getEventManager().register(listener);
            }
        }
    }

    public void stopListener(@NotNull String name) {
        for (Listener listener : listeners) {
            if (name.equalsIgnoreCase(listener.getName())) {
                Nuclear.getInstance().getEventManager().unregister(listener);
            }
        }
    }

    public @Nullable Listener getListener(@NotNull String name) {
        for (Listener listener : listeners) {
            if (name.equalsIgnoreCase(listener.getName())) {
                return listener;
            }
        }
        return null;
    }

    public List<Listener> getListeners() {
        return new ArrayList<>(listeners);
    }
}
