package de.tomalbrc.minivfx;

import de.tomalbrc.minivfx.config.ModConfig;
import de.tomalbrc.minivfx.event.CowEvents;
import de.tomalbrc.minivfx.event.SheepEvents;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;

import static de.tomalbrc.minivfx.Util.broadcast;
import static de.tomalbrc.minivfx.Util.shapedItemParticles;

public class MiniVfx implements ModInitializer {
    public static final String MOD_ID = "mini-vfx";

    @Override
    public void onInitialize() {
        if (ModConfig.getInstance().item.enabled && ModConfig.getInstance().item.useRP) {
            PolymerResourcePackUtils.addModAssets(MOD_ID);
            PolymerResourcePackUtils.markAsRequired();
        }

        SheepEvents.SHEAR.register((serverLevel, sheep, itemStack) -> {
            if (!itemStack.isEmpty()) {
                var packet = shapedItemParticles(Shapes.block(), itemStack, sheep.position().add(0, sheep.getBbHeight() / 2, 0.5));
                if (packet != null) broadcast(serverLevel, sheep.position(), packet, 16);
            }
        });

        CowEvents.SHEAR.register((player, cow) -> {
            var packet = shapedItemParticles(Shapes.block(), cow.getVariant() == MushroomCow.Variant.RED ? Items.RED_MUSHROOM.getDefaultInstance() : Items.BROWN_MUSHROOM.getDefaultInstance(), cow.position().add(0, cow.getBbHeight() / 2, 0));
            if (packet != null) broadcast((ServerLevel) player.level(), cow.position(), packet);
        });

        CowEvents.MILK.register((player, cow) -> {
            var packet = shapedItemParticles(Block.cube(0.5), Items.WHITE_CONCRETE.getDefaultInstance(), cow.position().add(0, cow.getBbHeight() / 6, 0));
            if (packet != null) broadcast((ServerLevel) player.level(), cow.position(), packet);
        });

        CowEvents.SOUP.register((player, cow) -> {
            var packet = shapedItemParticles(Block.cube(0.5), Items.TERRACOTTA.getDefaultInstance(), cow.position().add(0, cow.getBbHeight() / 6, 0));
            if (packet != null) broadcast((ServerLevel) player.level(), cow.position(), packet);
        });

        if (ModConfig.getInstance().hurtEffect) {
            ServerLivingEntityEvents.AFTER_DAMAGE.register((livingEntity, damageSource, v, v1, b) -> {
                var packet = shapedItemParticles(Block.cube(0.6), Items.REDSTONE_BLOCK.getDefaultInstance(), Items.RED_NETHER_BRICKS.getDefaultInstance(), livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0));
                if (packet != null) broadcast((ServerLevel) livingEntity.level(), livingEntity.position(), packet);
            });

            ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
                var packet = shapedItemParticles(Shapes.block(), Items.REDSTONE_BLOCK.getDefaultInstance(), Items.RED_NETHER_BRICKS.getDefaultInstance(), livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0));
                if (packet != null) broadcast((ServerLevel) livingEntity.level(), livingEntity.position(), packet);
            });
        }

        ServerLifecycleEvents.SERVER_STARTED.register(Util::loadColors);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> Util.loadColors(server));
    }
}
