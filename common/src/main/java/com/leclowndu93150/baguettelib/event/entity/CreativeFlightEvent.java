package com.leclowndu93150.baguettelib.event.entity;

import net.minecraft.world.entity.player.Player;

public abstract class CreativeFlightEvent {
    protected final Player player;

    protected CreativeFlightEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public static class Toggle extends CreativeFlightEvent {
        private boolean flightState;
        private boolean canceled;

        public Toggle(Player player, boolean flightState) {
            super(player);
            this.flightState = flightState;
        }

        public boolean getFlightState() {
            return flightState;
        }

        public void setFlightState(boolean flightState) {
            this.flightState = flightState;
        }

        public boolean isEnablingFlight() {
            return flightState;
        }

        public boolean isDisablingFlight() {
            return !flightState;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }

    public static class Changed extends CreativeFlightEvent {
        private final boolean previousFlightState;
        private final boolean newFlightState;

        public Changed(Player player, boolean previousFlightState, boolean newFlightState) {
            super(player);
            this.previousFlightState = previousFlightState;
            this.newFlightState = newFlightState;
        }

        public boolean getPreviousFlightState() {
            return previousFlightState;
        }

        public boolean getNewFlightState() {
            return newFlightState;
        }

        public boolean wasFlightEnabled() {
            return !previousFlightState && newFlightState;
        }

        public boolean wasFlightDisabled() {
            return previousFlightState && !newFlightState;
        }
    }
}
