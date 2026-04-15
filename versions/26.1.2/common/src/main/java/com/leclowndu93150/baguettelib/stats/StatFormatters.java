package com.leclowndu93150.baguettelib.stats;

import net.minecraft.network.chat.Component;

import java.util.function.IntFunction;

public final class StatFormatters {

    private StatFormatters() {}

    public static final IntFunction<Component> DEFAULT = value ->
            Component.literal(value <= 0 ? "0" : Integer.toString(value));

    public static final IntFunction<Component> TIME = value -> {
        if (value <= 0) return Component.literal("0");
        int days = (int) (value / 72000D + 0.5D);
        int ticks = value % 24000;
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        return Component.literal(String.format("[%dd] %02d:%02d:%02d", days, hours, minutes, seconds));
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
