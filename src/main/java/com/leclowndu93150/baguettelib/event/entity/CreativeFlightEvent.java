package com.leclowndu93150.baguettelib.event.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class CreativeFlightEvent extends Event {
    protected final Player player;

    protected CreativeFlightEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Fired when a player attempts to toggle creative flight mode.
     * Can be canceled to prevent the flight toggle.
     * Can also modify the flight state that will be applied.
     */
    @Cancelable
    public static class Toggle extends CreativeFlightEvent {
        private boolean flightState;

        public Toggle(Player player, boolean flightState) {
            super(player);
            this.flightState = flightState;
        }

        /**
         * Get the flight state that will be applied.
         * @return The flight state (can be modified by setFlightState)
         */
        public boolean getFlightState() {
            return flightState;
        }

        /**
         * Set the flight state that will be applied.
         * This allows event handlers to override the intended flight state.
         * @param flightState The new flight state to apply
         */
        public void setFlightState(boolean flightState) {
            this.flightState = flightState;
        }

        public boolean isEnablingFlight() {
            return flightState;
        }

        public boolean isDisablingFlight() {
            return !flightState;
        }
    }

    /**
     * Fired after creative flight state has been changed.
     * Cannot be canceled.
     */
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
