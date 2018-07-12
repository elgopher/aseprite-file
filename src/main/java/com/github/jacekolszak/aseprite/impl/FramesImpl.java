package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Frames;

public final class FramesImpl implements Frames {

    private final ASE.Header header;

    public FramesImpl(ASE.Header header) {
        this.header = header;
    }

    @Override
    public int count() {
        return header.frames();
    }
}
