package de.tomalbrc.dropvfx.impl;

import de.tomalbrc.dropvfx.config.ModConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VfxMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (is(mixinClassName, "ItemEntityMixin")) {
            return ModConfig.getInstance().item.enabled;
        }

        // mob
        if (is(mixinClassName, "mob.CowMixin")) {
            return ModConfig.getInstance().cow.milk;
        }

        if (is(mixinClassName, "mob.MushroomCowShearMixin")) {
            return ModConfig.getInstance().cow.shear;
        }

        if (is(mixinClassName, "mob.MushroomCowSoupMixin")) {
            return ModConfig.getInstance().cow.soup;
        }

        if (is(mixinClassName, "mob.SheepMixin")) {
            return ModConfig.getInstance().sheep.shear;
        }

        if (is(mixinClassName, "AbstractArrowMixin")) {
            return ModConfig.getInstance().arrow.trail;
        }

        if (is(mixinClassName, "block.CampfireBlockMixin")) {
            return ModConfig.getInstance().campfire.enabled;
        }

        if (is(mixinClassName, "block.ServerLevelMixin")) {
            return ModConfig.getInstance().explosion.enabled;
        }

        if (is(mixinClassName, "block.JukeboxBlockMixin") || is(mixinClassName, "block.JukeboxSongPlayerMixin")) {
            return ModConfig.getInstance().jukebox.enabled;
        }

        if (is(mixinClassName, "block.TorchBlockMixin") || is(mixinClassName, "block.WallTorchBlockMixin")) {
            return ModConfig.getInstance().torch.enabled;
        }

        return true;
    }

    private boolean is(String mixinClassName, String s2) {
        return ("de.tomalbrc.dropvfx.mixin." + s2).equals(mixinClassName);
    }
}