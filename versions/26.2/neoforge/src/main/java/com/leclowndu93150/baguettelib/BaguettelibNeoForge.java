package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.player.neoforge.PlayerActivityNeoForgeHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class BaguettelibNeoForge {

    public BaguettelibNeoForge(IEventBus modEventBus) {
        PlayerActivityNeoForgeHandler.register(NeoForge.EVENT_BUS);
        CommonClass.init();
    }
}
