package de.tomalbrc.dropvfx.impl;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParticleExplosionEmitter extends ElementHolder {
    private final List<ParticleEmitter> rings = new ObjectArrayList<>();
    private final List<ParticleEmitter> sparks = new ObjectArrayList<>();
    private final RandomSource randomSource;

    private int age = 0;

    private final List<ParticleEmitter> elements = new ObjectArrayList<>();

    public ParticleExplosionEmitter(RandomSource randomSource) {
        super();
        this.randomSource = randomSource;
        for (int i = 0; i < 8; i++) {
            var off = new Vec3(randomSource.nextFloat() * 0.1, 0, randomSource.nextFloat() * 0.1);
            var vel = new Vec3(randomSource.nextFloat() - 0.5, 1.0, randomSource.nextFloat() - 0.5);
            GravityParticleEmitter element = new GravityParticleEmitter(off, vel);
            sparks.add(element);
            elements.add(element);
        }
    }

    @Override
    protected void onTick() {
        super.onTick();

//        if (rings.size() < 2) {
//            ParticleEmitter element = new RingParticleEmitter(randomSource.nextInt(360)*Mth.DEG_TO_RAD, randomSource.nextInt(360)*Mth.DEG_TO_RAD);
//            rings.add(element);
//            elements.add(element);
//        }

        if (age > 1 * 20 && this.getAttachment() != null) {
            this.getAttachment().destroy();
        }

        if (this.getAttachment() != null) {
            for (ParticleEmitter element : this.elements) {
                element.tick(this.getAttachment().getWorld(), this.getPos(), this.age);
            }
        }

        age++;
    }

    private interface ParticleEmitter {
        void tick(ServerLevel serverLevel, Vec3 pos, int holderAge);
    }

    private static class GravityParticleEmitter implements ParticleEmitter {
        private long age = 0;
        private Vec3 vel;
        private Vec3 off;

        public GravityParticleEmitter(Vec3 offset, Vec3 vel) {
            this.vel = vel;
            this.off = offset;
        }

        @Override
        public void tick(ServerLevel serverLevel, Vec3 pos, int holderAge) {
            // update pos
            var p = pos.add(off);

            float scale = 0.25f + age*0.8f * 0.1f; // scale goes 0→1→0
            serverLevel.sendParticles(new DustParticleOptions(getSparkColor(age), scale), p.x, p.y, p.z, 0, 0f, 0f, 0f, 0f);

            this.off = this.off.add(this.vel);
            this.vel = this.vel.scale(0.9).add(0, -0.125, 0);

            this.age++;
        }

        private static int lerpColor(int c1, int c2, float t) {
            int r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
            int r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
            int r = (int)(r1 + (r2 - r1) * t);
            int g = (int)(g1 + (g2 - g1) * t);
            int b = (int)(b1 + (b2 - b1) * t);
            return (r << 16) | (g << 8) | b;
        }

        private static int getSparkColor(long age) {
            float t = age / 30f; //20 ticks
            if (t < 0.33f) return lerpColor(0xFFA500, 0xFF0000, t / 0.33f);
            else if (t < 0.66f) return lerpColor(0xFF0000, 0x000000, (t - 0.33f) / 0.33f);
            else return lerpColor(0x000000, 0x404040, (t - 0.66f) / 0.34f);
        }

    }

    private static class RingParticleEmitter implements ParticleEmitter {
        private long age = 0;
        private float pitch = 0;
        private float yaw = 0;

        public RingParticleEmitter(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        private static int lerpRed(int from, int to, float t) {
            int r1 = (from >> 16) & 0xFF, g1 = (from >> 8) & 0xFF, b1 = from & 0xFF;
            int r2 = (to >> 16) & 0xFF;
            int r = (int)(r1 + (r2 - r1) * t);
            return (r << 16) | (g1 << 8) | b1;
        }

        @Override
        public void tick(ServerLevel serverLevel, Vec3 pos, int holderAge) {
            // update pos
            if (age > 20)
                return;

            int start = 0x101010;
            int target = 0xe01010;
            float t = Math.min(age / 100f, 1f); // 100ticks
            int dynamicColor = lerpRed(start, target, t);

            int steps = 45;
            for (int i = 0; i < steps; i++) {
                var v = new Vec3((2+age)/10.f, 0, 0).yRot(Mth.DEG_TO_RAD * (i * (360f/steps) + (serverLevel.getRandom().nextFloat()*0.1f))).xRot(pitch).yRot(yaw);
                var p = pos.add(v);
                serverLevel.sendParticles(new DustColorTransitionOptions(dynamicColor, dynamicColor, 1f), p.x, p.y, p.z, 0, v.x, v.y, v.z, 10f);
            }

            this.age++;
        }
    }
}
