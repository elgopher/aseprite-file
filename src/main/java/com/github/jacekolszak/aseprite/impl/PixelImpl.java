package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Color;
import com.github.jacekolszak.aseprite.Pixel;

final class PixelImpl implements Pixel {

    private final int x;
    private final int y;
    private final Color color;
    private final int index;

    PixelImpl(int x, int y, Color color, int index) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.index = index;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int index() {
        return index;
    }
}
