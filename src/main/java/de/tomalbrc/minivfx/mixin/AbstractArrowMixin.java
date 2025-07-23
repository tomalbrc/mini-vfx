package de.tomalbrc.dropvfx.mixin;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    @Shadow protected abstract boolean isInGround();

    public AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void vfx$arrowParticle(CallbackInfo ci) {
        if (!isInGround()) {
            var level = (ServerLevel)level();
            var pos = position();
            level.sendParticles(new DustColorTransitionOptions(isOnFire() ? 0xe05131 : 0x7f7f7f, 0x212121, 1f), pos.x, pos.y, pos.z, 0, 0f, 0f, 0f, 0f);
        }
    }
}
