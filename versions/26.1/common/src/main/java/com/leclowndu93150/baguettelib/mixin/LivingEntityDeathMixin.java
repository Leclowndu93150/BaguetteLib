package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.event.BaguetteEvents;
import com.leclowndu93150.baguettelib.event.entity.death.LivingDeathEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 500)
public class LivingEntityDeathMixin {

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void onPreDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.isRemoved()) {
            return;
        }

        LivingDeathEvent.Pre event = new LivingDeathEvent.Pre(self, damageSource);
        for (var listener : BaguetteEvents.LIVING_DEATH_PRE.getListeners()) {
            listener.accept(event);
        }
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void onPostDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.isRemoved()) {
            return;
        }

        LivingDeathEvent.Post event = new LivingDeathEvent.Post(self, damageSource);
        for (var listener : BaguetteEvents.LIVING_DEATH_POST.getListeners()) {
            listener.accept(event);
        }
    }
}
