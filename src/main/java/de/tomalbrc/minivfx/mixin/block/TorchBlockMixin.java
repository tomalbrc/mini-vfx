package de.tomalbrc.minivfx.mixin.block;

import de.tomalbrc.minivfx.impl.FireEmitter;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TorchBlock.class)
public class TorchBlockMixin implements BlockWithElementHolder {
    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        boolean soulfire = initialBlockState.getBlock() == Blocks.SOUL_TORCH;
        return new FireEmitter(soulfire,  soulfire ? 10 : 5, 1f/16f, 0.5f, 0f, 20, 5);
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }
}
