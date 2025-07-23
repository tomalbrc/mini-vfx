package de.tomalbrc.dropvfx.mixin;

import de.tomalbrc.dropvfx.impl.ItemEmitter;
import de.tomalbrc.dropvfx.impl.ItemColors;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Unique boolean dropvfx$didInit = false;

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "setItem", at = @At("TAIL"))
    public void dropvfx$initItem(ItemStack itemStack, CallbackInfo ci) {
        if (!dropvfx$didInit) {
            ItemEmitter.attach((ItemEntity) (Object) this, ItemColors.get(getItem().getItem()), getItem().getRarity());
            dropvfx$didInit = true;
        }
    }
}
