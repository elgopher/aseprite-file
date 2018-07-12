package com.github.jacekolszak.aseprite;

import java.util.Arrays;
import java.util.Optional;

public enum ColorMode {

    RGBA(32), GRAYSCALE(16), INDEXED(8);

    private final int bitsPerPixel;

    ColorMode(int bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
    }

    public int bitsPerPixel() {
        return bitsPerPixel;
    }

    public static Optional<ColorMode> from(int bitsPerPixel) {
        return Arrays.stream(values())
                .filter(colorMode -> colorMode.bitsPerPixel == bitsPerPixel)
                .findFirst();
    }

}
