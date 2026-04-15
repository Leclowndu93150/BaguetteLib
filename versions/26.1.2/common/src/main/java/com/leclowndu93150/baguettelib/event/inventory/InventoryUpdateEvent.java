package com.leclowndu93150.baguettelib.event.inventory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class InventoryUpdateEvent {
    protected final Player player;
    protected final int slot;
    protected final ItemStack oldStack;
    protected final ItemStack newStack;

    protected InventoryUpdateEvent(Player player, int slot, ItemStack oldStack, ItemStack newStack) {
        this.player = player;
        this.slot = slot;
        this.oldStack = oldStack.copy();
        this.newStack = newStack.copy();
    }

    public Player getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getOldStack() {
        return oldStack;
    }

    public ItemStack getNewStack() {
        return newStack;
    }

    public static class Armor extends InventoryUpdateEvent {
        private final EquipmentSlot equipmentSlot;

        public Armor(Player player, EquipmentSlot equipmentSlot, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
            this.equipmentSlot = equipmentSlot;
        }

        public EquipmentSlot getEquipmentSlot() {
            return equipmentSlot;
        }
    }

    public static class Hands extends InventoryUpdateEvent {
        private final EquipmentSlot equipmentSlot;

        public Hands(Player player, EquipmentSlot equipmentSlot, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
            this.equipmentSlot = equipmentSlot;
        }

        public EquipmentSlot getEquipmentSlot() {
            return equipmentSlot;
        }
    }

    public static class Hotbar extends InventoryUpdateEvent {
        public Hotbar(Player player, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
        }
    }

    public static class MainInventory extends InventoryUpdateEvent {
        public MainInventory(Player player, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
        }
    }

    public static class Offhand extends InventoryUpdateEvent {
        public Offhand(Player player, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
        }
    }

    public static class All extends InventoryUpdateEvent {
        public All(Player player, int slot, ItemStack oldStack, ItemStack newStack) {
            super(player, slot, oldStack, newStack);
        }
    }
}
