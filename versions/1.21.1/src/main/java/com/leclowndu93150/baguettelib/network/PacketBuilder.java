package com.leclowndu93150.baguettelib.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PacketBuilder<T extends CustomPacketPayload> {
    private final NetworkManager manager;
    private final Class<T> packetClass;
    private final String name;
    private final CustomPacketPayload.Type<T> type;
    private StreamCodec<RegistryFriendlyByteBuf, T> codec;
    private PacketDirection direction = PacketDirection.BIDIRECTIONAL;
    private PacketHandler<T> handler;

    PacketBuilder(NetworkManager manager, Class<T> packetClass, String name) {
        this.manager = manager;
        this.packetClass = packetClass;
        this.name = name;
        this.type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(manager.modId, name));
    }

    public PacketBuilder<T> codec(StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        this.codec = codec;
        return this;
    }

    public PacketBuilder<T> clientToServer() {
        this.direction = PacketDirection.CLIENT_TO_SERVER;
        return this;
    }

    public PacketBuilder<T> serverToClient() {
        this.direction = PacketDirection.SERVER_TO_CLIENT;
        return this;
    }

    public PacketBuilder<T> bidirectional() {
        this.direction = PacketDirection.BIDIRECTIONAL;
        return this;
    }

    public PacketBuilder<T> handler(PacketHandler<T> handler) {
        this.handler = handler;
        return this;
    }

    public PacketBuilder<T> handler(SimplePacketHandler<T> handler) {
        this.handler = new PacketHandler<T>() {
            @Override
            public void handle(T packet, PacketContext context) {
                handler.handle(packet, context);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, T> getCodec() {
                return codec;
            }

            @Override
            public PacketDirection getDirection() {
                return direction;
            }
        };
        return this;
    }

    public void build() {
        if (codec == null) throw new IllegalStateException("Codec must be set");
        if (handler == null) throw new IllegalStateException("Handler must be set");

        if (this.handler.getCodec() == null) {
            this.handler = new PacketHandler<T>() {
                @Override
                public void handle(T packet, PacketContext context) {
                    handler.handle(packet, context);
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, T> getCodec() {
                    return codec;
                }

                @Override
                public PacketDirection getDirection() {
                    return direction;
                }
            };
        }

        manager.registerPacketInternal(name, packetClass, type, codec, handler);
    }
}