package com.leclowndu93150.baguettelib.config.forge;

import com.leclowndu93150.baguettelib.config.ConfigScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;

import java.util.Map;
import java.util.function.Function;

public final class ConfigScreenRegistrar {

    private ConfigScreenRegistrar() {}

    public static void registerAll() {
        for (Map.Entry<String, Function<Screen, Screen>> entry : ConfigScreens.all().entrySet()) {
            Function<Screen, Screen> factory = entry.getValue();
            ModList.get().getModContainerById(entry.getKey()).ifPresent(container ->
                    container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                            () -> new ConfigScreenHandler.ConfigScreenFactory(factory)));
        }
    }
}
