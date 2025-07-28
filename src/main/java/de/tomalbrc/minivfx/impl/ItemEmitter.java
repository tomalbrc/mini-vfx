package de.tomalbrc.minivfx.impl;

import de.tomalbrc.minivfx.MiniVfx;
import de.tomalbrc.minivfx.config.ModConfig;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DyedItemColor;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class ItemEmitter extends ElementHolder {
    final private static ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(MiniVfx.MOD_ID, "fx");

    final ItemDisplayElement itemDisplayElement;
    final ItemEntity parent;
    final int color;
    final Rarity rarity;
    final int[] palette;
    final int mod;

    final boolean particles;

    public ItemEmitter(ItemEntity itemEntity, int color, Rarity rarity) {
        super();

        this.parent = itemEntity;
        this.rarity = rarity;
        this.color = color;

        if (rarity == Rarity.COMMON && (ModConfig.getInstance().item.alwaysUseModel || itemEntity.getItem().getCustomName() != null || itemEntity.getItem().isEnchanted()))
            rarity = Rarity.UNCOMMON;

        this.mod = mod();
        this.particles = ModConfig.getInstance().itemRarities.get(rarity.getSerializedName()).showParticles;

        this.palette = new int[6];
        var len = this.palette.length;
        for (int i = 0; i < len; i++) {
            this.palette[i] = ItemColors.shiftHue(color, -(len / 2f) + i);
        }

        if (ModConfig.getInstance().item.useRP && rarity != Rarity.COMMON) {
            var stack = Items.PAPER.getDefaultInstance();
            stack.set(DataComponents.ITEM_MODEL, MODEL);
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(ItemColors.brighten(color, 0.2f)));
            stack.set(DataComponents.RARITY, rarity);

            this.itemDisplayElement = new ItemDisplayElement(stack);
            this.itemDisplayElement.setTeleportDuration(1);
            this.itemDisplayElement.setTeleportDuration(1);
            this.itemDisplayElement.setItemDisplayContext(ItemDisplayContext.NONE);
            this.itemDisplayElement.setTranslation(new Vector3f(0f, 0.25f + (2.5f/16f), 0f));
            this.itemDisplayElement.setBillboardMode(Display.BillboardConstraints.VERTICAL);
            this.addElement(itemDisplayElement);
        }
        else {
            this.itemDisplayElement = null;
        }
    }

    protected int mod() {
        return switch (rarity) {
            case UNCOMMON -> 4;
            case RARE -> 3;
            case EPIC -> 2;
            default -> 5;
        };
    }

    @Override
    protected void startWatchingExtraPackets(ServerGamePacketListenerImpl player, Consumer<Packet<ClientGamePacketListener>> packetConsumer) {
        super.startWatchingExtraPackets(player, packetConsumer);

        if (this.itemDisplayElement != null) packetConsumer.accept(VirtualEntityUtils.createRidePacket(this.parent.getId(), IntList.of(this.itemDisplayElement.getEntityId())));
    }

    @Override
    protected void onTick() {
        super.onTick();

        if (!particles)
            return;

        var attachment = this.getAttachment();
        if (attachment != null && this.parent.getAge() > 5) {
            var serverLevel = attachment.getWorld();
            if (serverLevel.getGameTime() % this.mod == 0) {
                var pos = this.parent.position().add(serverLevel.getRandom().nextFloat()*0.5 - 0.25f, serverLevel.getRandom().nextFloat() * 0.5f + 0.1f, serverLevel.getRandom().nextFloat()*0.5 - 0.25f);
                TrailParticleOption trailParticleOption = new TrailParticleOption(pos.add(0, 1.0, 0), this.palette[serverLevel.getRandom().nextInt(this.palette.length)], serverLevel.getRandom().nextInt(5) + 10);
                serverLevel.sendParticles(trailParticleOption, false, false, pos.x, pos.y, pos.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public static void attach(ItemEntity entity, int color, Rarity rarity) {
        var model = new ItemEmitter(entity, color, rarity);
        if (ModConfig.getInstance().itemRarities.get(rarity.getSerializedName()).showParticles)
            EntityAttachment.ofTicking(model, entity);
        else
            EntityAttachment.of(model, entity);
    }
}
