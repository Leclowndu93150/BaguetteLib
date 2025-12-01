package com.leclowndu93150.baguettelib;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

@Mod(Baguettelib.MODID)
public class Baguettelib {
    public static final String MODID = "baguettelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Baguettelib(){

        // Before anyone asks bla bla bla why do you need a library now ?
        // it's because my mod's entry point doesn't even load when reborn is installed
        // I tried multiple ways and none of them worked. deal with it.
        if(isModInFolder("chisel-forge") && isModInFolder("chisel-1.20.1")){
            throw new ModLoadingException(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.VALIDATE, "Chisel Modern is not compatible with Chisel Reborn.", null);
        }

        //MinecraftForge.EVENT_BUS.addListener(this::onCreativeFlight);
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

//    @SubscribeEvent
//    public void onCreativeFlight(CreativeFlightEvent.Toggle event){
//        event.setCanceled(true);
//        event.setFlightState(false);
//    }
}
