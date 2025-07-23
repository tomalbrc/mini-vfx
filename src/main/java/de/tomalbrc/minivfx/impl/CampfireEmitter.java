package de.tomalbrc.minivfx.impl;

import de.tomalbrc.minivfx.config.ModConfig;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class CampfireEmitter extends FireEmitter {
    private boolean lit;
    private final boolean soulfire;

    public CampfireEmitter(boolean soulfire, int mod, float radius, float height, float yOffset, int time, int randomTime, boolean lit) {
        super(soulfire, mod, radius, height, yOffset, time, randomTime);
        this.lit = lit;
        this.soulfire = soulfire;
    }

    @Override
    protected boolean canEmit() {
        return super.canEmit() && lit;
    }

    protected Vec3 pos(ServerLevel serverLevel) {
        return Objects.requireNonNull(getAttachment()).getPos().add(serverLevel.getRandom().nextFloat() * (radius) - radius / 2f, serverLevel.getRandom().nextFloat() * height + yOffset + 0.2, serverLevel.getRandom().nextFloat() * (radius) - radius / 2f);
    }

    @Override
    protected void sendParticles(ServerLevel serverLevel, Vec3 pos) {
        var dot = serverLevel.getRandom().nextInt(5) != 1;
        if (!dot) pos = pos(serverLevel);
        boolean soulp = false;
        var o = dot ? this.getParticleOptions(serverLevel, pos) : soulfire && (soulp = ModConfig.getInstance().campfire.soulParticles && serverLevel.getRandom().nextInt(4) == 1) ? ParticleTypes.SOUL : new DustColorTransitionOptions(this.palette[serverLevel.getRandom().nextInt(3)], this.palette[this.palette.length - 1], 1.0f);
        serverLevel.sendParticles(o, false, false, pos.x, pos.y, pos.z, dot ? 1 : 0, 0.0F, dot ? 0 : 1.0F, 0.0F, dot ? 0 : soulp ? 0.05 : 2.5F);
    }

    @Override
    public void notifyUpdate(HolderAttachment.UpdateType updateType) {
        super.notifyUpdate(updateType);

        if (updateType == BlockAwareAttachment.BLOCK_STATE_UPDATE) {
            var attachment = ((BlockAwareAttachment) getAttachment());
            this.lit = attachment != null && attachment.getBlockState().getValue(CampfireBlock.LIT);
        }
    }
}
