package de.tomalbrc.minivfx.impl;

import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class FacingFireEmitter extends FireEmitter {
    private Vec3 offset;

    public FacingFireEmitter(Direction direction, boolean soulfire, int mod, float radius, float height, float yOffset, int time, int randomTime) {
        super(soulfire, mod, radius, height, yOffset, time, randomTime);
        this.offset = from(direction);
    }

    private Vec3 from(Direction direction) {
        return direction.getUnitVec3().scale(-0.25);
    }

    protected Vec3 getParticlePos(ServerLevel serverLevel) {
        return Objects.requireNonNull(getAttachment()).getPos().add(offset).add(serverLevel.getRandom().nextFloat() * (2.f * radius) - radius, serverLevel.getRandom().nextFloat() * height + yOffset, serverLevel.getRandom().nextFloat() * (2.f * radius) - radius);
    }

    @Override
    public void notifyUpdate(HolderAttachment.UpdateType updateType) {
        super.notifyUpdate(updateType);

        if (updateType == BlockAwareAttachment.BLOCK_STATE_UPDATE) {
            var attachment = ((BlockAwareAttachment) getAttachment());
            var direction = attachment.getBlockState().getValue(WallTorchBlock.FACING);
            this.offset = from(direction);
        }
    }
}
