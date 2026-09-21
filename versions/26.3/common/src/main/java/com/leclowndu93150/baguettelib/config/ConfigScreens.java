package com.leclowndu93150.baguettelib.config;

import net.minecraft.client.gui.screens.Screen;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class ConfigScreens {

    private static final Map<String, Function<Screen, Screen>> FACTORIES = new LinkedHashMap<>();

    private ConfigScreens() {}

    public static void register(String modId, Function<Screen, Screen> factory) {
        FACTORIES.put(modId, factory);
    }

    public static Function<Screen, Screen> get(String modId) {
        return FACTORIES.get(modId);
    }

    public static Map<String, Function<Screen, Screen>> all() {
        return Collections.unmodifiableMap(FACTORIES);
    }

    public static Screen create(String modId, Screen parent) {
        Function<Screen, Screen> factory = FACTORIES.get(modId);
        return factory == null ? null : factory.apply(parent);
    }
}
