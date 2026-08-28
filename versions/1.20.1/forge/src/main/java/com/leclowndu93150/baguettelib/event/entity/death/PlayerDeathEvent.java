package com.leclowndu93150.baguettelib.event.entity.death;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;

public abstract class PlayerDeathEvent extends LivingDeathEvent {
    protected PlayerDeathEvent(Player player, DamageSource source) {
        super(player, source);
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }

    public Player getPlayer() {
        return getEntity();
    }

    /**
     * Fired before player death processing.
     * Allows modification of inventory, experience, etc.
     * Can be canceled to prevent death.
     */
    @Cancelable
    public static class Pre extends PlayerDeathEvent {
        public Pre(Player player, DamageSource source) {
            super(player, source);
        }
    }

    /**
     * Fired after player death processing.
     * Cannot be canceled.
     */
    public static class Post extends PlayerDeathEvent {
        public Post(Player player, DamageSource source) {
            super(player, source);
        }
    }
}