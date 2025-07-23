package de.tomalbrc.minivfx.impl;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class JukeboxEmitter extends AbstractParticleEmitter {
    private boolean on = false;

    public JukeboxEmitter(int mod, float radius, float height, float yOffset) {
        super(mod, radius, height, yOffset);
    }

    @Override
    protected boolean canEmit() {
        return super.canEmit() && on;
    }

    @Override
    protected Vec3 getParticlePos(ServerLevel serverLevel) {
        return Objects.requireNonNull(getAttachment()).getPos().add(serverLevel.getRandom().nextFloat() * (2.f * radius) - radius, (serverLevel.getRandom().nextFloat() * radius) + yOffset, serverLevel.getRandom().nextFloat() * (2.f * radius) - radius);
    }

    @Override
    protected void sendParticles(ServerLevel serverLevel, Vec3 pos) {
        serverLevel.sendParticles(getParticleOptions(serverLevel, pos), false, false, pos.x, pos.y, pos.z, 0, serverLevel.getRandom().nextDouble(), 0.0F, 0.0F, serverLevel.getRandom().nextDouble());
    }

    @Override
    protected ParticleOptions getParticleOptions(ServerLevel serverLevel, Vec3 pos) {
        return ParticleTypes.NOTE;
    }

    public void start() {
        on = true;
    }

    public void stop() {
        on = false;
    }
}
