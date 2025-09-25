package com.leclowndu93150.baguettelib;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Baguettelib.MODID)
public class Baguettelib {
    public static final String MODID = "baguettelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Baguettelib(){
        //MinecraftForge.EVENT_BUS.addListener(this::onCreativeFlight);
    }

//    @SubscribeEvent
//    public void onCreativeFlight(CreativeFlightEvent.Toggle event){
//        event.setCanceled(true);
//        event.setFlightState(false);
//    }
}
