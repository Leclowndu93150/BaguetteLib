package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class PlayerEquipmentMixin {

    @Inject(method = "setItemSlot", at = @At("HEAD"))
    private void onSetItemSlot(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (!((Object) this instanceof Player self)) {
            return;
        }

        if (self.level().isClientSide()) {
            return;
        }

        ItemStack oldStack = self.getItemBySlot(slot);

        if (ItemStack.matches(oldStack, stack)) {
            return;
        }

        int slotIndex = getSlotIndex(slot);

        if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            var event = new InventoryUpdateEvent.Armor(self, slot, slotIndex, oldStack, stack);
            for (var listener : BaguetteEvents.INVENTORY_ARMOR.getListeners()) {
                listener.accept(event);
            }
        } else if (slot.getType() == EquipmentSlot.Type.HAND) {
            var event = new InventoryUpdateEvent.Hands(self, slot, slotIndex, oldStack, stack);
            for (var listener : BaguetteEvents.INVENTORY_HANDS.getListeners()) {
                listener.accept(event);
            }
        }

        var allEvent = new InventoryUpdateEvent.All(self, slotIndex, oldStack, stack);
        for (var listener : BaguetteEvents.INVENTORY_ALL.getListeners()) {
            listener.accept(allEvent);
        }
    }

    @Unique
    private static int getSlotIndex(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> 0;
            case OFFHAND -> 40;
            case FEET -> 36;
            case LEGS -> 37;
            case CHEST -> 38;
            case HEAD -> 39;
            default -> -1;
        };
    }
}
