package com.leclowndu93150.baguettelib.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UuidMapSavedData<V> extends SavedData {

    protected final Map<UUID, V> map = new HashMap<>();

    public UuidMapSavedData() {
    }

    public UuidMapSavedData(Map<UUID, V> initial) {
        this.map.putAll(initial);
    }

    public V get(UUID uuid) {
        return map.get(uuid);
    }

    public V getOrDefault(UUID uuid, V fallback) {
        return map.getOrDefault(uuid, fallback);
    }

    public void put(UUID uuid, V value) {
        map.put(uuid, value);
        setDirty();
    }

    public void remove(UUID uuid) {
        if (map.remove(uuid) != null) {
            setDirty();
        }
    }

    public boolean contains(UUID uuid) {
        return map.containsKey(uuid);
    }

    public Set<UUID> keys() {
        return map.keySet();
    }

    public Map<UUID, V> asMap() {
        return map;
    }

    public int size() {
        return map.size();
    }

    public static <V> Codec<Map<UUID, V>> mapCodec(Codec<V> valueCodec) {
        return Codec.unboundedMap(UUIDUtil.STRING_CODEC, valueCodec);
    }
}
