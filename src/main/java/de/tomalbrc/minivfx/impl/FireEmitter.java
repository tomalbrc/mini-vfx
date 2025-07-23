package de.tomalbrc.dropvfx.impl;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class FireEmitter extends AbstractParticleEmitter {
    protected static final int[] NORMAL = new int[]{0xF8D568, 0xFFA500, 0xC46210, 0xC0362C, 0xFF1515, 0x201010};
    protected static final int[] SOUL = new int[]{0x6495ED, 0x599ACC, 0x123456, 0x151B54, 0x101020};

    protected final int[] palette;
    protected final int time;
    protected final int randomTime;

    public FireEmitter(boolean soulfire, int mod, float radius, float height, float yOffset, int time, int randomTime) {
        super(mod, radius, height, yOffset);
        this.palette = soulfire ? SOUL : NORMAL;
        this.time = time;
        this.randomTime = randomTime;
    }

    protected Vec3 getParticlePos(ServerLevel serverLevel) {
        return Objects.requireNonNull(getAttachment()).getPos().add(serverLevel.getRandom().nextFloat()*(2.f*radius) - radius, serverLevel.getRandom().nextFloat() * height + yOffset, serverLevel.getRandom().nextFloat()*(2.f*radius) - radius);
    }

    protected void sendParticles(ServerLevel serverLevel, Vec3 pos) {
        serverLevel.sendParticles(getParticleOptions(serverLevel, pos), false, false, pos.x, pos.y, pos.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    protected ParticleOptions getParticleOptions(ServerLevel serverLevel, Vec3 pos) {
        return new TrailParticleOption(pos.add(0, 1.0, 0), this.palette[serverLevel.getRandom().nextInt(this.palette.length)], serverLevel.getRandom().nextInt(randomTime) + time);
    }
}
