package de.tomalbrc.dropvfx.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import de.tomalbrc.dropvfx.impl.ParticleExplosionEmitter;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerExplosion;explode()V"))
    private void vfx$explosion(Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionDamageCalculator, double d, double e, double f, float g, boolean bl, Level.ExplosionInteraction explosionInteraction, ParticleOptions particleOptions, ParticleOptions particleOptions2, Holder<SoundEvent> holder, CallbackInfo ci, @Local ServerExplosion explosion) {
        if (!explosion.isSmall()) {
            ChunkAttachment.ofTicking(new ParticleExplosionEmitter(entity.getRandom()), ServerLevel.class.cast(this), entity.position());
        }
    }
}
