package de.tomalbrc.minivfx.mixin;

import de.tomalbrc.minivfx.impl.ItemColors;
import de.tomalbrc.minivfx.impl.ItemEmitter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Unique boolean minivfx$didInit = false;

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "setItem", at = @At("TAIL"))
    public void minivfx$initItem(ItemStack itemStack, CallbackInfo ci) {
        if (!minivfx$didInit && level() instanceof ServerLevel) {
            ItemEmitter.attach((ItemEntity) (Object) this, ItemColors.get(getItem().getItem()), getItem().getRarity());
            minivfx$didInit = true;
        }
    }
}
