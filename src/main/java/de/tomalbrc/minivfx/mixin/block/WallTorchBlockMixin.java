package de.tomalbrc.minivfx.mixin.block;

import de.tomalbrc.minivfx.impl.FacingFireEmitter;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WallTorchBlock.class)
public class WallTorchBlockMixin implements BlockWithElementHolder {
    @Override
    public @Nullable ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        boolean soulfire = initialBlockState.getBlock() == Blocks.SOUL_WALL_TORCH;
        return new FacingFireEmitter(initialBlockState.getValue(WallTorchBlock.FACING), soulfire,  soulfire ? 10 : 5, 1f/16f, 0.6f, 0.1f, 20, 5);
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }

}
