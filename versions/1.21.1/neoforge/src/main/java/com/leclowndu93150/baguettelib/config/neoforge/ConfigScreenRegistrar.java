package com.leclowndu93150.baguettelib.config.neoforge;

import com.leclowndu93150.baguettelib.config.ConfigScreens;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.Map;
import java.util.function.Function;

public final class ConfigScreenRegistrar {

    private ConfigScreenRegistrar() {}

    public static void registerAll() {
        for (Map.Entry<String, Function<Screen, Screen>> entry : ConfigScreens.all().entrySet()) {
            Function<Screen, Screen> factory = entry.getValue();
            ModList.get().getModContainerById(entry.getKey()).ifPresent(container ->
                    container.registerExtensionPoint(IConfigScreenFactory.class,
                            (owner, parent) -> factory.apply(parent)));
        }
    }
}
