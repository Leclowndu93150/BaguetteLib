package com.leclowndu93150.baguettelib.curios;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LegacyCurioSlotDataMigration {

    private static final String LEGACY_KEY = BaguetteCuriosData.KEY;
    private static final String REMOVED_LEGACY_KEY = "!" + LEGACY_KEY;
    private static final String CUSTOM_DATA_KEY = "minecraft:custom_data";
    private static final String SHORT_CUSTOM_DATA_KEY = "custom_data";

    public static <T> T migrate(DynamicOps<T> ops, T input) {
        MapLike<T> components = ops.getMap(input).result().orElse(null);
        if (components == null) {
            return input;
        }

        T legacy = components.get(LEGACY_KEY);
        if (legacy == null && components.get(REMOVED_LEGACY_KEY) == null) {
            return input;
        }

        String customDataKey = components.get(SHORT_CUSTOM_DATA_KEY) != null ? SHORT_CUSTOM_DATA_KEY : CUSTOM_DATA_KEY;
        T customData = components.get(customDataKey);
        if (legacy != null) {
            T base = customData == null ? ops.emptyMap() : customData;
            customData = ops.mergeToMap(base, ops.createString(LEGACY_KEY), legacy).result().orElse(customData);
        }

        Map<T, T> migrated = new LinkedHashMap<>();
        components.entries().forEach(entry -> {
            String key = ops.getStringValue(entry.getFirst()).result().orElse(null);
            if (LEGACY_KEY.equals(key) || REMOVED_LEGACY_KEY.equals(key) || customDataKey.equals(key)) {
                return;
            }
            migrated.put(entry.getFirst(), entry.getSecond());
        });
        if (customData != null) {
            migrated.put(ops.createString(customDataKey), customData);
        }
        return ops.createMap(migrated);
    }

    private LegacyCurioSlotDataMigration() {}
}
