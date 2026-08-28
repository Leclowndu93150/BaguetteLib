package com.leclowndu93150.baguettelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class BaguettelibNeoForge {

    public BaguettelibNeoForge(IEventBus modEventBus) {
        Constants.LOG.info("BaguetteLib loaded on NeoForge!");
    }
}
