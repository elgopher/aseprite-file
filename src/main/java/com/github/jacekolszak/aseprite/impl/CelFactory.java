package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.ColorMode;
import com.github.jacekolszak.aseprite.Palette;

public interface CelFactory {

    Cel create(int xPosition, int yPosition, int width, int height, byte[] data);

    static CelFactory newCelFactory(ColorMode colorMode, Palette palette) {
        if (colorMode.equals(ColorMode.INDEXED)) {
            return (xPosition, yPosition, width, height, data) -> new IndexedCel(xPosition, yPosition, width, height, palette,
                    data);
        } else if (colorMode.equals(ColorMode.RGBA)) {
            return RGBACel::new;
        } else {
            return null;
        }
    }

}
