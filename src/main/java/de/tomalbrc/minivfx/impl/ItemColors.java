package de.tomalbrc.minivfx.impl;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ItemColors {
    public static final Map<Item, Integer> colormap = new Reference2IntOpenHashMap<>();

    public static void put(Item item, int color) {
        colormap.put(item, color);
    }

    public static void put(TagKey<Item> item, int color) {
        var it = BuiltInRegistries.ITEM.getTagOrEmpty(item);
        for (Holder<Item> holder : it) {
            colormap.put(holder.value(), color);
        }
    }

    public static int get(Item item) {
        return colormap.getOrDefault(item, 0x7f7f7f);
    }

    public static void clear() {
        colormap.clear();
    }

    public static int shiftHue(int rgb, float deg) {
        float r = (rgb >> 16 & 255) / 255f, g = (rgb >> 8 & 255) / 255f, b = (rgb & 255) / 255f;
        float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b));
        float h = 0, s = max == 0 ? 0 : (max - min) / max, v = max;
        if (max != min) {
            if (max == r) h = (g - b) / (max - min);
            else if (max == g) h = 2 + (b - r) / (max - min);
            else h = 4 + (r - g) / (max - min);
            h = (h * 60 + 360) % 360;
        }
        h = (h + deg + 360) % 360;
        float c = v * s, x = c * (1 - Math.abs(h / 60 % 2 - 1)), m = v - c;
        float rf = 0, gf = 0, bf = 0;
        if (h < 60) {
            rf = c;
            gf = x;
        } else if (h < 120) {
            rf = x;
            gf = c;
        } else if (h < 180) {
            gf = c;
            bf = x;
        } else if (h < 240) {
            gf = x;
            bf = c;
        } else if (h < 300) {
            rf = x;
            bf = c;
        } else {
            rf = c;
            bf = x;
        }
        int ri = (int) ((rf + m) * 255), gi = (int) ((gf + m) * 255), bi = (int) ((bf + m) * 255);
        return (ri << 16) | (gi << 8) | bi;
    }

    public static int desaturate(int rgb, float amount) {
        int r = (rgb >> 16) & 255, g = (rgb >> 8) & 255, b = rgb & 255;
        int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
        r += (int)((gray - r) * amount);
        g += (int)((gray - g) * amount);
        b += (int)((gray - b) * amount);
        return (r << 16) | (g << 8) | b;
    }

    public static int brighten(int rgb, float amount) {
        int r = (rgb >> 16) & 255, g = (rgb >> 8) & 255, b = rgb & 255;
        r = Math.min(255, (int)(r + (255 - r) * amount));
        g = Math.min(255, (int)(g + (255 - g) * amount));
        b = Math.min(255, (int)(b + (255 - b) * amount));
        return (r << 16) | (g << 8) | b;
    }
}
