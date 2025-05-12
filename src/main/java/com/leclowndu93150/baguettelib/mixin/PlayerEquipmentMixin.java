package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEquipmentMixin {

    @Inject(method = "setItemSlot", at = @At("HEAD"))
    private void onSetItemSlot(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        Player self = (Player) (Object) this;

        if (self.level().isClientSide) {
            return;
        }

        ItemStack oldStack = self.getItemBySlot(slot);

        if (ItemStack.matches(oldStack, stack)) {
            return;
        }

        int slotIndex = getSlotIndex(slot);

        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Armor(self, slot, slotIndex, oldStack, stack));
        } else if (slot.getType() == EquipmentSlot.Type.HAND) {
            MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Hands(self, slot, slotIndex, oldStack, stack));
        }

        MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.All(self, slotIndex, oldStack, stack));
    }

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