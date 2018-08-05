package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.Color;
import com.github.jacekolszak.aseprite.Pixel;

final class RGBACel implements Cel {

    private final Pixel[][] pixels;

    RGBACel(int xPosition, int yPosition, int width, int height, byte[] data) {
        pixels = new Pixel[xPosition + width][yPosition + height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int offset = y * width * 4 + x * 4;
                int red = Byte.toUnsignedInt(data[offset]);
                int green = Byte.toUnsignedInt(data[offset + 1]);
                int blue = Byte.toUnsignedInt(data[offset + 2]);
                int alpha = Byte.toUnsignedInt(data[offset + 3]);
                Color color = new ColorImpl(red, green, blue, alpha);
                pixels[xPosition + x][yPosition + y] = new PixelImpl(xPosition + x, yPosition + y, color, -1);
            }
        }
    }

    @Override
    public Pixel pixel(int x, int y) {
        return pixels[x][y];
    }

}
