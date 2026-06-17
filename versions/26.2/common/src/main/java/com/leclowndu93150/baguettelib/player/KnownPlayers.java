package com.leclowndu93150.baguettelib.player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public final class KnownPlayers {

    private KnownPlayers() {}

    public static Set<UUID> all(MinecraftServer server) {
        Set<UUID> uuids = new HashSet<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            uuids.add(player.getUUID());
        }
        uuids.addAll(fromStatsFolder(server));
        return uuids;
    }

    public static Set<UUID> fromStatsFolder(MinecraftServer server) {
        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
        Set<UUID> uuids = new HashSet<>();
        if (!Files.isDirectory(statsDir)) {
            return uuids;
        }
        try (Stream<Path> files = Files.list(statsDir)) {
            files.forEach(file -> {
                String fileName = file.getFileName().toString();
                if (!fileName.endsWith(".json")) return;
                String uuidPart = fileName.substring(0, fileName.length() - 5);
                try {
                    uuids.add(UUID.fromString(uuidPart));
                } catch (IllegalArgumentException ignored) {
                }
            });
        } catch (IOException ignored) {
        }
        return uuids;
    }
}
