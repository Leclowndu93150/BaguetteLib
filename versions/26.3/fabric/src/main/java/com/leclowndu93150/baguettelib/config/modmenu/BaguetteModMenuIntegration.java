package com.leclowndu93150.baguettelib.config.modmenu;

import com.leclowndu93150.baguettelib.config.ConfigScreens;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class BaguetteModMenuIntegration implements ModMenuApi {

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new LinkedHashMap<>();
        for (Map.Entry<String, Function<Screen, Screen>> entry : ConfigScreens.all().entrySet()) {
            Function<Screen, Screen> factory = entry.getValue();
            ConfigScreenFactory<Screen> screenFactory = factory::apply;
            factories.put(entry.getKey(), screenFactory);
        }
        return factories;
    }
}
