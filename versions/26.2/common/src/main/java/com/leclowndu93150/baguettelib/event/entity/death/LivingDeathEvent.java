package com.leclowndu93150.baguettelib.event.entity.death;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public abstract class LivingDeathEvent {
    protected final LivingEntity entity;
    protected final DamageSource source;

    protected LivingDeathEvent(LivingEntity entity, DamageSource source) {
        this.entity = entity;
        this.source = source;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public static class Pre extends LivingDeathEvent {
        private boolean canceled;

        public Pre(LivingEntity entity, DamageSource source) {
            super(entity, source);
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }

    public static class Post extends LivingDeathEvent {
        public Post(LivingEntity entity, DamageSource source) {
            super(entity, source);
        }
    }
}
