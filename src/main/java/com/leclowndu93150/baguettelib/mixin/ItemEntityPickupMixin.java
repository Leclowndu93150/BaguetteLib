package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemEntity.class, priority = 500)
public class ItemEntityPickupMixin {

    @Inject(method = "playerTouch", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private void onPlayerTouch(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;

        if (!self.level().isClientSide) {
            ItemStack itemStack = self.getItem();

            if (itemStack.isEmpty()) {
                return;
            }

            if (self.hasPickUpDelay() || (self.getTarget() != null && !self.getTarget().equals(player.getUUID()))) {
                return;
            }

            int targetSlot = findTargetSlot(player, itemStack);
            if (targetSlot == -1) {
                return;
            }

            ItemStack oldStack = player.getInventory().getItem(targetSlot);
            ItemStack newStack = oldStack.isEmpty() ? itemStack.copy() : oldStack.copy();

            if (!oldStack.isEmpty() && ItemStack.isSameItemSameComponents(oldStack, itemStack)) {
                int transferAmount = Math.min(itemStack.getCount(), oldStack.getMaxStackSize() - oldStack.getCount());
                newStack.setCount(oldStack.getCount() + transferAmount);
            } else {
                newStack = itemStack.copy();
            }

            if (targetSlot >= 0 && targetSlot < 9) {
                NeoForge.EVENT_BUS.post(new InventoryUpdateEvent.Hotbar(player, targetSlot, oldStack, newStack));
            } else if (targetSlot >= 9 && targetSlot < 36) {
                NeoForge.EVENT_BUS.post(new InventoryUpdateEvent.MainInventory(player, targetSlot, oldStack, newStack));
            } else if (targetSlot == 40) {
                NeoForge.EVENT_BUS.post(new InventoryUpdateEvent.Offhand(player, targetSlot, oldStack, newStack));
            }

            NeoForge.EVENT_BUS.post(new InventoryUpdateEvent.All(player, targetSlot, oldStack, newStack));
        }
    }

    private int findTargetSlot(Player player, ItemStack itemStack) {
        var inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack existing = inventory.getItem(i);
            if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, itemStack)
                    && existing.getCount() < existing.getMaxStackSize()) {
                return i;
            }
        }

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }
}