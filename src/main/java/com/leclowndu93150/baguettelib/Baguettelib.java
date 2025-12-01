package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.event.entity.CreativeFlightEvent;
import com.leclowndu93150.baguettelib.example.ExamplePacket;
import com.leclowndu93150.baguettelib.network.NetworkManager;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingException;
import net.neoforged.fml.ModLoadingIssue;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.moddiscovery.ModFile;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mod(Baguettelib.MODID)
public class Baguettelib {
    public static final String MODID = "baguettelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static NetworkManager NETWORK;
    public Baguettelib(IEventBus modEventBus, ModContainer modContainer){

        // Before anyone asks bla bla bla why do you need a library now ?
        // it's because my mod's entry point doesn't even load when reborn is installed
        // I tried multiple ways and none of them worked. deal with it.
        if(isModInFolder("chisel-neoforge") && isModInFolder("chisel-1.21.1")){
            throw new ModLoadingException(ModLoadingIssue.error("Chisel Modern is not compatible with Chisel Reborn. People will not loose their builds if they used the latter mod, as there is auto migration."));
        }

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

    private boolean isModInFolder(String fileNameStart) {
        if (fileNameStart == null || fileNameStart.isEmpty()) {
            return false;
        }

        Path modsFolder = FMLPaths.MODSDIR.get();
        if (!Files.exists(modsFolder) || !Files.isDirectory(modsFolder)) {
            return false;
        }

        try (Stream<Path> files = Files.list(modsFolder)) {
            return files
                    .map(Path::getFileName)
                    .filter(Objects::nonNull)
                    .map(Path::toString)
                    .anyMatch(name -> name.startsWith(fileNameStart));
        } catch (IOException e) {
            LOGGER.error("e: ", e);
            return false;
        }
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
