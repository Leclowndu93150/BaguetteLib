package com.leclowndu93150.baguettelib.network;

import net.minecraft.server.level.ServerPlayer;

public interface PacketContext {
    ServerPlayer getPlayer();
    boolean isClientSide();
    void enqueueWork(Runnable work);
}
