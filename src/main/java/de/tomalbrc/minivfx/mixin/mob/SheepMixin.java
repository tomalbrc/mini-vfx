package de.tomalbrc.dropvfx.mixin.mob;

import de.tomalbrc.dropvfx.event.SheepEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public class SheepMixin {
    @Inject(method = "method_61475", at = @At(value = "TAIL"))
    private void vfx$shear(ServerLevel serverLevel, ItemStack itemStack, CallbackInfo ci) {
        SheepEvents.SHEAR.invoker().shear(serverLevel, (Sheep)(Object)this, itemStack);
    }
}
