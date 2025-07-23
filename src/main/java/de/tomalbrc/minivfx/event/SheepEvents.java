package de.tomalbrc.dropvfx.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;

public class SheepEvents {
    public static Event<SheepShearEvent> SHEAR = EventFactory.createArrayBacked(SheepShearEvent.class, (listeners) -> (serverLevel, sheep, itemStack) -> {
        for (SheepShearEvent listener : listeners) {
            listener.shear(serverLevel, sheep, itemStack);
        }
    });

    @FunctionalInterface
    public interface SheepShearEvent {
        void shear(ServerLevel serverLevel, Sheep sheep, ItemStack itemStack);
    }
}