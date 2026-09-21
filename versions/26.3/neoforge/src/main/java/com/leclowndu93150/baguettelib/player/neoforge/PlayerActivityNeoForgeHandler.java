package com.leclowndu93150.baguettelib.player.neoforge;

import com.leclowndu93150.baguettelib.Constants;
import com.leclowndu93150.baguettelib.player.OfflinePlayerStats;
import com.leclowndu93150.baguettelib.player.PlayerActivityTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class PlayerActivityNeoForgeHandler {

    private PlayerActivityNeoForgeHandler() {}

    public static void register(IEventBus gameBus) {
        gameBus.addListener(PlayerActivityNeoForgeHandler::onLogin);
        gameBus.addListener(PlayerActivityNeoForgeHandler::onLogout);
    }

    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        mark(event.getEntity());
    }

    private static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        mark(player);
        OfflinePlayerStats.invalidate(player.getUUID());
    }

    private static void mark(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        MinecraftServer server = serverPlayer.level().getServer();
        if (server == null) return;
        PlayerActivityTracker tracker = PlayerActivityTracker.get(server);
        tracker.markSeen(serverPlayer);
        Constants.LOG.debug("PlayerActivityTracker marked {} seen", serverPlayer.getUUID());
    }
}
