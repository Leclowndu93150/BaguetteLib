package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = ItemEntity.class, priority = 727)
public class ItemEntityPickupMixin {

    @Shadow
    private int pickupDelay;

    @Shadow
    private @Nullable UUID target;

    @Inject(method = "playerTouch", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private void onPlayerTouch(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;

        if (!self.level().isClientSide()) {
            ItemStack itemStack = self.getItem();

            if (itemStack.isEmpty()) {
                return;
            }

            if (this.pickupDelay > 0 || (this.target != null && !this.target.equals(player.getUUID()))) {
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
                var event = new InventoryUpdateEvent.Hotbar(player, targetSlot, oldStack, newStack);
                for (var listener : BaguetteEvents.INVENTORY_HOTBAR.getListeners()) {
                    listener.accept(event);
                }
            } else if (targetSlot >= 9 && targetSlot < 36) {
                var event = new InventoryUpdateEvent.MainInventory(player, targetSlot, oldStack, newStack);
                for (var listener : BaguetteEvents.INVENTORY_MAIN.getListeners()) {
                    listener.accept(event);
                }
            } else if (targetSlot == 40) {
                var event = new InventoryUpdateEvent.Offhand(player, targetSlot, oldStack, newStack);
                for (var listener : BaguetteEvents.INVENTORY_OFFHAND.getListeners()) {
                    listener.accept(event);
                }
            }

            var allEvent = new InventoryUpdateEvent.All(player, targetSlot, oldStack, newStack);
            for (var listener : BaguetteEvents.INVENTORY_ALL.getListeners()) {
                listener.accept(allEvent);
            }
        }
    }

    @Unique
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
