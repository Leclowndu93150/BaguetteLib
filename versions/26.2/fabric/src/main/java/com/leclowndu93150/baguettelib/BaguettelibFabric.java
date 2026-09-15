package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.player.fabric.PlayerActivityFabricHandler;
import net.fabricmc.api.ModInitializer;

public class BaguettelibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerActivityFabricHandler.register();
        CommonClass.init();
    }
}
