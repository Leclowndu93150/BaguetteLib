package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.config.neoforge.ConfigScreenRegistrar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Baguettelib.MODID, dist = Dist.CLIENT)
public class BaguettelibClient {

    public BaguettelibClient(IEventBus modEventBus) {
        modEventBus.addListener(FMLClientSetupEvent.class, event -> event.enqueueWork(ConfigScreenRegistrar::registerAll));
    }
}
