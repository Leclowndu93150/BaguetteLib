package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.entity.death.PlayerDeathEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class, priority = 500)
public class ServerPlayerDeathMixin {

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void onPreDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        if (self.isRemoved()) {
            return;
        }

        PlayerDeathEvent.Pre event = new PlayerDeathEvent.Pre(self, damageSource);
        for (var listener : BaguetteEvents.PLAYER_DEATH_PRE.getListeners()) {
            listener.accept(event);
        }
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void onPostDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        if (self.isRemoved()) {
            return;
        }

        PlayerDeathEvent.Post event = new PlayerDeathEvent.Post(self, damageSource);
        for (var listener : BaguetteEvents.PLAYER_DEATH_POST.getListeners()) {
            listener.accept(event);
        }
    }
}
