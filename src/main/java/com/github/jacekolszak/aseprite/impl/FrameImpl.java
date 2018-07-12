package com.github.jacekolszak.aseprite.impl;

import java.time.Duration;

import com.github.jacekolszak.aseprite.Frame;

final class FrameImpl implements Frame {

    private final ASE.Frame frame;

    FrameImpl(ASE.Frame frame) {
        this.frame = frame;
    }

    @Override
    public Duration duration() {
        return Duration.ofMillis(frame.duration());
    }
}
