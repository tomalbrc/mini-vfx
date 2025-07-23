package de.tomalbrc.dropvfx.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Locale;

public record ColorRGB(int rgb) {
    private static final String PREFIX = "#";
    public static final Codec<ColorRGB> CODEC;

    private String formatValue() {
        return String.format(Locale.ROOT, "%s%06X", PREFIX, this.rgb);
    }

    public String toString() {
        return this.formatValue();
    }

    static {
        CODEC = Codec.STRING.comapFlatMap((string) -> {
            if (!string.startsWith(PREFIX)) {
                return DataResult.error(() -> "Not a color code: " + string);
            } else {
                try {
                    int i = (int)Long.parseLong(string.substring(1), 16);
                    return DataResult.success(new ColorRGB(i));
                } catch (NumberFormatException numberFormatException) {
                    return DataResult.error(() -> "Exception parsing color code: " + numberFormatException.getMessage());
                }
            }
        }, ColorRGB::formatValue);
    }
}