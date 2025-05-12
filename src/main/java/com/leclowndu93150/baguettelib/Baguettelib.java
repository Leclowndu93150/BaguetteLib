package com.leclowndu93150.baguettelib;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Baguettelib.MODID)
public class Baguettelib
{
    public static final String MODID = "baguettelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Baguettelib(IEventBus modEventBus, ModContainer modContainer){

    }
}
