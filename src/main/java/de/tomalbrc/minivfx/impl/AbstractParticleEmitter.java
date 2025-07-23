package de.tomalbrc.minivfx.impl;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractParticleEmitter extends ElementHolder {
    protected final int mod;
    protected final float radius;
    protected final float height;
    protected final float yOffset;

    public AbstractParticleEmitter(int mod, float radius, float height, float yOffset) {
        super();
        this.mod = mod;
        this.radius = radius;
        this.height = height;
        this.yOffset = yOffset;
    }

    @Override
    protected void onTick() {
        super.onTick();

        var attachment = this.getAttachment();
        if (attachment != null && this.canEmit()) {
            var serverLevel = attachment.getWorld();
            var pos = getParticlePos(serverLevel);
            this.sendParticles(serverLevel, pos);
        }
    }

    protected boolean canEmit() {
        var attachment = this.getAttachment();
        return attachment != null && (attachment.getWorld().getGameTime() % this.mod) == 0;
    }

    abstract protected ParticleOptions getParticleOptions(ServerLevel serverLevel, Vec3 pos);

    abstract protected Vec3 getParticlePos(ServerLevel serverLevel);

    protected void sendParticles(ServerLevel serverLevel, Vec3 pos) {
        serverLevel.sendParticles(getParticleOptions(serverLevel, pos), false, false, pos.x, pos.y, pos.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
    }
}
