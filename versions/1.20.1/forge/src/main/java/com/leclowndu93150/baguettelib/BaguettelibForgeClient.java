package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.config.forge.ConfigScreenRegistrar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Baguettelib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class BaguettelibForgeClient {

    private BaguettelibForgeClient() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ConfigScreenRegistrar::registerAll);
    }
}
