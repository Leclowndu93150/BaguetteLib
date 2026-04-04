package com.leclowndu93150.baguettelib.example;

import com.leclowndu93150.baguettelib.network.PacketCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ExamplePacket(int value, String message) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ExamplePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("baguettelib", "example"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExamplePacket> STREAM_CODEC =
            StreamCodec.composite(
                    PacketCodecs.integer(), ExamplePacket::value,
                    PacketCodecs.string(), ExamplePacket::message,
                    ExamplePacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}