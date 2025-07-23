package de.tomalbrc.dropvfx.config;

import de.tomalbrc.dropvfx.impl.ItemColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public class DefaultColors {
    public static void blockColors() {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block.defaultMapColor() != MapColor.NONE) {
                var item = block.asItem();
                ItemColors.put(item, ItemColors.brighten(block.defaultMapColor().col, 0.2f));
            }
        }
    }

    public static void specialColors() {
        ItemColors.put(Items.DIAMOND,             0x2CBAA8);
        ItemColors.put(Items.EMERALD,             0x47A036);
        ItemColors.put(Items.NETHERITE_INGOT,     0x443A3B);
        ItemColors.put(Items.IRON_INGOT,          0xe0e0e0);
        ItemColors.put(Items.GOLD_INGOT,          0xDEB12D);
        ItemColors.put(Items.NETHER_STAR,         0xFFFF55);
        ItemColors.put(Items.TOTEM_OF_UNDYING,    0xFFFF55);
        ItemColors.put(Items.ENCHANTED_GOLDEN_APPLE, 0xFF55FF);
        ItemColors.put(Items.ENCHANTED_BOOK,      0xFF55FF);
        ItemColors.put(Items.BEACON,              0x55FFFF);
        ItemColors.put(Items.ELYTRA,              0x5555FF);
    }

    public static void toolColors() {
        // Tools & Weapons
        ItemColors.put(Items.DIAMOND_PICKAXE,    0x2CBAA8);
        ItemColors.put(Items.DIAMOND_AXE,        0x2CBAA8);
        ItemColors.put(Items.DIAMOND_SWORD,      0x2CBAA8);
        ItemColors.put(Items.IRON_PICKAXE,       0xCECACA);
        ItemColors.put(Items.IRON_SWORD,         0xCECACA);
        ItemColors.put(Items.IRON_HOE,         0xCECACA);
        ItemColors.put(Items.GOLDEN_PICKAXE,     0xDEB12D);
        ItemColors.put(Items.GOLDEN_SWORD,       0xDEB12D);
        ItemColors.put(Items.GOLDEN_HOE,       0xDEB12D);
        ItemColors.put(Items.NETHERITE_PICKAXE,  0x443A3B);
        ItemColors.put(Items.NETHERITE_SWORD,    0x443A3B);
        ItemColors.put(Items.NETHERITE_HOE,    0x443A3B);
        ItemColors.put(Items.ELYTRA,             0x443A3B);
        ItemColors.put(Items.TRIDENT,            0x55FFFF);
    }

    public static void armorColors() {
        // Armor
        ItemColors.put(Items.DIAMOND_HELMET,     0x2CBAA8);
        ItemColors.put(Items.DIAMOND_CHESTPLATE, 0x2CBAA8);
        ItemColors.put(Items.DIAMOND_LEGGINGS,   0x2CBAA8);
        ItemColors.put(Items.DIAMOND_BOOTS,      0x2CBAA8);

        ItemColors.put(Items.IRON_HELMET,        0xCECACA);
        ItemColors.put(Items.IRON_CHESTPLATE,    0xCECACA);
        ItemColors.put(Items.IRON_LEGGINGS,      0xCECACA);
        ItemColors.put(Items.IRON_BOOTS,         0xCECACA);

        ItemColors.put(Items.GOLDEN_HELMET,      0xDEB12D);
        ItemColors.put(Items.GOLDEN_CHESTPLATE,  0xDEB12D);
        ItemColors.put(Items.GOLDEN_LEGGINGS,    0xDEB12D);
        ItemColors.put(Items.GOLDEN_BOOTS,       0xDEB12D);

        ItemColors.put(Items.NETHERITE_HELMET,   0x443A3B);
        ItemColors.put(Items.NETHERITE_CHESTPLATE,0x443A3B);
        ItemColors.put(Items.NETHERITE_LEGGINGS, 0x443A3B);
        ItemColors.put(Items.NETHERITE_BOOTS,    0x443A3B);
    }
}
