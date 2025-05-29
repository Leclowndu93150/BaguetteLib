package com.leclowndu93150.baguettelib.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface PacketHandler<T extends CustomPacketPayload> {
    void handle(T packet, PacketContext context);
    StreamCodec<RegistryFriendlyByteBuf, T> getCodec();
    PacketDirection getDirection();
}

