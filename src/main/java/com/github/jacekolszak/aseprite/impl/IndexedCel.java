package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.Color;
import com.github.jacekolszak.aseprite.Palette;
import com.github.jacekolszak.aseprite.Pixel;

final class IndexedCel implements Cel {

    private final Pixel[][] pixels;

    IndexedCel(int xPosition, int yPosition, int width, int height, Palette palette, byte[] data) {
        this.pixels = new Pixel[xPosition + width][yPosition + height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = y * width + x;
                byte index = data[offset];
                Color color = palette.at(index);
                pixels[xPosition + x][yPosition + y] = new PixelImpl(xPosition + x, yPosition + y, color, index);
            }
        }
    }

    @Override
    public Pixel pixel(int x, int y) {
        return pixels[x][y];
    }

}
