package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.ColorMode;
import com.github.jacekolszak.aseprite.Size;
import com.github.jacekolszak.aseprite.Sprite;

public final class SpriteImpl implements Sprite {

    private final ASE.Header header;

    public SpriteImpl(ASE.Header header) {
        this.header = header;
    }

    @Override
    public Size size() {
        return new SizeImpl(header.width(), header.height());
    }

    @Override
    public ColorMode colorMode() {
        return ColorMode.from(header.colorDepth())
                .orElseThrow(
                        () -> new RuntimeException("Not supported color mode of " + header.colorDepth() + " bits per pixel"));
    }
}
