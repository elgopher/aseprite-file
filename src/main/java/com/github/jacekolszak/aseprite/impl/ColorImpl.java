package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Color;

final class ColorImpl implements Color {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    ColorImpl(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public int red() {
        return red;
    }

    @Override
    public int green() {
        return green;
    }

    @Override
    public int blue() {
        return blue;
    }

    @Override
    public int alpha() {
        return alpha;
    }
}
