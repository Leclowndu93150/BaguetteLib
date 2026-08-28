package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Inject(method = "setItem", at = @At("HEAD"))
    private void onSetItem(int index, ItemStack stack, CallbackInfo ci) {
        Inventory self = (Inventory) (Object) this;
        ItemStack oldStack = self.getItem(index);

        if (ItemStack.matches(oldStack, stack)) {
            return;
        }

        if (self.player.level().isClientSide) {
            return;
        }

        if (index >= 0 && index < 9) {
            // Hotbar
            MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Hotbar(self.player, index, oldStack, stack));
        } else if (index >= 9 && index < 36) {
            // Main inventory
            MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.MainInventory(self.player, index, oldStack, stack));
        } else if (index == 40) {
            // Offhand
            MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Offhand(self.player, index, oldStack, stack));
        } else if (index >= 36 && index < 40) {
            // Armor slots (36=feet, 37=legs, 38=chest, 39=head)
            EquipmentSlot slot = switch (index) {
                case 36 -> EquipmentSlot.FEET;
                case 37 -> EquipmentSlot.LEGS;
                case 38 -> EquipmentSlot.CHEST;
                case 39 -> EquipmentSlot.HEAD;
                default -> null;
            };
            if (slot != null) {
                MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Armor(self.player, slot, index, oldStack, stack));
            }
        }

        MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.All(self.player, index, oldStack, stack));
    }
}