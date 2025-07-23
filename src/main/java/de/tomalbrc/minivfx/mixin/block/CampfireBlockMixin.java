package de.tomalbrc.dropvfx.mixin.block;

import de.tomalbrc.dropvfx.impl.CampfireEmitter;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin implements BlockWithElementHolder {
    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        boolean soulfire = initialBlockState.getBlock() == Blocks.SOUL_CAMPFIRE;
        return new CampfireEmitter(soulfire,  soulfire ? 8 : 2, 0.5f, 1.f, -0.15f, 10, 5, initialBlockState.getValue(CampfireBlock.LIT));
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }
}
