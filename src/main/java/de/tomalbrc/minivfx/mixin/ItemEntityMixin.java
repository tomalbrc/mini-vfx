package de.tomalbrc.minivfx.mixin;

import de.tomalbrc.minivfx.impl.ItemEmitter;
import de.tomalbrc.minivfx.impl.ItemColors;
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
    @Unique boolean minivfx$didInit = false;

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "setItem", at = @At("TAIL"))
    public void minivfx$initItem(ItemStack itemStack, CallbackInfo ci) {
        if (!minivfx$didInit) {
            ItemEmitter.attach((ItemEntity) (Object) this, ItemColors.get(getItem().getItem()), getItem().getRarity());
            minivfx$didInit = true;
        }
    }
}
