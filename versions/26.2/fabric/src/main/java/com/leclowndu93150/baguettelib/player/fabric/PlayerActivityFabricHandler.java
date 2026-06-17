package com.leclowndu93150.baguettelib.player.fabric;

import com.leclowndu93150.baguettelib.Constants;
import com.leclowndu93150.baguettelib.player.OfflinePlayerStats;
import com.leclowndu93150.baguettelib.player.PlayerActivityTracker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerActivityFabricHandler {

    private PlayerActivityFabricHandler() {}

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> mark(server, listener.player));
        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
            mark(server, listener.player);
            OfflinePlayerStats.invalidate(listener.player.getUUID());
        });
    }

    private static void mark(MinecraftServer server, ServerPlayer player) {
        if (server == null || player == null) return;
        PlayerActivityTracker tracker = PlayerActivityTracker.get(server);
        tracker.markSeen(player);
        Constants.LOG.debug("PlayerActivityTracker marked {} seen", player.getUUID());
    }
}
