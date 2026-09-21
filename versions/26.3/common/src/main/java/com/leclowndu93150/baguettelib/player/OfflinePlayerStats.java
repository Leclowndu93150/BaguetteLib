package com.leclowndu93150.baguettelib.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class OfflinePlayerStats {

    private static final ConcurrentHashMap<UUID, ServerStatsCounter> OFFLINE_CACHE = new ConcurrentHashMap<>();

    private final UUID uuid;
    private final String name;
    private final ServerStatsCounter stats;
    private final MinecraftServer server;
    private final ServerPlayer onlinePlayer;

    private OfflinePlayerStats(UUID uuid, String name, ServerStatsCounter stats, MinecraftServer server, ServerPlayer onlinePlayer) {
        this.uuid = uuid;
        this.name = name;
        this.stats = stats;
        this.server = server;
        this.onlinePlayer = onlinePlayer;
    }

    public static OfflinePlayerStats of(ServerPlayer player) {
        return new OfflinePlayerStats(
                player.getUUID(),
                player.getGameProfile().name(),
                player.getStats(),
                player.level().getServer(),
                player
        );
    }

    public static Optional<OfflinePlayerStats> of(MinecraftServer server, UUID uuid) {
        ServerPlayer online = server.getPlayerList().getPlayer(uuid);
        if (online != null) {
            return Optional.of(of(online));
        }
        NameAndId nameAndId = server.services().nameToIdCache().get(uuid).orElse(null);
        String name = nameAndId != null ? nameAndId.name() : uuid.toString();
        ServerStatsCounter counter = loadOfflineStats(server, uuid);
        if (counter == null) {
            return Optional.empty();
        }
        return Optional.of(new OfflinePlayerStats(uuid, name, counter, server, null));
    }

    private static ServerStatsCounter loadOfflineStats(MinecraftServer server, UUID uuid) {
        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
        Path file = statsDir.resolve(uuid + ".json");
        if (!Files.isRegularFile(file)) {
            return null;
        }
        return OFFLINE_CACHE.computeIfAbsent(uuid, id -> new ServerStatsCounter(server, file));
    }

    public static void invalidate(UUID uuid) {
        OFFLINE_CACHE.remove(uuid);
    }

    public static void invalidateAll() {
        OFFLINE_CACHE.clear();
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public ServerStatsCounter stats() {
        return stats;
    }

    public int getValue(Stat<?> stat) {
        return stats.getValue(stat);
    }

    public MinecraftServer server() {
        return server;
    }

    public boolean isOnline() {
        return onlinePlayer != null;
    }

    public ServerPlayer onlinePlayer() {
        return onlinePlayer;
    }

    public GameProfile gameProfile() {
        return onlinePlayer != null ? onlinePlayer.getGameProfile() : new GameProfile(uuid, name);
    }
}
