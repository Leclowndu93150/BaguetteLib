package com.leclowndu93150.baguettelib.stats;

import net.minecraft.network.chat.Component;

import java.util.function.IntFunction;

public final class StatFormatters {

    private StatFormatters() {}

    public static final IntFunction<Component> DEFAULT = value ->
            Component.literal(value <= 0 ? "0" : Integer.toString(value));

    public static final IntFunction<Component> TIME = value -> {
        if (value <= 0) return Component.literal("0");
        long seconds = value / 20L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        return Component.literal(String.format("[%dd] %02d:%02d:%02d",
                days, hours % 24L, minutes % 60L, seconds % 60L));
    };

    public static final IntFunction<Component> DISTANCE = value -> {
        if (value <= 0) return Component.literal("0");
        double blocks = value / 100.0;
        if (blocks >= 1000) {
            return Component.literal(String.format("%.2f km", blocks / 1000.0));
        }
        return Component.literal(String.format("%.1f m", blocks));
    };

    public static final IntFunction<Component> DAMAGE = value -> {
        if (value <= 0) return Component.literal("0");
        return Component.literal(String.format("%.1f", value / 10.0));
    };
}
