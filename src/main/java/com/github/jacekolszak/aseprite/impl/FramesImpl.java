package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Frame;
import com.github.jacekolszak.aseprite.Frames;

public final class FramesImpl implements Frames {

    private final ASE ase;

    public FramesImpl(ASE ase) {
        this.ase = ase;
    }

    @Override
    public int count() {
        return ase.header().frames();
    }

    @Override
    public Frame frame(int number) {
        return new FrameImpl(ase.frame(number));
    }
}
