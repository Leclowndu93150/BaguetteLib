package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.inventory.InventoryUpdateEvent;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class PacketHandlerMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleSetCarriedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void onHandleSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        int oldSlot = this.player.getInventory().getSelectedSlot();
        int newSlot = packet.getSlot();

        if (oldSlot != newSlot && newSlot >= 0 && newSlot < 9) {
            ItemStack oldMainHand = this.player.getInventory().getItem(oldSlot);
            ItemStack newMainHand = this.player.getInventory().getItem(newSlot);

            if (!ItemStack.matches(oldMainHand, newMainHand)) {
                var event = new InventoryUpdateEvent.Hands(
                        this.player, EquipmentSlot.MAINHAND, 0, oldMainHand, newMainHand
                );
                for (var listener : BaguetteEvents.INVENTORY_HANDS.getListeners()) {
                    listener.accept(event);
                }
            }
        }
    }
}
