package de.tomalbrc.minivfx.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public record ColorEntry(HolderSet<Item> items, ColorRGB color) {
    public static final Codec<ColorEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(ColorEntry::items),
            ColorRGB.CODEC.fieldOf("color").forGetter(ColorEntry::color)
    ).apply(instance, ColorEntry::new));
}
