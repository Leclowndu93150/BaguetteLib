package com.leclowndu93150.baguettelib;

import net.fabricmc.api.ModInitializer;

public class BaguettelibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("BaguetteLib loaded on Fabric!");
    }
}
