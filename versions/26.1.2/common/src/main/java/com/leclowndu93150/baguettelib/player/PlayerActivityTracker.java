package com.leclowndu93150.baguettelib.player;

import com.leclowndu93150.baguettelib.Constants;
import com.leclowndu93150.baguettelib.data.UuidMapSavedData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Map;
import java.util.UUID;

public class PlayerActivityTracker extends UuidMapSavedData<Long> {

    public static final Codec<PlayerActivityTracker> CODEC = RecordCodecBuilder.create(i -> i.group(
            mapCodec(Codec.LONG).fieldOf("last_seen").forGetter(t -> t.map)
    ).apply(i, PlayerActivityTracker::new));

    public static final SavedDataType<PlayerActivityTracker> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "player_activity"),
            PlayerActivityTracker::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    public PlayerActivityTracker() {
        super();
    }

    public PlayerActivityTracker(Map<UUID, Long> initial) {
        super(initial);
    }

    public static PlayerActivityTracker get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public long getLastSeen(UUID uuid) {
        return getOrDefault(uuid, 0L);
    }

    public void markSeen(UUID uuid, long gameTime) {
        put(uuid, gameTime);
    }

    public void markSeen(ServerPlayer player) {
        markSeen(player.getUUID(), player.level().getGameTime());
    }
}
