package de.tomalbrc.dropvfx.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.AbstractCow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;

public class CowEvents {
    public static Event<CowMilkEvent> MILK = EventFactory.createArrayBacked(CowMilkEvent.class, (listeners) -> (player, cow) -> {
        for (CowMilkEvent listener : listeners) {
            listener.milk(player, cow);
        }
    });

    public static Event<MushroomCowSoupEvent> SOUP = EventFactory.createArrayBacked(MushroomCowSoupEvent.class, (listeners) -> (player, cow) -> {
        for (MushroomCowSoupEvent listener : listeners) {
            listener.soup(player, cow);
        }
    });

    public static Event<MushroomCowShearEvent> SHEAR = EventFactory.createArrayBacked(MushroomCowShearEvent.class, (listeners) -> (player, cow) -> {
        for (MushroomCowShearEvent listener : listeners) {
            listener.shear(player, cow);
        }
    });

    @FunctionalInterface
    public interface MushroomCowShearEvent {
        void shear(Player player, MushroomCow cow);
    }

    @FunctionalInterface
    public interface MushroomCowSoupEvent {
        void soup(Player player, AbstractCow cow);
    }

    @FunctionalInterface
    public interface CowMilkEvent {
        void milk(Player player, AbstractCow cow);
    }
}