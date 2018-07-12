package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Size;

final class SizeImpl implements Size {

    private final int width;
    private final int height;

    SizeImpl(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }
}
