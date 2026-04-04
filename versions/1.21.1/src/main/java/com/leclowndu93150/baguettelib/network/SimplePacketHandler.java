package com.leclowndu93150.baguettelib.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@FunctionalInterface
public interface SimplePacketHandler<T extends CustomPacketPayload> {
    void handle(T packet, PacketContext context);
}
