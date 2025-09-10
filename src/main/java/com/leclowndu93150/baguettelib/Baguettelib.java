package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.event.entity.CreativeFlightEvent;
import com.leclowndu93150.baguettelib.example.ExamplePacket;
import com.leclowndu93150.baguettelib.network.NetworkManager;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

@Mod(Baguettelib.MODID)
public class Baguettelib {
    public static final String MODID = "baguettelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static NetworkManager NETWORK;
    public Baguettelib(IEventBus modEventBus, ModContainer modContainer){
        NETWORK = NetworkManager.create(MODID, modEventBus);

        NETWORK.registerPacket(ExamplePacket.class, "example")
                .codec(ExamplePacket.STREAM_CODEC)
                .serverToClient()
                .handler((packet, context) -> {
                    context.enqueueWork(() -> {
                        LOGGER.info("Received packet: {} - {}", packet.value(), packet.message());
                    });
                })
                .build();

        NeoForge.EVENT_BUS.addListener(this::onPlayerJoin);
        NeoForge.EVENT_BUS.addListener(this::onCreativeFlight);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        // if(event.getEntity() instanceof ServerPlayer player) Baguettelib.NETWORK.sendToPlayer(player, new ExamplePacket(69,"Hello World"));
    }

    @SubscribeEvent
    public void onCreativeFlight(CreativeFlightEvent.Toggle event){
//        event.setCanceled(true);
//        event.setFlightState(false);
    }

}
