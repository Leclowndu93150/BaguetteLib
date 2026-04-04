package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow @Final public Player player;

    @Inject(method = "setItem", at = @At("HEAD"))
    private void onSetItem(int index, ItemStack stack, CallbackInfo ci) {
        Inventory self = (Inventory) (Object) this;
        ItemStack oldStack = self.getItem(index);

        if (ItemStack.matches(oldStack, stack)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (index >= 0 && index < 9) {
            var event = new InventoryUpdateEvent.Hotbar(player, index, oldStack, stack);
            for (var listener : BaguetteEvents.INVENTORY_HOTBAR.getListeners()) {
                listener.accept(event);
            }
        } else if (index >= 9 && index < 36) {
            var event = new InventoryUpdateEvent.MainInventory(player, index, oldStack, stack);
            for (var listener : BaguetteEvents.INVENTORY_MAIN.getListeners()) {
                listener.accept(event);
            }
        } else if (index == 40) {
            var event = new InventoryUpdateEvent.Offhand(player, index, oldStack, stack);
            for (var listener : BaguetteEvents.INVENTORY_OFFHAND.getListeners()) {
                listener.accept(event);
            }
        } else if (index >= 36 && index < 40) {
            EquipmentSlot slot = switch (index) {
                case 36 -> EquipmentSlot.FEET;
                case 37 -> EquipmentSlot.LEGS;
                case 38 -> EquipmentSlot.CHEST;
                case 39 -> EquipmentSlot.HEAD;
                default -> null;
            };
            if (slot != null) {
                var event = new InventoryUpdateEvent.Armor(player, slot, index, oldStack, stack);
                for (var listener : BaguetteEvents.INVENTORY_ARMOR.getListeners()) {
                    listener.accept(event);
                }
            }
        }

        var allEvent = new InventoryUpdateEvent.All(player, index, oldStack, stack);
        for (var listener : BaguetteEvents.INVENTORY_ALL.getListeners()) {
            listener.accept(allEvent);
        }
    }
}
