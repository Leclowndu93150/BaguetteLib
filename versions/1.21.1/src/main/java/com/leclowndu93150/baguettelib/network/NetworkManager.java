package com.leclowndu93150.baguettelib.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static final Map<String, NetworkManager> INSTANCES = new HashMap<>();
    final String modId;
    private final Map<String, PacketRegistration<?>> registrations = new HashMap<>();

    private NetworkManager(String modId) {
        this.modId = modId;
    }

    public static NetworkManager create(String modId, IEventBus modEventBus) {
        NetworkManager instance = new NetworkManager(modId);
        INSTANCES.put(modId, instance);
        modEventBus.addListener(instance::onRegisterPayloadHandlers);
        return instance;
    }

    public static NetworkManager getInstance(String modId) {
        return INSTANCES.get(modId);
    }

    public <T extends CustomPacketPayload> PacketBuilder<T> registerPacket(Class<T> packetClass, String name) {
        return new PacketBuilder<>(this, packetClass, name);
    }

    <T extends CustomPacketPayload> void registerPacketInternal(
            String name,
            Class<T> packetClass,
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            PacketHandler<T> handler) {

        registrations.put(name, new PacketRegistration<>(packetClass, type, codec, handler));
    }

    private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(modId);

        for (PacketRegistration<?> registration : registrations.values()) {
            registerPayload(registrar, registration);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CustomPacketPayload> void registerPayload(PayloadRegistrar registrar, PacketRegistration<T> registration) {
        switch (registration.handler.getDirection()) {
            case CLIENT_TO_SERVER -> registrar.playToServer(registration.type, registration.codec, (payload, context) -> {
                registration.handler.handle(payload, new ContextWrapper(context));
            });
            case SERVER_TO_CLIENT -> registrar.playToClient(registration.type, registration.codec, (payload, context) -> {
                registration.handler.handle(payload, new ContextWrapper(context));
            });
            case BIDIRECTIONAL -> registrar.playBidirectional(registration.type, registration.codec, (payload, context) -> {
                registration.handler.handle(payload, new ContextWrapper(context));
            });
        }
    }

    public <T extends CustomPacketPayload> void sendToServer(T packet) {
        PacketDistributor.sendToServer(packet);
    }

    public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public <T extends CustomPacketPayload> void sendToAllPlayers(T packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }

    private static class PacketRegistration<T extends CustomPacketPayload> {
        final Class<T> packetClass;
        final CustomPacketPayload.Type<T> type;
        final StreamCodec<RegistryFriendlyByteBuf, T> codec;
        final PacketHandler<T> handler;

        PacketRegistration(Class<T> packetClass, CustomPacketPayload.Type<T> type,
                           StreamCodec<RegistryFriendlyByteBuf, T> codec, PacketHandler<T> handler) {
            this.packetClass = packetClass;
            this.type = type;
            this.codec = codec;
            this.handler = handler;
        }
    }

    private static class ContextWrapper implements PacketContext {
        private final IPayloadContext context;

        public ContextWrapper(IPayloadContext context) {
            this.context = context;
        }

        @Override
        public ServerPlayer getPlayer() {
            return (ServerPlayer) context.player();
        }

        @Override
        public boolean isClientSide() {
            return context.flow().getReceptionSide().isClient();
        }

        @Override
        public void enqueueWork(Runnable work) {
            context.enqueueWork(work);
        }
    }
}