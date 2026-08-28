package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.entity.CreativeFlightEvent;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerAbilitiesPacketMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handlePlayerAbilities", at = @At("HEAD"), cancellable = true)
    private void onHandlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet, CallbackInfo ci) {
        if (this.player.getAbilities().mayfly && this.player.getAbilities().flying != packet.isFlying()) {
            CreativeFlightEvent.Toggle toggleEvent = new CreativeFlightEvent.Toggle(this.player, packet.isFlying());
            MinecraftForge.EVENT_BUS.post(toggleEvent);

            if (toggleEvent.isCanceled()) {
                this.player.onUpdateAbilities();
                ci.cancel();
                return;
            } else {
                this.player.getAbilities().flying = toggleEvent.getFlightState();

                CreativeFlightEvent.Changed changedEvent = new CreativeFlightEvent.Changed(this.player, !toggleEvent.getFlightState(), toggleEvent.getFlightState());
                MinecraftForge.EVENT_BUS.post(changedEvent);
            }

            ci.cancel();
        }
    }
}