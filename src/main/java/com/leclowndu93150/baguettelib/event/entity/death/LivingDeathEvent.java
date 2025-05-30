package com.leclowndu93150.baguettelib.event.entity.death;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class LivingDeathEvent extends Event {
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

    /**
     * Fired before death processing begins.
     * Can be canceled to prevent death.
     * Allows modification of entity state before death.
     */
    public static class Pre extends LivingDeathEvent implements ICancellableEvent {
        public Pre(LivingEntity entity, DamageSource source) {
            super(entity, source);
        }
    }

    /**
     * Fired after death has been processed but before entity removal.
     * Cannot be canceled.
     */
    public static class Post extends LivingDeathEvent {
        public Post(LivingEntity entity, DamageSource source) {
            super(entity, source);
        }
    }
}