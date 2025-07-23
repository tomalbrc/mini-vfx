package de.tomalbrc.dropvfx.mixin.block;

import de.tomalbrc.dropvfx.impl.JukeboxEmitter;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxSongPlayer.class)
public class JukeboxSongPlayerMixin {
    @Shadow
    @Final
    private BlockPos blockPos;

    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;levelEvent(Lnet/minecraft/world/entity/Entity;ILnet/minecraft/core/BlockPos;I)V"))
    private void vfx$play(LevelAccessor levelAccessor, Holder<JukeboxSong> holder, CallbackInfo ci) {
        var attachment = vfx$attachment(levelAccessor);
        if (attachment != null) attachment.start();
    }

    @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void vfx$stop(LevelAccessor levelAccessor, BlockState blockState, CallbackInfo ci) {
        var attachment = vfx$attachment(levelAccessor);
        if (attachment != null) attachment.stop();
    }

    @Unique
    private @Nullable JukeboxEmitter vfx$attachment(LevelAccessor levelAccessor) {
        var self = ((JukeboxSongPlayer) (Object) this);
        if (self == null || !(levelAccessor instanceof ServerLevel serverLevel)) {
            return null;
        }

        var att = BlockBoundAttachment.get(serverLevel, blockPos);
        return att != null ? (JukeboxEmitter) att.holder() : null;
    }
}
