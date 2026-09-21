package com.leclowndu93150.baguettelib.event;

import com.leclowndu93150.baguettelib.event.entity.CreativeFlightEvent;
import com.leclowndu93150.baguettelib.event.entity.death.LivingDeathEvent;
import com.leclowndu93150.baguettelib.event.entity.death.PlayerDeathEvent;
import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class BaguetteEvents {

    public static final Event<Consumer<LivingDeathEvent.Pre>> LIVING_DEATH_PRE = new Event<>();
    public static final Event<Consumer<LivingDeathEvent.Post>> LIVING_DEATH_POST = new Event<>();

    public static final Event<Consumer<PlayerDeathEvent.Pre>> PLAYER_DEATH_PRE = new Event<>();
    public static final Event<Consumer<PlayerDeathEvent.Post>> PLAYER_DEATH_POST = new Event<>();

    public static final Event<Consumer<CreativeFlightEvent.Toggle>> CREATIVE_FLIGHT_TOGGLE = new Event<>();
    public static final Event<Consumer<CreativeFlightEvent.Changed>> CREATIVE_FLIGHT_CHANGED = new Event<>();

    public static final Event<Consumer<InventoryUpdateEvent.Armor>> INVENTORY_ARMOR = new Event<>();
    public static final Event<Consumer<InventoryUpdateEvent.Hands>> INVENTORY_HANDS = new Event<>();
    public static final Event<Consumer<InventoryUpdateEvent.Hotbar>> INVENTORY_HOTBAR = new Event<>();
    public static final Event<Consumer<InventoryUpdateEvent.MainInventory>> INVENTORY_MAIN = new Event<>();
    public static final Event<Consumer<InventoryUpdateEvent.Offhand>> INVENTORY_OFFHAND = new Event<>();
    public static final Event<Consumer<InventoryUpdateEvent.All>> INVENTORY_ALL = new Event<>();

    public static final class Event<T extends Consumer<?>> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();

        public void register(T listener) {
            listeners.add(listener);
        }

        public List<T> getListeners() {
            return listeners;
        }
    }

    private BaguetteEvents() {}
}
