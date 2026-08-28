package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class PacketHandlerMixin {


    @Inject(method = "handleSetCarriedItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", ordinal = 1))
    private void onHandleSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        ServerGamePacketListenerImpl self = (ServerGamePacketListenerImpl) (Object) this;

        int oldSlot = self.player.getInventory().selected;
        int newSlot = packet.getSlot();

        if (oldSlot != newSlot && newSlot >= 0 && newSlot < 9) {
            ItemStack oldMainHand = self.player.getInventory().getItem(oldSlot);
            ItemStack newMainHand = self.player.getInventory().getItem(newSlot);

            if (!ItemStack.matches(oldMainHand, newMainHand)) {
                MinecraftForge.EVENT_BUS.post(new InventoryUpdateEvent.Hands(
                        self.player, EquipmentSlot.MAINHAND, 0, oldMainHand, newMainHand
                ));
            }
        }
    }
}