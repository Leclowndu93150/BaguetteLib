package com.leclowndu93150.baguettelib.event.entity.death;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

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

    public static class Pre extends PlayerDeathEvent {
        private boolean canceled;

        public Pre(Player player, DamageSource source) {
            super(player, source);
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }

    public static class Post extends PlayerDeathEvent {
        public Post(Player player, DamageSource source) {
            super(player, source);
        }
    }
}
